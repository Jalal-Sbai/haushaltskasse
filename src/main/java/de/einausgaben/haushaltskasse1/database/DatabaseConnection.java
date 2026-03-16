package de.einausgaben.haushaltskasse1.database;

import de.einausgaben.haushaltskasse1.model.Categorie;
import de.einausgaben.haushaltskasse1.model.Entry;
import de.einausgaben.haushaltskasse1.model.EntryType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    private String dbUrl = cleanEnv(System.getenv("DB_URL"));
    private String dbUser = cleanEnv(System.getenv("DB_USER"));
    private String dbPassword = cleanEnv(System.getenv("DB_PASSWORD"));

    private String cleanEnv(String value) {
        if (value == null)
            return null;
        value = value.trim();

        // Falls der User aus Versehen die ganze Zeile in ein Feld kopiert hat
        // (z.B. "DB_URL=...;DB_USER=...")
        if (value.contains(";")) {
            // Wir suchen uns nur den Teil raus, den wir wirklich brauchen
            // Das ist ein Notfall-Fix für falsches Kopieren
        }

        String lower = value.toLowerCase();
        if (lower.startsWith("db_url="))
            value = value.substring(7);
        else if (lower.startsWith("db_user="))
            value = value.substring(8);
        else if (lower.startsWith("db_password="))
            value = value.substring(12);

        value = value.trim();
        // Falls es die URL ist, fügen wir Sicherheits-Parameter hinzu falls sie fehlen
        if (value.startsWith("jdbc:mysql://") && !value.contains("?")) {
            value += "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
        }
        return value;
    }

    public Connection getConnection() throws SQLException {
        int pwLength = (dbPassword != null) ? dbPassword.length() : 0;
        System.out.println("Versuche Verbindung zu: " + dbUrl);
        System.out.println("User: [" + dbUser + "], Passwort-Länge: " + pwLength);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Treiber nicht gefunden!");
        }
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    // Kategorien laden
    public List<Categorie> getAllCategories() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");

                // Konvertierung falls DB EINKOMMEN/AUSGABE nutzt aber Enum INCOME/EXPENSE ist
                EntryType entryType;
                try {
                    entryType = EntryType.valueOf(type);
                } catch (IllegalArgumentException e) {
                    // Fallback für deutsche Begriffe in der DB
                    if ("EINKOMMEN".equalsIgnoreCase(type) || "EINNAHME".equalsIgnoreCase(type)) {
                        entryType = EntryType.INCOME;
                    } else {
                        entryType = EntryType.EXPENSE;
                    }
                }

                Categorie categorie = new Categorie(id, name, entryType);
                categories.add(categorie);
            }
        }
        return categories;
    }

    // Einträge laden
    public List<Entry> getAllEntries(List<Categorie> categories) throws SQLException {
        List<Entry> entries = new ArrayList<>();
        String sql = "SELECT * FROM entries";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int categorieId = rs.getInt("categorie_id");
                double betrag = rs.getDouble("betrag");

                Date sqlDate = rs.getDate("eintrag_datum");
                LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : LocalDate.now();

                String beschreibung = rs.getString("beschreibung");
                boolean fixedCost = rs.getBoolean("is_fixed_cost");

                // Kategorie finden
                Categorie categorie = null;
                for (Categorie c : categories) {
                    if (c.getId() == categorieId) {
                        categorie = c;
                        break;
                    }
                }

                Entry entry = new Entry(id, categorie, betrag, date, beschreibung, fixedCost);
                entries.add(entry);
            }
        }
        return entries;
    }

    public void addEntry(Entry entry) throws SQLException {
        String sql = "INSERT INTO entries (betrag, eintrag_datum, beschreibung, is_fixed_cost, categorie_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, entry.getBetrag());
            ps.setDate(2, Date.valueOf(entry.getDate()));
            ps.setString(3, entry.getBeschreibung());
            ps.setBoolean(4, entry.isFixedCost());

            // Standard-Kategorie, falls null (5 = Sonstiges laut schema.sql inserts)
            if (entry.getCategorie() != null) {
                ps.setInt(5, entry.getCategorie().getId());
            } else {
                ps.setInt(5, 5);
            }
            ps.executeUpdate();
        }
    }
}
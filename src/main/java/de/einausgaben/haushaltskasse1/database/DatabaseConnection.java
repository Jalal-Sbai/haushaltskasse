
package haushaltskasse.database;

import haushaltskasse.model.*;

import java.sql.*;


public class DatabaseConnection {
    public static void main(String[] args) {
        // Umgebungsvariablen auslesen
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");


        if (dbUrl == null || dbUser == null || dbPassword == null) {
            System.err.println("Bitte Umgebungsvariablen setzen!");
            return;
        }


        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Connected!");
            //Statement st = conn.createStatement();

            String query = "INSERT INTO eintraege (categorie_id, betrag, eintrag_datum, beschreibung, is_fixed_cost) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            int categorie_id = 1;
            double betrag = 20.5;
            String eintrag_datum = "2026-03-12";
            String beschreibung = "Test";
            boolean is_fixed_Cost = false;

            ps.setInt(1, categorie_id);
            ps.setDouble(2, betrag);
            ps.setDate(3, Date.valueOf(eintrag_datum));
            ps.setString(4, beschreibung);
            ps.setBoolean(5, is_fixed_Cost);
            System.out.println("ps= " + ps.executeUpdate() + " " + beschreibung + " / " + is_fixed_Cost);

            ps.executeUpdate();
            System.out.println("Eintrag gespeichert!");

            String selectquery = "select * from categorie";
            PreparedStatement ps2 = conn.prepareStatement(selectquery);

            ResultSet rs = ps2.executeQuery();


            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                System.out.println("id= " + id + " / name:" + name + " / type: " + type);
            }
            conn.close();
            rs.close();


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}


package haushaltskasse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        // Umgebungsvariablen auslesen
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // Prüfen, ob alle Variablen gesetzt sind
        if (dbUrl == null || dbUser == null || dbPassword == null) {
            System.err.println("Bitte DB_URL, DB_USER und DB_PASSWORD als Umgebungsvariablen setzen!");
            return;
        }

        // Try-with-resources sorgt automatisch für das Schließen der Connection
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            System.out.println("Connected!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
package de.einausgaben.haushaltskasse1.view;

import de.einausgaben.haushaltskasse1.database.DatabaseConnection;
import de.einausgaben.haushaltskasse1.model.Entry;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ViewController {

    // Datenbankverbindung
    private DatabaseConnection db = new DatabaseConnection();

    // FXML Controls
    @FXML
    private TextField beschreibungField;
    @FXML
    private TextField betragField;

    @FXML
    private TableView<Entry> table;
    @FXML
    private TableColumn<Entry, String> beschreibungColumn;
    @FXML
    private TableColumn<Entry, Double> betragColumn;

    @FXML
    private Label saldoLabel;

    // Liste der Einträge
    private ObservableList<Entry> eintraege = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Tabellen-Spalten mit Daten verbinden
        beschreibungColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBeschreibung()));

        betragColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBetrag()));

        // Tabelle mit Einträgen verbinden
        table.setItems(eintraege);

        // Daten aus Datenbank laden
        loadFromDatabase();

        // Saldo initial berechnen
        aktualisiereSaldo();
    }

    private void loadFromDatabase() {
        try {
            var categories = db.getAllCategories();
            var entries = db.getAllEntries(categories);
            eintraege.setAll(entries);
        } catch (SQLException e) {
            e.printStackTrace();
            // Zeige Fehler beim Start an, aber nur wenn es wirklich ein Problem gibt
            javafx.application.Platform.runLater(() -> {
                showError("Verbindungs-Fehler",
                        "Konnte keine Daten laden. Prüfe deine MySQL-Zugangsdaten!\n\nFehler: " + e.getMessage());
            });
        }
    }

    // Einnahme hinzufügen
    @FXML
    private void addEinkommen() {
        addEintrag(true);
    }

    // Ausgabe hinzufügen
    @FXML
    private void addAusgabe() {
        addEintrag(false);
    }

    // Allgemeine Methode zum Hinzufügen eines Eintrags
    private void addEintrag(boolean einnahme) {
        String beschreibung = beschreibungField.getText();
        double betrag;

        try {
            betrag = Double.parseDouble(betragField.getText());
        } catch (NumberFormatException e) {
            // Ungültiger Betrag -> nichts tun
            return;
        }

        if (!einnahme) {
            betrag = -betrag;
        }

        // Neuer Eintrag
        Entry entry = new Entry(
                0, // ID (wird von DB vergeben)
                null, // Kategorie (optional für dieses Beispiel)
                betrag,
                LocalDate.now(),
                beschreibung,
                false // feste Kosten
        );

        // In Datenbank speichern
        try {
            db.addEntry(entry);
            eintraege.add(entry); // Nur hinzufügen wenn DB-Speichern okay war?
            // Oder wir lassen es in der Liste, zeigen aber den Fehler:
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Datenbank-Fehler", "Der Eintrag konnte nicht in MySQL gespeichert werden: " + e.getMessage());
        }

        // Felder zurücksetzen
        beschreibungField.clear();
        betragField.clear();

        // Saldo aktualisieren
        aktualisiereSaldo();
    }

    private void showError(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Saldo berechnen
    private void aktualisiereSaldo() {
        double saldo = eintraege.stream().mapToDouble(Entry::getBetrag).sum();
        saldoLabel.setText(String.format("Saldo: %.2f €", saldo));
    }

    // CSV speichern
    @FXML
    private void speichereCSV() {
        try (FileWriter writer = new FileWriter("haushaltskasse.csv")) {
            writer.write("Beschreibung,Betrag\n");
            for (Entry e : eintraege) {
                writer.write(e.getBeschreibung() + "," + e.getBetrag() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
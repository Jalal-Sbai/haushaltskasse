package de.einausgaben.haushaltskasse1.model;

import java.time.LocalDate;

public class Entry {
    private int id;
    private Categorie categorie;
    private double betrag;
    private LocalDate date;
    private String beschreibung;
    private boolean isFesteKosten;

    public Entry(int id, Categorie categorie, double betrag, LocalDate date, String beschreibung, boolean isFesteKosten) {
        this.id = id;
        this.categorie = categorie;
        this.betrag = betrag;
        this.date = date;
        this.beschreibung = beschreibung;
        this.isFesteKosten = isFesteKosten;
    }

    // Getters
    public int getId() {

        return id;
    }

    public Categorie getCategorie() {

        return categorie;
    }

    public double getBetrag() {

        return betrag;
    }

    public LocalDate getDate() {

        return date;
    }

    public String getBeschreibung() {

        return beschreibung;
    }

    public boolean isFixedCost() {

        return isFesteKosten;
    }
}

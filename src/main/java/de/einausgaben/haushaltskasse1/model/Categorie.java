package de.einausgaben.haushaltskasse1.model;

public class Categorie {
    private int id;
    private String name;
    private EntryType type;

    public Categorie(int id, String name, EntryType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public EntryType getType() {

        return type;
    }

    @Override
    public String toString() {

        return name + "  " + type;
    }
}

package org.example.domain;

public enum AntwortType {

    offeneAntwort("Offene Antwort", "Antwort mit Freitext"),
    geschlosseneAntwort("Geschlossene Antwort", "Antwort mit Auswahlmöglichkeiten");

    private final String name;
    private final String beschreibung;

    AntwortType(String name, String beschreibung) {
        this.name = name;
        this.beschreibung = beschreibung;
    }

    public static AntwortType fromName(String name) {
        for (AntwortType type : AntwortType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unbekannter AntwortType: " + name);
    }

    public String getName() {
        return name;
    }
    public String getBeschreibung() {
        return beschreibung;
    }

}

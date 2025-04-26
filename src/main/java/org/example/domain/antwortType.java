package org.example.domain;

public enum antwortType {

    offeneAntwort("Offene Antwort", "Antwort mit Freitext"),
    geschlosseneAntwort("Geschlossene Antwort", "Antwort mit Auswahlmöglichkeiten");

    private final String name;
    private final String beschreibung;

    antwortType(String name, String beschreibung) {
        this.name = name;
        this.beschreibung = beschreibung;
    }
    public String getName() {
        return name;
    }
    public String getBeschreibung() {
        return beschreibung;
    }


}

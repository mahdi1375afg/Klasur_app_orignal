package org.example.domain;

public enum AnswerType {

    offeneAntwort("Offene Antwort", "Antwort mit Freitext"),
    geschlosseneAntwort("Geschlossene Antwort", "Antwort mit Auswahlmöglichkeiten");

    private final String name;
    private final String description;

    AnswerType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static AnswerType fromName(String name) {
        for (AnswerType type : AnswerType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unbekannter AntwortType: " + name);
    }

    public String getName() {
        return name;
    }

}

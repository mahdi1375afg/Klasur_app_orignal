package org.example.domain;

public enum QuestionType {


	singleChoiceFragen("Single-Choice","Der Prüfling wählt eine einzige Antwort aus mehreren Optionen aus."),
	multipleChoiceFragen("Multiple-Choice","Der Prüfling kann eine oder mehrere Antworten aus mehreren Optionen auswählen."),
	wahrOderFalsch("Wahr/Falsch","Der Prüfling gibt an, ob eine Aussage wahr oder falsch ist."),
	leerstellen("Lückentext","Der Prüfling ergänzt einen Text mit vorgegebenen Wörtern (in der Regel eine Option)."),
	zuordnung("Zuordnung","Der Prüfling ordnet Begriffe oder Bilder einander zu."),
	ranking("Ranking","Der Prüfling ordnet eine Liste von Begriffen oder Bildern nach einem bestimmten Kriterium."),
	offen("offen","Der Prüfling kann seine Antwort selber schreiben");

	private final String name;
	private final String beschreibung;

	public static QuestionType fromName(String label) {
		for (QuestionType type : QuestionType.values()) {
			if (type.getName().equalsIgnoreCase(label)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unbekannter CloseType: " + label);
	}

	QuestionType(String name, String beschreibung) {
		this.name = name;
		this.beschreibung = beschreibung;
	}
	public  String getName() {
		return name;
	}
	public String getBeschreibung() {
		return beschreibung;
	}

}
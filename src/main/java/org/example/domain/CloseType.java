package org.example.domain;

public enum CloseType {


	singleChoiceFragen("Single Choice Fragen","Der Prüfling wählt eine einzige Antwort aus mehreren Optionen aus."),
	multipleChoiceFragen("Multiple Choice Fragen","Der Prüfling kann eine oder mehrere Antworten aus mehreren Optionen auswählen."),
	wahrOderFalsch("Wahr oder Falsch Fragen","Der Prüfling gibt an, ob eine Aussage wahr oder falsch ist."),
	leerstellen("lueckentext Fragen","Der Prüfling ergänzt einen Text mit vorgegebenen Wörtern (in der Regel eine Option)."),
	zuordnung("Zuordnungsfragen","Der Prüfling ordnet Begriffe oder Bilder einander zu."),
	ranking("Ranking Fragen","Der Prüfling ordnet eine Liste von Begriffen oder Bildern nach einem bestimmten Kriterium.");

	private final String name;
	private final String beschreibung;

	CloseType(String name, String beschreibung) {
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
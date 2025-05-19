package org.example.domain;

public enum FragenArt {

	GeschlosseneFrage("Geschlossene Fragen", "Überprüfung von Faktenwissen und Sofortige Feedbackmöglichkeiten" ),
	OffeneFrage("Offene Fragen", "Förderung von kritischem Denken - Vertiefung der Analysefähigkeit");

	private final String art;
	private final String beschreibung;

	FragenArt(String art, String beschreibung) {
		this.art = art;
		this.beschreibung = beschreibung;
	}

	public String getArt(){
		return art;
	}
	public String getBeschreibung(){
		return beschreibung;
	}
}
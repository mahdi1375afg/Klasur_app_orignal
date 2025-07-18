package org.example.domain;

public enum BloomLevel {


	erinnern(1, "Erinnern","Gelerntes auswendig wiedergeben, Ausführen von Routinen."),
	verstehen(2, "Verstehen", "Gelerntes erklären, reformulieren oder paraphrasieren."),
	anwenden(3, "Anwenden", "Gelerntes in neuem Kontext / neuer Situation anwenden"),
	analysieren(4,"Analysieren", "Gelerntes in Bestandteile zerlegen, Strukturen erläutern. "),
	bewerten(5, "Bewerten", "Gelerntes nach (meist selbst) gewählten Kriterien kritisch beurteilen "),
	erschaffen(6, "Erschaffen", "Gelerntes neu zusammenfügen oder neue Inhalte generieren. ");
	private  final int nummer;
	private  final String katagorie;
	private  final String beschreibung;

	public static BloomLevel fromCategory(String kategorie) {
		for (BloomLevel level : BloomLevel.values()) {
			if (level.getCategory().equalsIgnoreCase(kategorie)) {
				return level;
			}
		}
		throw new IllegalArgumentException("Unbekannte BloomLevel-Kategorie: " + kategorie);
	}


	BloomLevel(int nummer, String katagorie, String beschreibung) {
		this.nummer = nummer;
		this.katagorie = katagorie;
		this.beschreibung = beschreibung;
	}

	public String getCategory(){
		return katagorie;
	}

}
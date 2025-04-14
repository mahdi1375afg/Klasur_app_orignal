package org.example.domain;

public enum BloomLevel {


	errinern(1, "Erinnern","Gelerntes auswendig wiedergeben, Ausführen von Routinen."),
	verstehen(2, "Verstehen", "Gelerntes erklären, reformulieren oder paraphrasieren."),
	anwenden(3, "Anwenden", "Gelerntes in neuem Kontext / neuer Situation anwenden"),
	analysieren(4,"Analysieren", "Gelerntes in Bestandteile zerlegen, Strukturen erläutern. "),
	bewerten(5, "Bewerten", "Gelerntes nach (meist selbst) gewählten Kriterien kritisch beurteilen "),
	erschaffen(6, "Erschaffen", "Gelerntes neu zusammenfügen oder neue Inhalte generieren. ");
	private  final int nummer;
	private  final String katagorie;
	private  final String beschreibung;

	BloomLevel(int nummer, String katagorie, String beschreibung) {
		this.nummer = nummer;
		this.katagorie = katagorie;
		this.beschreibung = beschreibung;
	}
	public int getNummer(){
		return nummer;
	}
	public String getKatagorie(){
		return katagorie;
	}

	public String getBeschreibung(){
		return beschreibung;
	}

}
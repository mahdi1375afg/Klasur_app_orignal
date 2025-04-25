package org.example.domain;

public class Antwort {

	private int id;
	private String antwortText;
	private boolean korrekt;
	private int rank;

	public Antwort (int id, String antwortText, boolean koorrekt, int rank){
		this.id = id;
		this.antwortText = antwortText;
		this.korrekt = korrekt;
		this.rank = rank;
	}

	public Antwort (){
		this.id = 0;
		this.antwortText = "";
		this.korrekt = false;
		this.rank = 0;
	}

	public int getId (){
		return id;
	}

	public String getAntwortText (){
		return antwortText;
	}
	public boolean isKorrekt (){
		return korrekt;
	}
	public int getRank (){
		return rank;
	}

	public void setId (int id){
		this.id = id;
	}
	public void setAntwortText (String antwortText){
		this.antwortText = antwortText;
	}
	public void setKorrekt (boolean korrekt){
		this.korrekt = korrekt;
	}
	public void setRank (int rank){
		this.rank = rank;
	}


}
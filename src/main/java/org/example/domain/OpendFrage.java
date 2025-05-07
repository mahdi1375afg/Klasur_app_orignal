package org.example.domain;


public class OpendFrage extends Frage {

	private String antwortBogen;

	public OpendFrage(int id, String frageText, BloomLevel bloomLevel, Modul module, int geschaetzeZeit, FragenArt fragenArt, String antwortBogen) {
		super(id, frageText, bloomLevel, module, geschaetzeZeit, fragenArt);
		this.antwortBogen = antwortBogen;
	}

}
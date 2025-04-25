package org.example.domain;


public class OpendFragen extends Fragen {

	private String antwortBogen;

	public OpendFragen(int id, String frageText, BloomLevel bloomLevel, Modul module, int geschaetzeZeit, FragenArt fragenArt, String antwortBogen) {
		super(id, frageText, bloomLevel, module, geschaetzeZeit, fragenArt);
		this.antwortBogen = antwortBogen;
	}

}
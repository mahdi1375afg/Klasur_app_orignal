package org.example.domain;

public class Fragen {

	private int id;
	private String frageText;
	private BloomLevel bloomLevel;
	private Modul module;
	private int geschaetzeZeit;
	private FragenArt fragenArt;

	public Fragen(int id, String frageText, BloomLevel bloomLevel, Modul module, int geschaetzeZeit, FragenArt fragenArt) {
		this.id = id;
		this.frageText = frageText;
		this.bloomLevel = bloomLevel;
		this.module = module;
		this.geschaetzeZeit = geschaetzeZeit;
		this.fragenArt = fragenArt;
	}
	public int getId() {
		return id;
	}
	public String getFrageText() {
		return frageText;
	}
	public BloomLevel getBloomLevel() {
		return bloomLevel;
	}
	public Modul getModule() {
		return module;
	}
	public int getGeschaetzeZeit() {
		return geschaetzeZeit;
	}
	public FragenArt getFragenArt() {
		return fragenArt;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setFrageText(String frageText) {
		this.frageText = frageText;
	}
	public void setBloomLevel(BloomLevel bloomLevel) {
		this.bloomLevel = bloomLevel;
	}
	public void setModule(Modul module) {
		this.module = module;
	}
	public void setGeschaetzeZeit(int geschaetzeZeit) {
		this.geschaetzeZeit = geschaetzeZeit;
	}
	public void setFragenArt(FragenArt fragenArt) {
		this.fragenArt = fragenArt;
	}
	public String toString() {
		return "Fragen [id=" + id + ", frageText=" + frageText + ", bloomLevel=" + bloomLevel + ", module=" + module
				+ ", geschaetzeZeit=" + geschaetzeZeit + ", fragenArt=" + fragenArt + "]";
	}

}
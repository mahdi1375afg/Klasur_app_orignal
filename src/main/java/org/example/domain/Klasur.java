package org.example.domain;

import java.time.LocalDate;

public class Klasur {

	private int id;
	private LocalDate erstellungsdatum;
	private String name;
	private int beschreibung;

	private Modul modul;

	private int dauer;

	public Klasur (int id, LocalDate erstellungsdatum, String name, int beschreibung, Modul modul, int dauer) {
		this.id = id;
		this.erstellungsdatum = erstellungsdatum;
		this.name = name;
		this.beschreibung = beschreibung;
		this.modul = modul;
		this.dauer = dauer;
	}

	public int getId () {
		return id;
	}
	public LocalDate getErstellungsdatum () {
		return erstellungsdatum;
	}
	public String getName () {
		return name;
	}
	public int getBeschreibung () {
		return beschreibung;
	}
	public Modul getModul () {
		return modul;
	}
	public int getDauer () {
		return dauer;
	}
	public void setId (int id) {
		this.id = id;
	}
	public void setErstellungsdatum (LocalDate erstellungsdatum) {
		this.erstellungsdatum = erstellungsdatum;
	}
	public void setName (String name) {
		this.name = name;
	}
	public void setBeschreibung (int beschreibung) {
		this.beschreibung = beschreibung;
	}
	public void setModul (Modul modul) {
		this.modul = modul;
	}
	public void setDauer (int dauer) {
		this.dauer = dauer;
	}


}
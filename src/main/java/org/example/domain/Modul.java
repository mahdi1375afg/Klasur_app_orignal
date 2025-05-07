package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Modul {

	private int id;
	private String name;
	private String Beschreibung;

	public Modul (int id, String name, String Beschreibung){
		this.id = id;
		this.name = name;
		this.Beschreibung = Beschreibung;
	}

    public static List<String> getAllModul() {
		List<String> modules = new ArrayList<>();
		modules.add("Betriebssysteme");
		modules.add("Software Engineering 2");
		return modules;
    }
}
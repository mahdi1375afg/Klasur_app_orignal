package org.example.domain;

import org.example.dao.dbConn;
import org.example.dao.dbConnModul;

import java.sql.SQLException;
import java.util.*;

public class Modul {

	public static List<Modul> modules = new ArrayList<>();

	private final int id;
	private  final String name;

	public Modul (int id, String name){
		this.id = id;
		this.name = name;
	}

    public static void getAllModul() throws SQLException {
		//lädt alle Module eines Nutzers aus der Datenbank und sortiert sie alphabetisch

		HashMap<Integer, String> result = dbConnModul.sqlGetAllModul();

		modules.clear();

		for (Map.Entry<Integer, String> eintrag : result.entrySet()) {
			modules.add(new Modul(eintrag.getKey(), eintrag.getValue()));
		}

		modules.sort((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
	}

	public static List<String> getAllNames(){
		//Gibt die Namen aller Module zurück
		List<String> modulNames = new ArrayList<>();
		for (Modul eintrag : modules) {
			modulNames.add(eintrag.getName());
		}

		modulNames.sort(String::compareToIgnoreCase);
		return modulNames;
	}

	public static Modul getModul(int questionID) throws SQLException {
		//Gibt ein das Modul zu einer Frage zurück

		List<Map<String, Object>> rows = dbConn.sqlSelect("aufgaben_modul", "aufgabe_id", questionID);

		if (!rows.isEmpty()) {
			int modulId = (Integer) rows.getFirst().get("modul_id");
			for (Modul eintrag : modules) {
				if (eintrag.getId() == modulId) {
					return eintrag;
				}
			}
		}

		return null;
	}

	public String getName(){
		return name;
	}

	public int getId() throws SQLException {return id;}

	public static int getId(String name) throws SQLException {
		//Gibt die ID eines Moduls falls vorhanden zurück
		for (Modul eintrag : modules) {
			if (eintrag.getName().equals(name)) {
				return eintrag.getId();
			}
		}
		return -1;
	}
}
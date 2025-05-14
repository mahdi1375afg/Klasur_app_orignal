package org.example.domain;

import org.example.dao.dbConnModul;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Modul {

	public static List<Modul> modules = new ArrayList<Modul>();

	private int id;
	private String name;

	public Modul (int id, String name){
		this.id = id;
		this.name = name;
	}

    public static List<Modul> getAllModul() throws SQLException {
		dbConnModul.sqlGetAllModul();
		HashMap<Integer, String> result = dbConnModul.sqlGetAllModul();
		for (Map.Entry<Integer, String> eintrag : result.entrySet()) {
			modules.add(new Modul(eintrag.getKey(), eintrag.getValue()));
		}
		return modules;
    }

	public static List<String> getAllNames() throws SQLException {
		List<String> modulNames = new ArrayList<>();
		for (Modul eintrag : modules) {
			modulNames.add(eintrag.getName());
		}
		return modulNames;
	}

	public String getName() throws SQLException {
		return name;
	}

	public int getId() throws SQLException {
		return id;
	}

	public static int getId(String name) throws SQLException {
		for (Modul eintrag : modules) {
			if (eintrag.getName().equals(name)) {
				return eintrag.getId();
			}
		}
		return -1;
	}
}
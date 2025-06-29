package org.example.domain;

import java.sql.Connection;

import org.example.Main;

import org.example.dao.*;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

public class UserAccount {
	public static User aktuellerBenutzer;

	public Boolean register(String name, String password) throws SQLException {
		dbConnUser connection = new dbConnUser();
		if(!connection.getNameUsed(name)) {
			connection.sqlInsert(name,password);
			return true;
		} else {
			return false;
		}
    }

	public int login(String name, String password) throws SQLException {
		dbConnUser connection = new dbConnUser();
		if(connection.getNameUsed(name)) {
			User user = connection.getUser(name);
			try {
				if(user.getPassword().equals(password)) {
					this.aktuellerBenutzer = user;
					Main.initUser(user.getId());
					return 0;
				} else {
					return 1;
				}
			} catch (NullPointerException e) {
				return 1;
			}
		} else {
			return 2;
		}
	}

	public String logout() {
		if(aktuellerBenutzer == null) {
			return "Fehler beim abmelden! Kein Nutzer angemeldet!";
		} else {
			aktuellerBenutzer = null;
			return "Erfolgreich";
		}
	}

	public int questionCreate(String questionName, String questionQuestion, int questionDuration, String questionType, int questionPoints, String questionTaxonomie, int id) throws SQLException {
		dbConnFrage connection = new dbConnFrage();
		return connection.getId(questionName,questionQuestion,questionDuration,questionType,questionPoints,questionTaxonomie,id);

	}

	public void questionCreateOpen(int fragenId, String key) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "offene_aufgabe", new String[]{"musterloesung", "aufgabe_id"}, new Object[]{key, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void questionCreateClosed(int fragenId, String questionType) {
		try (Connection conn = dbConn.getConn()) {
			PGobject enumObj = new PGobject();
			enumObj.setType("aufgaben_typ");
			enumObj.setValue(questionType);
			dbConn.sqlInsert(conn, "geschlossene_aufgabe", new String[]{"typ", "aufgabe_id"}, new Object[]{enumObj, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void answerCreateClosed(int fragenId, String key, Boolean value) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "antwortmoeglichkeit_geschlossen",
					new String[]{"antworttext", "ist_korrekt", "geschlossene_aufgabe_id"},
					new Object[]{key, value, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void answerCreateClosedMultipleParts(int fragenId, String key, String value) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "antwortMehrParts_geschlossen",
					new String[]{"antworttext", "antworttext2", "geschlossene_aufgabe_id"},
					new Object[]{key, value, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void answerCreateClosedRanking(int fragenId, String key, Integer value) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "antwortRanking_geschlossen",
					new String[]{"antworttext", "rank", "geschlossene_aufgabe_id"},
					new Object[]{key, value, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void createTaskToModul(int aufgabe_id, String modul_name) throws SQLException {
		int modul_id = Modul.getId(modul_name);

		if (modul_id == -1) {
			System.out.println("Modul nicht gefunden!");
			return;
		}

		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "aufgaben_modul",
					new String[]{"aufgabe_id", "modul_id"},
					new Object[]{aufgabe_id, modul_id});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void createModul(String modulTitleText) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "modul", new String[]{"benutzer_id","name"}, new Object[]{aktuellerBenutzer.getId(),modulTitleText});
			Modul.getAllModul();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
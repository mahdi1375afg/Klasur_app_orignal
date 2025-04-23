package org.example.domain;

import org.example.dao.DBconn;
import org.example.dao.dbConnFrage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Map;
import java.util.Scanner; // nur für ohne GUI testing

public class benutzerKonto {

	private int id;
	private String benutzerName;

	private String passwort;

	private LocalDateTime letzteAnmeldung;
	private boolean aktiv;
	/**
	 * 
	 * @param Nutzer
	 */
	public boolean anmelden(int Nutzer) {
		// TODO - implement benutzerKonto.anmelden
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Nutzer
	 */
	public boolean abmelden(int Nutzer) {
		// TODO - implement benutzerKonto.abmelden
		throw new UnsupportedOperationException();
	}


	public boolean adminModulErstellen(String modulName, String modulBeschreibung) {
		DBconn db = new DBconn();
		String[] values = {modulName, modulBeschreibung};
		String[] columns = {"name", "beschreibung"};

		try {
			db.sqlInsert("modul", columns, values); // Beispiel für SQL Insert
		} catch (SQLException e) {
			e.printStackTrace(); // Fehlerbehandlung für Insert
		}

        return true;
    }

	/**
	 *
	 * @param Fragen
	 */
	public boolean fragenErstellen(int Fragen) throws SQLException {
		dbConnFrage connection = new dbConnFrage();
		String name;
		String aufgabentext;
		int zeit;
		String bloomLevel;
		String type;
		String modul;
		int punkte;

		Scanner scanner = new Scanner(System.in);
		System.out.println("fragenErstellen");
		System.out.println("Name:");
		name = scanner.nextLine();
		System.out.println("aufgabentext:");
		aufgabentext = scanner.nextLine();
		System.out.println("zeit:");
		zeit = Integer.parseInt(scanner.nextLine());
		System.out.println("bloomLevel:");
		bloomLevel = scanner.nextLine();
		System.out.println("type:");
		type = scanner.nextLine();
		System.out.println("modul:");
		modul = scanner.nextLine();
		System.out.println("punkte:");
		punkte = Integer.parseInt(scanner.nextLine());

		int modulid = modul.length();

		connection.sqlInsert(name,aufgabentext,zeit,bloomLevel,type,modulid,punkte);
		//Get alle Informationen aus dem GUI
		//Modul Id herausfinden

		// In der Datenbank Antwort Id hinzufügen?

		return true; //Machen obs wirklich funktioniert hat
	}

	/**
	 * 
	 * @param Fragen
	 */
	public boolean fragenBearbeiten(int Fragen) {
		// TODO - implement benutzerKonto.fragenBearbeiten
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Fragen
	 */
	public boolean fragenLoeschen(int Fragen) {
		// TODO - implement benutzerKonto.fragenLoeschen
		throw new UnsupportedOperationException();
	}

	/**
	 * @param Fragen
	 */
	public List<Map<String, Object>> fragenfiltern(int Fragen) throws SQLException {
		dbConnFrage connection = new dbConnFrage();
		List<Map<String, Object>> result = connection.sqlSelect("Egal","Egal");
		return result;
	}

	public void antwortErstellen() {
		// TODO - implement benutzerKonto.antwortErstellen
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Antwort
	 */
	public void antwortBearbeiten(int Antwort) {
		// TODO - implement benutzerKonto.antwortBearbeiten
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Antwort
	 */
	public void antwortloeaschen(int Antwort) {
		// TODO - implement benutzerKonto.antwortloeaschen
		throw new UnsupportedOperationException();
	}

	public void klasurErstellen() {
		// TODO - implement benutzerKonto.klasurErstellen
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Klasur
	 */
	public void klasurBearbeiten(int Klasur) {
		// TODO - implement benutzerKonto.klasurBearbeiten
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param Klasur
	 */
	public void KlarsurLoeschen(int Klasur) {
		// TODO - implement benutzerKonto.KlarsurLoeschen
		throw new UnsupportedOperationException();
	}

}
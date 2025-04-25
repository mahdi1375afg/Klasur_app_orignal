package org.example.domain;

import org.example.dao.DBconn;
import org.example.dao.dbConnAntwort;
import org.example.dao.dbConnFrage;
import org.example.domain.Klausur;
import org.example.dao.dbConnModul;

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

	public void antwortErstellen(Fragen frage) {
		Antwort antwort = new Antwort();
		Scanner scanner = new Scanner(System.in);
		//name eingeben
		System.out.println("Bitte geben Sie den Antwort ein:");
		String name = scanner.nextLine();
		antwort.setAntwortText(name);

		System.out.println("Bitte geben Sie Richtig oder Falsch ein:");
		if (scanner.nextLine().equals("Richtig")) {
			antwort.setKorrekt(true);
		} else {
			antwort.setKorrekt(false);
		}
		// nur beim fragen die von art zuordnung sind einsetzen
		System.out.println("Bitte geben Sie den Rank der Antwort ein:");
		int rank = Integer.parseInt(scanner.nextLine());
		antwort.setRank(rank);

		if(frage.getFragenArt().equals(FragenArt.OffeneFrage)){

			try{
				DBconn db = new DBconn();
				db.sqlInsert("antwort", new String[]{"Antwort","aufgabeId"}, new Object[]{antwort.getAntwortText(),frage.getId()});
			}catch (SQLException e){
				e.printStackTrace();
			}
		}else if(frage.getFragenArt().equals(FragenArt.GeschlosseneFrage)){
			try{
				DBconn db = new DBconn();
				dbConnAntwort dbAntwort = new dbConnAntwort();
				db.sqlInsert("antwort", new String[]{"Antwort","aufgabeId"}, new Object[]{antwort.getAntwortText(),frage.getId()});
				System.out.println("erfogreich im im antwort table eingefügt");
				List<Map<String, Object>> id = dbAntwort.sqlSelectAntwortId();
				System.out.println("select id erfolgreich");
				antwort.setId((int) id.get(id.size()-1).get("id"));
				System.out.println("antwort id ist: "+antwort.getId());
				String korrekt;
				if(antwort.isKorrekt()){korrekt = "true";}
				else {korrekt = "false";}
				dbAntwort.sqlInsertGeschlosseneAnt( antwort.getAntwortText(),korrekt,antwort.getId());
				System.out.println("erfogreich im im geschlosseneantwort table eingefügt");
			}catch (SQLException e){
				e.printStackTrace();
			}
		}

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
	// noch nicht fertig klasur erstellen
	public void klasurErstellen() {
		Klausur k = new Klausur();
		Scanner scanner = new Scanner(System.in);
		//name eingeben
		System.out.println("Bitte geben Sie den Namen der Klausur ein:");
		String name = scanner.nextLine();
		k.setName(name);

		System.out.println("Bitte geben Sie den Dauer der Klausur ein:");
		int dauer = Integer.parseInt(scanner.nextLine());
		k.setDauer(dauer);

		System.out.println("Bitte geben Sie die Beschreibung der Klausur ein:");
		String beschreibung = scanner.nextLine();
		k.setBeschreibung(beschreibung);

		System.out.println("Bitte geben Sie das Modul ID der Klausur ein:");
		int modul = Integer.parseInt(scanner.nextLine());

		dbConnModul db = new dbConnModul();
		try {
			// Get the Modul from the database
			List<Map<String, Object>> result = db.sqlSelectModul();
			if (result.isEmpty()) {
				System.out.println("Das Modul mit der ID " + modul + " existiert nicht.");
				return;
			}


			// Assign the Modul to the Klausur
			k.setModul(new Modul(modul, result.get(0).get("name").toString(), result.get(0).get("beschreibung").toString()));

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Fehler beim Abrufen des Moduls aus der Datenbank.");
		}



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
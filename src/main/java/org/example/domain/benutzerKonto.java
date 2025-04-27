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

		if (frage.getFragenArt().equals(FragenArt.OffeneFrage)) {
			//name eingeben
			System.out.println("Bitte geben Sie den Antwort ein:");
			String name = scanner.nextLine();
			antwort.setAntwortText(name);

			try {

				DBconn.sqlInsert("antwort", new String[]{"Antwort", "aufgabeId"}, new Object[]{antwort.getAntwortText(), frage.getId()});
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
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

			try {

				DBconn.sqlInsert("antwort", new String[]{"Antwort", "aufgabeId"}, new Object[]{antwort.getAntwortText(), frage.getId()});
				System.out.println("erfogreich im im antwort table eingefügt");
				List<Map<String, Object>> id = dbConnAntwort.sqlSelectAntwortId();
				System.out.println("select id erfolgreich");
				antwort.setId((int) id.get(id.size() - 1).get("id"));
				System.out.println("antwort id ist: " + antwort.getId());
				String korrekt;
				if (antwort.isKorrekt()) {
					korrekt = "true";
				} else {
					korrekt = "false";
				}
				dbConnAntwort.sqlInsertGeschlosseneAnt(antwort.getAntwortText(), korrekt, antwort.getId());
				System.out.println("erfogreich im im geschlosseneantwort table eingefügt");
			} catch (SQLException e) {
				e.printStackTrace();
			}


		}
	}

	//noch nicht fertig
	public void antwortBearbeiten(Antwort ant) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Bitte geben Sie den Id der Antwort, der sie bearbeiten möchten:");
		int id = Integer.parseInt(scanner.nextLine());

		if(ant.getAntwortType().equals(antwortType.offeneAntwort)){
			System.out.println("Bitte geben Sie den neuen AntwortText ein:");
			String newAntwortText = scanner.nextLine();

			try {
				DBconn.sqlUpdate("antwort", new String[]{"antwort"}, new Object[]{newAntwortText}, "id", id);
				System.out.println("Antwort erfolgreich bearbeitet.");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}else {

			System.out.println("Welche Spalten möchten Sie bearbeiten?");
			System.out.println("1. AntwortText");
			System.out.println("2. Korrekt");
			System.out.println("3. Rank");
			int choice = Integer.parseInt(scanner.nextLine());

			switch (choice) {
				case 1:
					System.out.println("Bitte geben Sie den neuen AntwortText ein:");
					String newAntwortText1 = scanner.nextLine();
					ant.setAntwortText(newAntwortText1);

					try {
						DBconn.sqlUpdate("antwort", new String[]{"antwort"}, new Object[]{newAntwortText1}, "id", id);
						System.out.println("antwort table erfolgreich bearbeitet.");

						List<Map<String, Object>> lastid = dbConnAntwort.sqlSelectAntwortId();
						System.out.println("select id erfolgreich");
						ant.setId((int) lastid.get(lastid.size() - 1).get("id"));
						System.out.println("antwort id ist: " + ant.getId());
						DBconn.sqlUpdate("geschlosseneantwort", new String[]{"antwort"}, new Object[]{newAntwortText1}, "antwortid", ant.getId());

					} catch (SQLException e) {
						e.printStackTrace();
					}

					break;
				case 2:
					System.out.println("Bitte geben Sie den neuen Korrekt ein (true/false):");
					boolean newKorrekt = Boolean.parseBoolean(scanner.nextLine());
					ant.setKorrekt(newKorrekt);
					try {
						String korrekt;
						if (ant.isKorrekt()) {
							korrekt = "true";
						} else {
							korrekt = "false";
						}
						DBconn.sqlUpdate("geschlosseneantwort", new String[]{"isKorrekt"}, new Object[]{korrekt}, "id", id);
						System.out.println("antwort table erfolgreich bearbeitet.");
					} catch (SQLException e) {
						e.printStackTrace();
					}
						break;
				case 3:
					System.out.println("Bitte geben Sie den neuen Rank ein:");
					int newRank = Integer.parseInt(scanner.nextLine());
					ant.setRank(newRank);
					try {
						DBconn.sqlUpdate("geschlosseneantwort", new String[]{"rank"}, new Object[]{newRank}, "id", id);
						System.out.println("rank im geschlosseneantwort table erfolgreich bearbeitet.");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				default:
					System.out.println("Ungültige Auswahl.");
			}

		}
	}

	public void antwortloeaschen(Antwort ant) {

		if (ant.getAntwortType().equals(antwortType.geschlosseneAntwort)) {
			try {
				DBconn.sqlDelete("geschlosseneantwort", "antwortid", ant.getId());
				System.out.println("geschlosseneantwort erfolgreich gelöscht.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {

			try {
				DBconn.sqlDelete("antwort", "id", ant.getId());
				System.out.println("table antwort erfolgreich gelöscht.");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	// noch nicht fertig klasur erstellen
	public void klasurErstellen(){
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
		
		try {
			// Get the Modul from the database
			List<Map<String, Object>> result = dbConnModul.sqlSelectModul();
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
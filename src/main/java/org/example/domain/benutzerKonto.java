package org.example.domain;

import java.time.Duration;
import org.example.dao.*;
import org.postgresql.util.PGobject;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Map;
import java.util.Scanner; // nur für ohne GUI testing

public class benutzerKonto {
	public static Nutzer aktuellerBenutzer;

	private LocalDateTime lastLogin;
	private boolean aktiv;

	public benutzerKonto() {
		// WIP
	}

	public Boolean register(String name, String password) throws SQLException {
		dbConnUser connection = new dbConnUser();
		if(!connection.getNameUsed(name)) {
			System.out.println(connection.getNameUsed(name));
			connection.sqlInsert(name,password);
			return true;
		} else {
			return false;
		}
    }

	public String anmelden(String name, String password) throws SQLException {
		dbConnUser connection = new dbConnUser();
		if(connection.getNameUsed(name)) {
			Nutzer nutzer = connection.getNutzer(name);
			try {
				if(nutzer.getPassword().equals(password)) {
					this.aktuellerBenutzer = nutzer;
					return "Erfolgreich";
				} else {
					System.out.println("Falsches Passwort");
					return "Fehler beim anmelden";
				}
			} catch (NullPointerException e) {
				return "Fehler beim anmelden";
			}
		} else {
			return "Fehler beim anmelden: Account nicht vorhanden";
		}
	}

	public String abmelden() {
		if(aktuellerBenutzer == null) {
			return "Fehler beim abmelden! Kein Nutzer angemeldet!";
		} else {
			aktuellerBenutzer = null;
			return "Erfolgreich";
		}
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

	public int fragenErstellen(String questionName, String questionQuestion, int questionDuration, String questionType, int questionPoints, String questionTaxonomie, int id) throws SQLException {
		dbConnFrage connection = new dbConnFrage();

		String name = questionName;
		String aufgabentext = questionQuestion;
		int zeit = questionDuration;
		String taxonomie = questionTaxonomie;
		String format = questionType;
		int benutzer_id = id;
		int punkte = questionPoints;
		connection.sqlInsert(name,aufgabentext,zeit,format,punkte,taxonomie,benutzer_id);
		return connection.getId(name,aufgabentext,zeit,format,punkte,taxonomie,benutzer_id);

	}

	public void fragenBearbeiten(int id) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welche Spalten möchten Sie bearbeiten?");
		System.out.println("1. Name");
		System.out.println("2. Aufgabe Text");
		System.out.println("3. Dauer");
		System.out.println("4. Format");
		System.out.println("5. Punkte");
		System.out.println("6. Taxonomie");
		System.out.println("7. Art der Geschlossenen Frage");


		int choice = Integer.parseInt(scanner.nextLine());

		switch (choice) {
			case 1:
				System.out.println("Bitte geben Sie den neuen Aufgaben Name ein:");
				String newAntwortText1 = scanner.nextLine();
				try {
					DBconn.sqlUpdate("aufgabe", new String[]{"name"}, new Object[]{newAntwortText1}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}

				break;
			case 2:
				System.out.println("Bitte geben Sie den neuen Aufgaben Text ein:");
				String aufgabeText = scanner.nextLine();
				try {
					DBconn.sqlUpdate("aufgabe", new String[]{"aufgabentext"}, new Object[]{aufgabeText}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				System.out.println("Bitte geben Sie den neues Dauer ein:");
				String dauer = scanner.nextLine();

				try {
					Duration dauer1 = Duration.parse(dauer);
					DBconn.sqlUpdate("aufgabe", new String[]{"zeit"}, new Object[]{dauer1.toString()}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 4:
				System.out.println("Bitte geben Sie den neuen Format (Offen oder Geschlossen) ein:");
				String format = scanner.nextLine();
				try {
					DBconn.sqlUpdate("aufgabe", new String[]{"format"}, new Object[]{format}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 5:
				System.out.println("Bitte geben Sie den neue Taxonomie ein:");
				String taxonomie= scanner.nextLine();
				try {
					DBconn.sqlUpdate("aufgabe", new String[]{"taxonomie"}, new Object[]{taxonomie}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 6:
				System.out.println("Bitte geben Sie den neuen Punkte ein:");
				int punkt = Integer.parseInt(scanner.nextLine());
				try {
					DBconn.sqlUpdate("aufgabe", new String[]{"punkte"}, new Object[]{punkt}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 7:
				System.out.println("Bitte geben Sie den neuen Art der Geschlossenen Frage ein:");
				String art = scanner.nextLine();
				try {
					DBconn.sqlUpdate("geschlossene_aufgabe", new String[]{"typ"}, new Object[]{art}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			default:
				System.out.println("Ungültige Auswahl.");
		}

	}


	public boolean fragenLoeschen(int id) {


		try {
			DBconn.sqlDelete("aufgaben_modul", "aufgabe_id", id);
			System.out.println("Aufgabe von tabelle aufgaben_modul erfolgreich gelöscht.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			DBconn.sqlDelete("antwortmoeglichkeit_geschlossen", "geschlossene_aufgabe_id", id);
			System.out.println("Aufgabe von tabelle antwortmoeglichkeit_geschlossen erfolgreich gelöscht.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			DBconn.sqlDelete("geschlossene_aufgabe", "aufgabe_id", id);
			System.out.println("Aufgabe von tabelle geschlossene_aufgabe erfolgreich gelöscht.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			DBconn.sqlDelete("offene_aufgabe", "aufgabe_id", id);
			System.out.println("Aufgabe von tabelle offene_aufgabe erfolgreich gelöscht.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			DBconn.sqlDelete("aufgabe", "id", id);
			System.out.println("Aufgabe von tabelle aufgabe erfolgreich gelöscht.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}


	/**
	 * @param Fragen
	 */
	public List<Map<String, Object>> fragenfiltern(int Fragen) throws SQLException {
		dbConnFrage connection = new dbConnFrage();
		List<Map<String, Object>> result = connection.sqlSelect("Egal","Egal");
		return result;
	}

	public void antwortErstellenOffen(int fragenId, String key) {
		try {
			DBconn.sqlInsert("offene_aufgabe", new String[]{"musterloesung","aufgabe_id"}, new Object[]{key,fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void frageErstellenGeschlossen(int fragenId, String questionType) {
		try {
			PGobject enumObj = new PGobject();
			enumObj.setType("aufgaben_typ");
			enumObj.setValue(questionType);
			DBconn.sqlInsert("geschlossene_aufgabe", new String[]{"typ","aufgabe_id"}, new Object[]{enumObj, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void antwortErstellenGeschlossen(int fragenId, String key, Boolean value) {
			try {
				DBconn.sqlInsert("antwortmoeglichkeit_geschlossen", new String[]{"antworttext","ist_korrekt","geschlossene_aufgabe_id"}, new Object[]{key,value,fragenId});
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public void antwortErstellenGeschlossenMultipleParts(int fragenId, String key, String value) {
		try {
			DBconn.sqlInsert("antwortMehrParts_geschlossen", new String[]{"antworttext","antworttext2","geschlossene_aufgabe_id"}, new Object[]{key,value,fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void antwortErstellenGeschlossenRanking(int fragenId, String key, Integer value) {
		try {
			DBconn.sqlInsert("antwortRanking_geschlossen", new String[]{"antworttext","rank","geschlossene_aufgabe_id"}, new Object[]{key,value,fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void antwortErstellen(Frage frage) {
		Antwort antwort = new Antwort();
		Scanner scanner = new Scanner(System.in);

		if (frage.getFragenArt().equals(FragenArt.OffeneFrage)) {
			//name eingeben
			System.out.println("Bitte geben Sie den Antwort ein:");
			String name = scanner.nextLine();
			antwort.setAntwortText(name);

			try {

				DBconn.sqlInsert("antwortmoeglichkeit_geschlossen", new String[]{"antworttext","ist_korrekt","geschlossene_aufgabe_id"}, new Object[]{antwort.getAntwortText(),"true",1});
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

				DBconn.sqlInsert("antwortmoeglichkeit_geschlossen", new String[]{"antworttext","ist_korrekt","geschlossene_aufgabe_id"}, new Object[]{antwort.getAntwortText(),"true",1});
				System.out.println("erfogreich im im antwort table eingefügt");
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

		if(ant.getAntwortType().equals(AntwortType.offeneAntwort)){
			System.out.println("Bitte geben Sie den neuen AntwortText ein:");
			String newAntwortText = scanner.nextLine();

			try {
				DBconn.sqlUpdate("antwortmoeglichkeit_geschlossen", new String[]{"antworttext"}, new Object[]{newAntwortText}, "id", id);
				System.out.println("Antwort erfolgreich bearbeitet.");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}else {

			System.out.println("Welche Spalten möchten Sie bearbeiten?");
			System.out.println("1. AntwortText");
			System.out.println("2. Korrekt");
			int choice = Integer.parseInt(scanner.nextLine());

			switch (choice) {
				case 1:
					System.out.println("Bitte geben Sie den neuen AntwortText ein:");
					String newAntwortText1 = scanner.nextLine();
					ant.setAntwortText(newAntwortText1);

					try {
						DBconn.sqlUpdate("antwortmoeglichkeit_geschlossen", new String[]{"antworttext"}, new Object[]{newAntwortText1}, "id", id);
						System.out.println("antwort table erfolgreich bearbeitet.");


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
						DBconn.sqlUpdate("antwortmoeglichkeit_geschlossen", new String[]{"ist_korrekt"}, new Object[]{korrekt}, "id", id);
						System.out.println("antwort table erfolgreich bearbeitet.");
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

		if (ant.getAntwortType().equals(AntwortType.geschlosseneAntwort)) {
			try {
				DBconn.sqlDelete("antwortmoeglichkeit_geschlossen", "id", ant.getId());
				System.out.println("geschlosseneantwort erfolgreich gelöscht.");
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
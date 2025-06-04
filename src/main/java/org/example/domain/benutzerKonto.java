package org.example.domain;

import java.sql.Connection;

import org.example.Main;

import org.example.dao.*;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

import java.time.LocalDate;
import java.util.Scanner; // nur für ohne GUI testing

public class benutzerKonto {
	public static Nutzer aktuellerBenutzer;

	public Boolean register(String name, String password) throws SQLException {
		dbConnUser connection = new dbConnUser();
		if(!connection.getNameUsed(name)) {
			connection.sqlInsert(name,password);
			return true;
		} else {
			return false;
		}
    }

	public int anmelden(String name, String password) throws SQLException {
		dbConnUser connection = new dbConnUser();
		if(connection.getNameUsed(name)) {
			Nutzer nutzer = connection.getNutzer(name);
			try {
				if(nutzer.getPassword().equals(password)) {
					this.aktuellerBenutzer = nutzer;
					Main.initUser(nutzer.getId());
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

	public String abmelden() {
		if(aktuellerBenutzer == null) {
			return "Fehler beim abmelden! Kein Nutzer angemeldet!";
		} else {
			aktuellerBenutzer = null;
			return "Erfolgreich";
		}
	}


	public int fragenErstellen(String questionName, String questionQuestion, int questionDuration, String questionType, int questionPoints, String questionTaxonomie, int id) throws SQLException {
		dbConnFrage connection = new dbConnFrage();
		return connection.getId(questionName,questionQuestion,questionDuration,questionType,questionPoints,questionTaxonomie,id);

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
					dbConn.sqlUpdate("aufgabe", new String[]{"name"}, new Object[]{newAntwortText1}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}

				break;
			case 2:
				System.out.println("Bitte geben Sie den neuen Aufgaben Text ein:");
				String aufgabeText = scanner.nextLine();
				try {
					dbConn.sqlUpdate("aufgabe", new String[]{"aufgabentext"}, new Object[]{aufgabeText}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				System.out.println("Bitte geben Sie den neues Dauer ein:");
				int dauer = Integer.parseInt(scanner.nextLine());

				try {
					dbConn.sqlUpdate("aufgabe", new String[]{"zeit"}, new Object[]{dauer}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 4:
				System.out.println("Bitte geben Sie den neuen Format (Offen oder Geschlossen) ein:");
				String format = scanner.nextLine();
				try {
					dbConn.sqlUpdate("aufgabe", new String[]{"format"}, new Object[]{format}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 5:
				System.out.println("Bitte geben Sie den neue Taxonomie ein:");
				String taxonomie= scanner.nextLine();
				try {
					dbConn.sqlUpdate("aufgabe", new String[]{"taxonomie"}, new Object[]{taxonomie}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 6:
				System.out.println("Bitte geben Sie den neuen Punkte ein:");
				int punkt = Integer.parseInt(scanner.nextLine());
				try {
					dbConn.sqlUpdate("aufgabe", new String[]{"punkte"}, new Object[]{punkt}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case 7:
				System.out.println("Bitte geben Sie den neuen Art der Geschlossenen Frage ein:");
				String art = scanner.nextLine();
				try {
					dbConn.sqlUpdate("geschlossene_aufgabe", new String[]{"typ"}, new Object[]{art}, "id", id);
					System.out.println("Aufgabe tabelle erfolgreich bearbeitet.");

				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			default:
				System.out.println("Ungültige Auswahl.");
		}

	}



	public void antwortErstellenOffen(int fragenId, String key) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "offene_aufgabe",
					new String[]{"musterloesung", "aufgabe_id"},
					new Object[]{key, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void frageErstellenGeschlossen(int fragenId, String questionType) {
		try (Connection conn = dbConn.getConn()) {
			PGobject enumObj = new PGobject();
			enumObj.setType("aufgaben_typ");
			enumObj.setValue(questionType);
			dbConn.sqlInsert(conn, "geschlossene_aufgabe", new String[]{"typ", "aufgabe_id"}, new Object[]{enumObj, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void antwortErstellenGeschlossen(int fragenId, String key, Boolean value) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "antwortmoeglichkeit_geschlossen",
					new String[]{"antworttext", "ist_korrekt", "geschlossene_aufgabe_id"},
					new Object[]{key, value, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void antwortErstellenGeschlossenMultipleParts(int fragenId, String key, String value) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "antwortMehrParts_geschlossen",
					new String[]{"antworttext", "antworttext2", "geschlossene_aufgabe_id"},
					new Object[]{key, value, fragenId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void antwortErstellenGeschlossenRanking(int fragenId, String key, Integer value) {
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


	public int ExamErstellen(String name, LocalDate date, int totalPoints, int id) throws SQLException {
		dbConnExam connection = new dbConnExam();
		return connection.getId(name, date, totalPoints, id);
	}

	public void createTaskToExam(int id, int examId) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "aufgaben_klausur",
					new String[]{"aufgabe_id", "klausur_id"},
					new Object[]{id, examId});
		} catch (SQLException e) {
			e.printStackTrace();
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
				dbConn.sqlUpdate("antwortmoeglichkeit_geschlossen", new String[]{"antworttext"}, new Object[]{newAntwortText}, "id", id);
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
						dbConn.sqlUpdate("antwortmoeglichkeit_geschlossen", new String[]{"antworttext"}, new Object[]{newAntwortText1}, "id", id);
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
						dbConn.sqlUpdate("antwortmoeglichkeit_geschlossen", new String[]{"ist_korrekt"}, new Object[]{korrekt}, "id", id);
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

	// noch nicht fertig Klausur erstellen
	/*
	public void klausurErstellen(){
		Exam k = new Exam();
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
			k.setModul(new Modul(modul, result.get(0).get("name").toString()));

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Fehler beim Abrufen des Moduls aus der Datenbank.");
		}


		// TODO - implement benutzerKonto.klasurErstellen
		throw new UnsupportedOperationException();
	}
*/

	public void klasurBearbeiten(int Klasur) {
		// TODO - implement benutzerKonto.klasurBearbeiten
		throw new UnsupportedOperationException();
	}


	public void KlarsurLoeschen(int Klasur) {
		// TODO - implement benutzerKonto.KlarsurLoeschen
		throw new UnsupportedOperationException();
	}

	public void createModul(String modulTitleText) {
		try (Connection conn = dbConn.getConn()) {
			dbConn.sqlInsert(conn, "modul", new String[]{"name"}, new Object[]{modulTitleText});
			Modul.getAllModul();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
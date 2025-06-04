package org.example.domain;

import org.example.dao.dbConn;
import org.example.dao.dbConnExam;
import org.example.dao.dbConnFrage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Exam {

	public static List<Exam> Exams = new ArrayList<Exam>();

	private int id;
	private LocalDate erstellungsdatum;
	private String name;
	private String beschreibung;

	public List<Task> tasks;

	private Modul modul;
	private int points;

	public Exam(int id, LocalDate erstellungsdatum, String name, List<Task> tasks, int points) {
		this.id = id;
		this.erstellungsdatum = erstellungsdatum;
		this.name = name;
		this.tasks = tasks;
		this.points = points;
	}

	public static void getAllExams(int id) throws SQLException {
		Exams.clear();
		List<Task> tasks = new ArrayList<>();
		List<Map<String, Object>> result = dbConnExam.sqlSelect(benutzerKonto.aktuellerBenutzer.getId());

		for (Map<String, Object> row : result) {
			BigDecimal punkteDecimal = (BigDecimal) row.get("gesamtpunkte");
			int punkte = punkteDecimal.intValue();
			List<Map<String, Object>> rows = dbConn.sqlSelect("aufgaben_klausur", "klausur_id", row.get("id"));
			for (Map<String, Object> row2 : rows) {
				for(Task task : Task.tasks) {
					if(task.getQuestion().getId() == (int)row2.get("aufgabe_id")) {
						tasks.add(task);
					}
				}
			}
			LocalDate datum = ((java.sql.Date) row.get("datum")).toLocalDate();
			Exam exam = new Exam((int)row.get("id"),datum,(String)row.get("name"),tasks,punkte);
			Exams.add(exam);
		}
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
	public String getBeschreibung () {
		return beschreibung;
	}
	public Modul getModul () {
		return modul;
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
	public void setBeschreibung (String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public void setModul (Modul modul) {
		this.modul = modul;
	}

	public List<Task> getTasks() {
		return tasks;
	}
}
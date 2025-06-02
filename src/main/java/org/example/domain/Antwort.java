package org.example.domain;

import org.example.dao.dbConn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Antwort {

	private int id;
	private String antwortText;
	private String antwortText2;
	private int antwortRanking;
	private QuestionType typ;
	private boolean korrekt;

	private AntwortType antwortType;

	public Antwort (int id, QuestionType typ, String antwortText, boolean korrekt, AntwortType antwortType){
		this.id = id;
		this.typ = typ;
		this.antwortText = antwortText;
		this.korrekt = korrekt;
		this.antwortType = antwortType;
	}

	public Antwort (int id, QuestionType typ, String antwortText, String antwortText2, AntwortType antwortType) {
		this.id = id;
		this.typ = typ;
		this.antwortText = antwortText;
		this.antwortText2 = antwortText2;
		this.antwortType = antwortType;
	}

	public Antwort (int id, QuestionType typ, String antwortText, int antwortRanking, AntwortType antwortType) {
		this.id = id;
		this.typ = typ;
		this.antwortText = antwortText;
		this.antwortRanking = antwortRanking;
		this.antwortType = antwortType;
	}

	public static List<Antwort> getAntwort(int id, AntwortType format) throws SQLException {
		List<Antwort> result = new ArrayList<>();

		if (format == AntwortType.offeneAntwort) {
			List<Map<String, Object>> rows = dbConn.sqlSelect("offene_aufgabe", "aufgabe_id", id);
			for (Map<String, Object> row : rows) {
				result.add(new Antwort(
						(Integer) row.get("aufgabe_id"),
						QuestionType.offen,
						(String) row.get("musterloesung"),
						true,
						format
				));
			}

		} else if (format == AntwortType.geschlosseneAntwort) {
			List<Map<String, Object>> rows = dbConn.sqlSelect("geschlossene_aufgabe", "aufgabe_id", id);
			if (!rows.isEmpty()) {
				Map<String, Object> firstRow = rows.get(0);
				QuestionType closeType = QuestionType.fromName((String) firstRow.get("typ"));

				if (closeType == QuestionType.wahrOderFalsch ||
						closeType == QuestionType.multipleChoiceFragen ||
						closeType == QuestionType.singleChoiceFragen) {

					List<Map<String, Object>> antwortRows = dbConn.sqlSelect("antwortmoeglichkeit_geschlossen", "geschlossene_aufgabe_id", id);
					for (Map<String, Object> row : antwortRows) {
						result.add(new Antwort(
								(Integer) row.get("id"),
								closeType,
								(String) row.get("antworttext"),
								(Boolean) row.get("ist_korrekt"),
								format
						));
					}

				} else if (closeType == QuestionType.leerstellen || closeType == QuestionType.zuordnung) {

					List<Map<String, Object>> antwortRows = dbConn.sqlSelect("antwortMehrParts_geschlossen", "geschlossene_aufgabe_id", id);
					for (Map<String, Object> row : antwortRows) {
						result.add(new Antwort(
								(Integer) row.get("id"),
								closeType,
								(String) row.get("antworttext"),
								(String) row.get("antworttext2"),
								format
						));
					}

				} else if (closeType == QuestionType.ranking) {

					List<Map<String, Object>> antwortRows = dbConn.sqlSelect("antwortRanking_geschlossen", "geschlossene_aufgabe_id", id);
					for (Map<String, Object> row : antwortRows) {
						result.add(new Antwort(
								(Integer) row.get("id"),
								closeType,
								(String) row.get("antworttext"),
								(Integer) row.get("rank"),
								format
						));
					}
				}
			}
		}

		return result;
	}


	public QuestionType getTyp() {
		return typ;
	}

	public AntwortType getAntwortType() {
		return antwortType;
	}


	public void setAntwortType(AntwortType antwortType) {
		this.antwortType = antwortType;
	}

	public int getId (){
		return id;
	}

	public String getAntwortText (){
		return antwortText;
	}

	public String getAntwortText2(){
		return antwortText2;
	}
	public boolean isKorrekt (){
		return korrekt;
	}

	public int getAntwortRanking (){
		return antwortRanking;
	}
	public void setId (int id){
		this.id = id;
	}
	public void setAntwortText (String antwortText){
		this.antwortText = antwortText;
	}
	public void setKorrekt (boolean korrekt){
		this.korrekt = korrekt;
	}



}
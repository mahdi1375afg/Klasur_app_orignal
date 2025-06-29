package org.example.domain;

import org.example.dao.dbConnFrage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Question {

	private int id;
	private String name;
	private String questionText;
	private int time;
	private AnswerType format;
	private int points;
	private BloomLevel taxonomie;

	public Question(int id, String name, String questionText, int time, AnswerType format, int points, BloomLevel taxonomie) {
		this.id = id;
		this.name = name;
		this.questionText = questionText;
		this.time = time;
		this.format = format;
		this.points = points;
		this.taxonomie = taxonomie;
	}

	public static List<Question> getAllFragen(int userid) throws SQLException {
		List<Question> fragen = new ArrayList<Question>();

		List<Map<String, Object>> result = dbConnFrage.sqlSelect(userid);

		for (Map<String, Object> row : result) {
			BigDecimal punkteDecimal = (BigDecimal) row.get("punkte");
			int punkte = punkteDecimal.intValue();
			Question question = new Question((int)row.get("id"),(String)row.get("name"),(String)row.get("aufgabentext"), (int) row.get("zeit"),(AnswerType) row.get("format"),punkte,(BloomLevel) row.get("taxonomie"));
			fragen.add(question);
		}

		return fragen;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {this.id = id;}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public String getQuestionText() {return questionText;}
	public void setQuestionText(String questionText) {this.questionText = questionText;}

	public int getTime() {return time;}
	public void setTime(int time) {this.time = time;}

	public AnswerType getFormat() {return format;}
	public void setFormat(AnswerType format) {this.format = format;}

	public int getPoints() {return points;}
	public void setPoints(int points) {this.points = points;}

	public BloomLevel getTaxonomie() {return taxonomie;}
	public void setTaxonomie() {this.taxonomie = taxonomie;}
}
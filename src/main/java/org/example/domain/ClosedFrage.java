package org.example.domain;

public class ClosedFrage extends Frage {

	private QuestionType closedFragentype;

	public ClosedFrage(int id, String name, String questionText, int time, AntwortType format, int points, BloomLevel taxonomie, QuestionType closedFragentype) {
		super(id, name, questionText, time, format, points, taxonomie);
		this.closedFragentype = closedFragentype;
	}

	public QuestionType getClosedFragentype() {
		return closedFragentype;
	}

}
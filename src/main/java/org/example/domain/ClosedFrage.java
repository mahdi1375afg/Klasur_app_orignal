package org.example.domain;

public class ClosedFrage extends Frage {

	private CloseType closedFragentype;

	public ClosedFrage(int id, String name, String questionText, int time, AntwortType format, int points, BloomLevel taxonomie, CloseType closedFragentype) {
		super(id, name, questionText, time, format, points, taxonomie);
		this.closedFragentype = closedFragentype;
	}

	public CloseType getClosedFragentype() {
		return closedFragentype;
	}

}
package org.example.domain;


public class OpendFrage extends Question {

	private String antwortBogen;

	public OpendFrage(int id, String name, String questionText, int time, AnswerType format, int points, BloomLevel taxonomie, String antwortBogen) {
		super(id, name, questionText, time, format, points, taxonomie);
		this.antwortBogen = antwortBogen;
	}

}
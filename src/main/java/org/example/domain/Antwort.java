package org.example.domain;

import org.example.dao.DBconn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Antwort {

	private int id;
	private String antwortText;
	private String antwortText2;
	private int antwortRanking;
	private boolean korrekt;

	private AntwortType antwortType;

	public Antwort (int id, String antwortText, boolean korrekt, AntwortType antwortType){
		this.id = id;
		this.antwortText = antwortText;
		this.korrekt = korrekt;
		this.antwortType = antwortType;
	}

	public Antwort (int id, String antwortText, String antwortText2, AntwortType antwortType) {
		this.id = id;
		this.antwortText = antwortText;
		this.antwortText2 = antwortText2;
		this.antwortType = antwortType;
	}

	public Antwort (int id, String antwortText, int antwortRanking, AntwortType antwortType) {
		this.id = id;
		this.antwortText = antwortText;
		this.antwortRanking = antwortRanking;
		this.antwortType = antwortType;
	}

	public static List<Antwort> getAntwort(int id, AntwortType format) throws SQLException {
		List<Antwort> result = new ArrayList<>();

		if (format == AntwortType.offeneAntwort) {
			ResultSet sr = DBconn.sqlSelect("offene_aufgabe", "aufgabe_id", id);
			while (sr.next()) {
				result.add(new Antwort(sr.getInt("aufgabe_id"), sr.getString("musterloesung"), true, format));
			}
			sr.close();

		} else if (format == AntwortType.geschlosseneAntwort) {
			ResultSet sr = DBconn.sqlSelect("geschlossene_aufgabe", "aufgabe_id", id);
			if (sr.next()) {
				CloseType closeType = CloseType.fromName(sr.getString("typ"));

				if (closeType == CloseType.wahrOderFalsch || closeType == CloseType.multipleChoiceFragen || closeType == CloseType.singleChoiceFragen) {
					ResultSet srAntwort = DBconn.sqlSelect("antwortmoeglichkeit_geschlossen", "geschlossene_aufgabe_id", id);
					while (srAntwort.next()) {
						result.add(new Antwort(srAntwort.getInt("id"), srAntwort.getString("antworttext"), srAntwort.getBoolean("ist_korrekt"), format));
					}
					srAntwort.close();

				} else if (closeType == CloseType.leerstellen || closeType == CloseType.zuordnung) {
					ResultSet srAntwort = DBconn.sqlSelect("antwortMehrParts_geschlossen", "geschlossene_aufgabe_id", id);
					while (srAntwort.next()) {
						result.add(new Antwort(srAntwort.getInt("id"), srAntwort.getString("antworttext"), srAntwort.getString("antworttext2"), format));
					}
					srAntwort.close();

				} else if (closeType == CloseType.ranking) {
					ResultSet srAntwort = DBconn.sqlSelect("antwortRanking_geschlossen", "geschlossene_aufgabe_id", id);
					while (srAntwort.next()) {
						result.add(new Antwort(srAntwort.getInt("id"), srAntwort.getString("antworttext"), srAntwort.getInt("rank"), format));
					}
					srAntwort.close();
				}
			}
			sr.close();
		}

		return result;
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
	public boolean isKorrekt (){
		return korrekt;
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
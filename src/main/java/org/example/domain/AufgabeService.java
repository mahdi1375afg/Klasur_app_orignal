package org.example.domain;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AufgabeService {
    //Frage
    String QuestionName;
    int QuestionPoints;
    int QuestionDuration;
    String QuestionType;
    String QuestionTaxonomie;
    private CloseType QuestionCloseType;

    String QuestionQuestion;

    //Antwort
    Map<String, Boolean> antworten = new HashMap<>();

    //Modul
    String QuestionModulName;
    int QuestionModulNumber;

    public void setTaskPageData(String QuestionName, int QuestionPoints, int QuestionDuration, String QuestionType, String QuestionTaxonomie, String QuestionModulName, CloseType closeType) {
        this.QuestionName = QuestionName;
        this.QuestionPoints = QuestionPoints;
        this.QuestionDuration = QuestionDuration;
        this.QuestionType = QuestionType;
        this.QuestionTaxonomie = QuestionTaxonomie;
        this.QuestionCloseType = closeType;
        this.QuestionModulName = QuestionModulName;
    }

    public void setAnswerPage(String answer,boolean truth) {
        antworten.put(answer, truth);
        System.out.println("Antwort: " + answer + " ist " + truth + " (hinzugefügt)");
    }

    public void save() throws SQLException {


        benutzerKonto konto = new benutzerKonto();
        int FragenId = konto.fragenErstellen(QuestionName, QuestionQuestion, QuestionDuration, QuestionType,QuestionPoints,QuestionTaxonomie,benutzerKonto.aktuellerBenutzer.getId());

        if(QuestionType.equals(AntwortType.offeneAntwort.getName())) {
            Map.Entry<String, Boolean> ersterEintrag = antworten.entrySet().iterator().next();
            ersterEintrag.getKey();
            konto.antwortErstellenOffen(FragenId,ersterEintrag.getKey());
        } else {
            konto.frageErstellenGeschlossen(FragenId,QuestionCloseType.getName());
            for (Map.Entry<String, Boolean> eintrag : antworten.entrySet()) {
                konto.antwortErstellenGeschlossen(FragenId,eintrag.getKey(),eintrag.getValue(),QuestionCloseType.getName());
            }
        }


        System.out.println("Save die Aufgabe!");

        System.out.println("QuestionName: " + QuestionName);
        System.out.println("Question: " + QuestionQuestion);
        System.out.println("Antworten: " + antworten.size());
        System.out.println(antworten);
    }

    public void setTask(String question) {
        QuestionQuestion = question;
    }
}

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
    Map<String, String> antwortenMultipleParts = new HashMap<>();
    Map<String, Integer> antwortenRanking = new HashMap<>();

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

    public void setAnswerPageMultipleParts(String answer,String answer2) {
        antwortenMultipleParts.put(answer, answer2);
        System.out.println("Antwort: " + answer + " ist " + answer2 + " (hinzugefügt)");
    }

    public void setAnswerPageRanking(String answer,int rank) {
        antwortenRanking.put(answer, rank);
        System.out.println("Antwort: " + answer + " ist " + rank + " (hinzugefügt)");
    }


    public void save() throws SQLException {
        benutzerKonto konto = new benutzerKonto();
        int FragenId = konto.fragenErstellen(QuestionName, QuestionQuestion, QuestionDuration, QuestionType,QuestionPoints,QuestionTaxonomie,benutzerKonto.aktuellerBenutzer.getId());
        konto.createTaskToModul(FragenId,QuestionModulName);
        if(QuestionType.equals(AntwortType.offeneAntwort.getName())) {
            Map.Entry<String, Boolean> ersterEintrag = antworten.entrySet().iterator().next();
            ersterEintrag.getKey();
            konto.antwortErstellenOffen(FragenId,ersterEintrag.getKey());
        } else {
            konto.frageErstellenGeschlossen(FragenId,QuestionCloseType.getName());
            if(QuestionCloseType.getName() == "Lückentext" || QuestionCloseType.getName() == "Zuordnung") {
                for (Map.Entry<String, String> eintrag : antwortenMultipleParts.entrySet()) {
                    konto.antwortErstellenGeschlossenMultipleParts(FragenId,eintrag.getKey(),eintrag.getValue());
                }
            } else if(QuestionCloseType.getName() == "Ranking") {
                for (Map.Entry<String, Integer> eintrag : antwortenRanking.entrySet()) {
                    konto.antwortErstellenGeschlossenRanking(FragenId,eintrag.getKey(),eintrag.getValue());
                }
            } else {
                for (Map.Entry<String, Boolean> eintrag : antworten.entrySet()) {
                    konto.antwortErstellenGeschlossen(FragenId,eintrag.getKey(),eintrag.getValue());
                }
            }
        }

        System.out.println("Save die Aufgabe!");

        System.out.println("QuestionName: " + QuestionName);
        System.out.println("Question: " + QuestionQuestion);

    }

    public void setTask(String question) {
        QuestionQuestion = question;
    }
}

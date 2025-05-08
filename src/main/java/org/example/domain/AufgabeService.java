package org.example.domain;

import java.util.HashMap;
import java.util.Map;

public class AufgabeService {
    //Frage
    String QuestionName;
    int QuestionPoints;
    int QuestionDuration;
    String QuestionType;
    String QuestionTaxonomie;

    String QuestionQuestion;

    //Antwort
    Map<String, Boolean> antworten = new HashMap<>();

    //Modul
    String QuestionModulName;
    int QuestionModulNumber;

    public void setTaskPageData(String QuestionName, int QuestionPoints, int QuestionDuration, String QuestionType, String QuestionTaxonomie, String QuestionModulName) {
        this.QuestionName = QuestionName;
        this.QuestionPoints = QuestionPoints;
        this.QuestionDuration = QuestionDuration;
        this.QuestionType = QuestionType;
        this.QuestionTaxonomie = QuestionTaxonomie;

        this.QuestionModulName = QuestionModulName;
    }

    public void setAnswerPage(String answer,boolean truth) {
        antworten.put(answer, truth);
        System.out.println("Antwort: " + answer + " ist " + truth);
    }

    public void save() {
        System.out.println("Save die Aufgabe!");
        System.out.println("QuestionName: " + QuestionName);
        System.out.println("Question: " + QuestionQuestion);
        System.out.println("Antworten: " + antworten.size());
    }

    public void setTask(String question) {
        QuestionQuestion = question;
    }
}

package org.example.domain;

import java.sql.SQLException;
import java.util.*;

public class AufgabeService {
    //Frage
    String QuestionName;
    int QuestionPoints;
    int QuestionDuration;
    String QuestionType;
    String QuestionTaxonomie;
    private QuestionType QuestionCloseType;

    String QuestionQuestion;

    //Antwort
    Map<String, Boolean> antworten = new LinkedHashMap<>();
    Map<String, String> antwortenMultipleParts = new LinkedHashMap<>();
    Map<String, Integer> antwortenRanking = new LinkedHashMap<>();

    //Modul
    String QuestionModulName;
    int QuestionModulNumber;

    public void setTaskPageData(String QuestionName, int QuestionPoints, int QuestionDuration, String QuestionType, String QuestionTaxonomie, String QuestionModulName, QuestionType closeType) {
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

    public void duplicateTask(Task original) throws SQLException {
        //dupliziert eine bestimmte Aufgabe

        Question question = original.getQuestion();
        List<Answer> antworten = original.getAnswer();
        Modul modul = original.getModul();

        this.setTaskPageData(
                question.getName() + "-Kopie",
                question.getPoints(),
                question.getTime(),
                question.getFormat().getName(),
                question.getTaxonomie().getKategorie(),
                modul.getName(),
                antworten.getFirst().getType()
        );

        this.setTask(question.getQuestionText());

        for (Answer answer : antworten) {
            switch (answer.getType()) {
                case wahrOderFalsch:
                case multipleChoiceFragen:
                case singleChoiceFragen:
                case offen:
                    this.setAnswerPage(answer.getAnswerText(), answer.isCorrect());
                    break;
                case zuordnung:
                case leerstellen:
                    this.setAnswerPageMultipleParts(answer.getAnswerText(), answer.getAnswerText2());
                    break;
                case ranking:
                    this.setAnswerPageRanking(answer.getAnswerText(), answer.getAnswerRanking());
                    break;
            }
        }

        this.save();
    }


    public void save() throws SQLException {
        UserAccount konto = new UserAccount();
        int FragenId = konto.questionCreate(QuestionName, QuestionQuestion, QuestionDuration, QuestionType,QuestionPoints,QuestionTaxonomie, UserAccount.aktuellerBenutzer.getId());
        konto.createTaskToModul(FragenId,QuestionModulName);
        if(QuestionType.equals(AnswerType.offeneAntwort.getName())) {
            Map.Entry<String, Boolean> ersterEintrag = antworten.entrySet().iterator().next();
            konto.questionCreateOpen(FragenId,ersterEintrag.getKey());
        } else {
            konto.questionCreateClosed(FragenId,QuestionCloseType.getName());
            if(Objects.equals(QuestionCloseType.getName(), "Lückentext") || Objects.equals(QuestionCloseType.getName(), "Zuordnung")) {
                for (Map.Entry<String, String> eintrag : antwortenMultipleParts.entrySet()) {
                    konto.answerCreateClosedMultipleParts(FragenId,eintrag.getKey(),eintrag.getValue());
                }
            } else if(Objects.equals(QuestionCloseType.getName(), "Ranking")) {
                for (Map.Entry<String, Integer> eintrag : antwortenRanking.entrySet()) {
                    konto.answerCreateClosedRanking(FragenId,eintrag.getKey(),eintrag.getValue());
                }
            } else {
                for (Map.Entry<String, Boolean> eintrag : antworten.entrySet()) {
                    konto.answerCreateClosed(FragenId,eintrag.getKey(),eintrag.getValue());
                }
            }
        }

        System.out.println("Save die Aufgabe!");

        System.out.println("QuestionName: " + QuestionName);
        System.out.println("Question: " + QuestionQuestion);
        Task.getAllTasks(UserAccount.aktuellerBenutzer.getId());

    }

    public void setTask(String question) {
        QuestionQuestion = question;
    }
}

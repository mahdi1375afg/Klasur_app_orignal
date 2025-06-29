package org.example.domain;

import org.example.dao.dbConn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Task {
    public static List<Task> tasks = new ArrayList<Task>();

    private Question question;
    private List<Answer> answer;
    private Modul modul;

    public Task(Question question, List<Answer> answer, Modul modul) {
        this.question = question;
        this.answer = answer;
        this.modul = modul;
    }

    public Question getQuestion() {return question;}
    public List<Answer> getAnswer() {return answer;}
    public Modul getModul() {return modul;}

    public static void getAllTasks(int nutzerId) throws SQLException {
        tasks.clear();

        List<Question> questions = Question.getAllFragen(nutzerId);
        for(Question question : questions) {
            List<Answer> answer = Answer.getAnswer(question.getId(), question.getFormat());
            Modul modul = Modul.getModul(question.getId());
            tasks.add(new Task(question, answer, modul));
        }
    }

    public static void deleteTask(Task task) throws SQLException {
        // Delete Antwortmoeglichkeit;
        for(Answer answer : task.getAnswer()) {
            if(answer.getType() == QuestionType.offen) {
                // Musterlösung ist in OffeneAufgabe
            } else if(answer.getType() == QuestionType.wahrOderFalsch || answer.getType() == QuestionType.multipleChoiceFragen || answer.getType() == QuestionType.singleChoiceFragen) {
                dbConn.sqlDelete("antwortmoeglichkeit_geschlossen", "geschlossene_aufgabe_id", task.getQuestion().getId());
            } else if(answer.getType() == QuestionType.leerstellen || answer.getType() == QuestionType.zuordnung) {
                dbConn.sqlDelete("antwortMehrParts_geschlossen", "geschlossene_aufgabe_id", task.getQuestion().getId());
            } else if(answer.getType() == QuestionType.ranking) {
                dbConn.sqlDelete("antwortRanking_geschlossen", "geschlossene_aufgabe_id", task.getQuestion().getId());
            }
        }

        // Delete Zusammenhang: aufgaben_modul where aufgabe_id, klausur_id equal sind
        dbConn.sqlDelete("aufgaben_modul", "aufgabe_id", task.getQuestion().getId(), "modul_id", task.getModul().getId());  // z. B. task.getModulId()

        // Delete Offene_aufgabe, geschlossene_aufgabe
        if(task.getQuestion().getFormat() == AnswerType.offeneAntwort) {
            dbConn.sqlDelete("offene_aufgabe", "aufgabe_id", task.getQuestion().getId());
        } else {
            dbConn.sqlDelete("geschlossene_aufgabe", "aufgabe_id", task.getQuestion().getId());
        }

        // Delete aufgabe
        dbConn.sqlDelete("aufgabe", "id", task.getQuestion().getId());

        tasks.remove(task);
    }


}

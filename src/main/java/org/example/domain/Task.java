package org.example.domain;

import org.example.dao.dbConn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Task {
    public static List<Task> tasks = new ArrayList<Task>();

    private Frage question;
    private List<Antwort> answer;
    private Modul modul;

    public Task(Frage question, List<Antwort> answer, Modul modul) {
        this.question = question;
        this.answer = answer;
        this.modul = modul;
    }

    public Frage getQuestion() {return question;}
    public List<Antwort> getAnswer() {return answer;}
    public Modul getModul() {return modul;}

    public static void getAllTasks(int nutzerId) throws SQLException {
        tasks.clear();

        List<Frage> questions = Frage.getAllFragen(nutzerId);
        for(Frage frage : questions) {
            List<Antwort> antwort = Antwort.getAntwort(frage.getId(),frage.getFormat());
            Modul modul = Modul.getModul(frage.getId());
            tasks.add(new Task(frage, antwort, modul));
        }
    }

    public static void deleteTask(Task task) throws SQLException {
        // Delete Antwortmoeglichkeit;
        for(Antwort antwort : task.getAnswer()) {
            if(antwort.getTyp() == QuestionType.offen) {
                // Musterlösung ist in OffeneAufgabe
            } else if(antwort.getTyp() == QuestionType.wahrOderFalsch || antwort.getTyp() == QuestionType.multipleChoiceFragen || antwort.getTyp() == QuestionType.singleChoiceFragen) {
                dbConn.sqlDelete("antwortmoeglichkeit_geschlossen", "geschlossene_aufgabe_id", task.getQuestion().getId());
            } else if(antwort.getTyp() == QuestionType.leerstellen || antwort.getTyp() == QuestionType.zuordnung) {
                dbConn.sqlDelete("antwortMehrParts_geschlossen", "geschlossene_aufgabe_id", task.getQuestion().getId());
            } else if(antwort.getTyp() == QuestionType.ranking) {
                dbConn.sqlDelete("antwortRanking_geschlossen", "geschlossene_aufgabe_id", task.getQuestion().getId());
            }
        }

        // Delete Zusammenhang: aufgaben_modul where aufgabe_id, klausur_id equal sind
        dbConn.sqlDelete("aufgaben_modul", "aufgabe_id", task.getQuestion().getId(), "modul_id", task.getModul().getId());  // z. B. task.getModulId()

        // Delete Offene_aufgabe, geschlossene_aufgabe
        if(task.getQuestion().getFormat() == AntwortType.offeneAntwort) {
            dbConn.sqlDelete("offene_aufgabe", "aufgabe_id", task.getQuestion().getId());
        } else {
            dbConn.sqlDelete("geschlossene_aufgabe", "aufgabe_id", task.getQuestion().getId());
        }

        // Delete aufgabe
        dbConn.sqlDelete("aufgabe", "id", task.getQuestion().getId());

        tasks.remove(task);
    }


}

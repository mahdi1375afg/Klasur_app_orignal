package org.example.domain;

import org.example.dao.dbConnModul;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


}

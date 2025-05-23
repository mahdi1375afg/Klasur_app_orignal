package org.example.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamService {

    String name;
    LocalDate date;
    int totalPoints;
    int totalTime;
    Modul modul;
    Map<QuestionType, Integer> questionType;
    List<BloomLevel> bloomLevels;
    int user_id;

    List<Task> tasks;

    public ExamService(String name, LocalDate date, int totalPoints, int TotalTime, Modul modul, Map<QuestionType, Integer> questionType, List<BloomLevel> bloomLevels, int user_id) {
        this.name = name;
        this.date = date;
        this.totalPoints = totalPoints;
        this.totalTime = TotalTime;
        this.modul = modul;
        this.questionType = questionType;
        this.bloomLevels = bloomLevels;
        this.user_id = user_id;
    }

    private boolean checkIfTimeAndPoints(List<Task> tasks) {
        int timeSum = 0;
        int pointSum = 0;

        for (Task task : tasks) {
            timeSum += task.getQuestion().getTime();
            pointSum += task.getQuestion().getPoints();
        }

        if (tasks.isEmpty()) {
            return false;
        } else if (timeSum < totalTime && pointSum < totalPoints) {
            return false;
        }
        return true;
    }


    public int createKlausur() {
        List<Task> allTasks = Task.tasks;
        List<Task> moduleTasks = new ArrayList<>(); // NACH MODUL FILTER
        List<Task> bloomTasks = new ArrayList<>(); // NACH BLOOM FILTER
        List<Task> typeTasks = new ArrayList<>(); // NACH TYPE FILTER

        List<Task> selectedTasks = new ArrayList<>();

        //FILTER MODUL
        for (Task task : allTasks) { // Geh alle Aufgaben durch
            if(modul.equals(task.getModul())) { // Ist das Modul der Klausur gleich dem Modul der Aufgabe?
                moduleTasks.add(task);
            }
        }

        if(!checkIfTimeAndPoints(moduleTasks)) { //Check ob noch möglich!!! Punkte, Zeit
            return 1; // Nach Modul fehlgeschlagen!!! Nicht genug Aufgaben
        }

        //FILTER BLOOMSCHE TAXONOMIE
        for (Task task : moduleTasks) {
            for (BloomLevel bloomLevel : bloomLevels) {
                if (task.getQuestion().getTaxonomie().equals(bloomLevel)) {
                    bloomTasks.add(task);
                }
            }
        }

        if(!checkIfTimeAndPoints(bloomTasks)) { //Check ob noch möglich!!! Punkte, Zeit
            return 2; // Nach Bloom fehlgeschlagen!!! Nicht genug Aufgaben
        }

        // FILTER AUFGABEN TYPE
        for (Task task : bloomTasks) { // Geh Alle Aufgaben durch
            for (QuestionType questionType : questionType.keySet()) { // Für Jede Aufgabe geh alle gewünschten QuestionTypes durch
                if(task.getAnswer().getFirst().getTyp().equals(questionType)) { // Hat die Aufgabe eine der gewünschten QuestionTypes?
                    typeTasks.add(task);
                }
            }
        }

        if(!checkIfTimeAndPoints(typeTasks)) { //Check ob noch möglich!!! Punkte, Zeit
            return 3; // Nach Type fehlgeschlagen!!! Nicht genug Aufgaben
        }

        //SELECTION
        //Pick Aufgaben so das min Anzahl an Aufgabentyp erreicht.
        List<Task> tempTasks = typeTasks;
        int remainingPoints = totalPoints;
        int remainingTime = totalTime;
        for(QuestionType Type : questionType.keySet()) { // Hol dir die min Aufgaben für jeden Type also geh alle Types durch
            int typeSum = questionType.get(Type); // Wie viel von diesem Type müssen wir haben?
            for(;typeSum > 0;) { // Wenn wir 1 oder mehr Tasks von dem Type brauchen
                for (Task task : tempTasks) { // geh alle gefilterten Tasks druch
                    boolean retry = true;
                    if(task.getAnswer().getFirst().getTyp().equals(Type)) { // Hat die gefilterte Task den richtigen Typ?
                        while(retry) {
                            retry = false;
                            if(task.getQuestion().getTime() <= remainingTime) { // Ist noch genug Zeit vorhanden für die Task?
                                if(task.getQuestion().getPoints() <= remainingPoints) { // Ist noch genug Points vorhanden für die Task?
                                    selectedTasks.add(task); // Task passt soweit. für sie in selected hinzu
                                    tempTasks.remove(task); // remove sie von temp damit sie nicht doppelt kommen kann
                                    remainingPoints -= task.getQuestion().getPoints(); // remove die Points, welche die Aufgabe braucht
                                    remainingTime = task.getQuestion().getTime(); // remove die Zeit, welche die Aufgabe braucht
                                    --typeSum; // -1 min Task von diesem Type
                                } else {
                                    // NICHT GENUG POINTS VORHANDEN MEHR
                                    int neededPoints = task.getQuestion().getPoints();
                                    for(Task exchTask : selectedTasks) {
                                        for(Task tempTask : tempTasks) {
                                            if(exchTask.getAnswer().getFirst().getTyp().equals(tempTask.getAnswer().getFirst().getTyp()) && exchTask.getQuestion().getPoints() > tempTask.getQuestion().getPoints())  {
                                                selectedTasks.remove(exchTask);
                                                selectedTasks.add(tempTask);
                                                tempTasks.remove(tempTask);
                                                remainingPoints += (exchTask.getQuestion().getPoints()-tempTask.getQuestion().getPoints());
                                                if (neededPoints <= remainingPoints) {
                                                    retry = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if(retry) {
                                            break;
                                        }
                                    }

                                }
                            } else {
                                // NICHT GENUG TIME VORHANDEN MEHR
                                int neededTime = task.getQuestion().getTime();
                                for(Task exchTask : selectedTasks) {
                                    for(Task tempTask : tempTasks) {
                                        if(exchTask.getAnswer().getFirst().getTyp().equals(tempTask.getAnswer().getFirst().getTyp()) && exchTask.getQuestion().getTime() > tempTask.getQuestion().getTime()) {
                                            selectedTasks.remove(exchTask);
                                            selectedTasks.add(tempTask);
                                            tempTasks.remove(tempTask);
                                            remainingTime += (exchTask.getQuestion().getTime()-tempTask.getQuestion().getTime());
                                            if(neededTime <= remainingTime) {
                                                retry = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(retry) {
                                    break;
                                }
                            }
                        }
                    } else {
                        return 4; // NICHT GENUG AUFGABEN (Kann das überhaupt aufgerufen werden wenn wir min aufgaben cappen auf Max anzahl?)
                    }
                }
            }
            if(remainingPoints > 0) {
                if(remainingTime > 0) {
                    for (Task task : tempTasks) {
                        if(task.getQuestion().getTime() <= remainingTime && task.getQuestion().getPoints() <= remainingPoints) {
                            selectedTasks.add(task);
                            remainingPoints -= task.getQuestion().getPoints();
                            remainingTime = task.getQuestion().getTime();
                        }
                    }
                } else {
                    return 6;
                }
            } else {
                return 5;
            }
        }
        tasks = selectedTasks;
        return 1;
    }


    public String getName() {
        return name;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getTotalpoints() {
        return totalPoints;
    }
    public int getUser_id() {
        return user_id;
    }





}

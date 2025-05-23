package org.example.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
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
        // Filter
        List<Task> moduleTasks = new ArrayList<>();
        List<Task> bloomTasks = new ArrayList<>();
        List<Task> typeTasks = new ArrayList<>();
        // Ergebnis
        List<Task> selectedTasks = new ArrayList<>();

        // FILTER: Modul
        for (Task task : allTasks) {
            if (modul.equals(task.getModul())) {
                moduleTasks.add(task);
            }
        }

        if (!checkIfTimeAndPoints(moduleTasks)) return 1;

        // FILTER: Bloom
        for (Task task : moduleTasks) {
            for (BloomLevel bloomLevel : bloomLevels) {
                if (task.getQuestion().getTaxonomie().equals(bloomLevel)) {
                    bloomTasks.add(task);
                }
            }
        }

        if (!checkIfTimeAndPoints(bloomTasks)) return 2;

        // FILTER: Typ
        for (Task task : bloomTasks) {
            for (QuestionType questionTypeKey : questionType.keySet()) {
                if (task.getAnswer().getFirst().getTyp().equals(questionTypeKey)) {
                    typeTasks.add(task);
                }
            }
        }

        if (!checkIfTimeAndPoints(typeTasks)) return 3;

        List<Task> tempTasks = new ArrayList<>(typeTasks);
        int remainingPoints = totalPoints;
        int remainingTime = totalTime;

        for (QuestionType type : questionType.keySet()) {
            int typeSum = questionType.get(type);
            while (typeSum > 0) {
                boolean foundTask = false;
                Iterator<Task> iterator = tempTasks.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.getAnswer().getFirst().getTyp().equals(type)) {
                        boolean retry = true;
                        while (retry) {
                            retry = false;
                            if (task.getQuestion().getTime() <= remainingTime && task.getQuestion().getPoints() <= remainingPoints) {
                                selectedTasks.add(task);
                                iterator.remove(); // Sichere Entfernung!
                                remainingPoints -= task.getQuestion().getPoints();
                                remainingTime -= task.getQuestion().getTime(); // FIXED: subtrahieren statt ersetzen
                                --typeSum;
                                foundTask = true;
                            } else {
                                boolean swapped = false;

                                int neededPoints = task.getQuestion().getPoints();
                                int neededTime = task.getQuestion().getTime();

                                boolean needsPoints = neededPoints > remainingPoints;
                                boolean needsTime = neededTime > remainingTime;

                                if (needsPoints) {
                                    Iterator<Task> selectedIterator = selectedTasks.iterator();
                                    while (selectedIterator.hasNext() && !swapped) {
                                        Task exchTask = selectedIterator.next();
                                        for (Task tempTask : tempTasks) {
                                            if (exchTask.getAnswer().getFirst().getTyp().equals(tempTask.getAnswer().getFirst().getTyp())
                                                    && exchTask.getQuestion().getPoints() > tempTask.getQuestion().getPoints()) {
                                                selectedIterator.remove();
                                                selectedTasks.add(tempTask);
                                                tempTasks.remove(tempTask);
                                                remainingPoints += (exchTask.getQuestion().getPoints() - tempTask.getQuestion().getPoints());
                                                if (neededPoints <= remainingPoints) {
                                                    retry = true;
                                                    swapped = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (!swapped && needsTime) {
                                    Iterator<Task> selectedIterator = selectedTasks.iterator();
                                    while (selectedIterator.hasNext() && !swapped) {
                                        Task exchTask = selectedIterator.next();
                                        for (Task tempTask : tempTasks) {
                                            if (exchTask.getAnswer().getFirst().getTyp().equals(tempTask.getAnswer().getFirst().getTyp())
                                                    && exchTask.getQuestion().getTime() > tempTask.getQuestion().getTime()) {
                                                selectedIterator.remove();
                                                selectedTasks.add(tempTask);
                                                tempTasks.remove(tempTask);
                                                remainingTime += (exchTask.getQuestion().getTime() - tempTask.getQuestion().getTime());
                                                if (neededTime <= remainingTime) {
                                                    retry = true;
                                                    swapped = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (!foundTask) return 4;
            }
        }

        if (remainingPoints > 0) {
            if (remainingTime > 0) {
                Iterator<Task> iterator = tempTasks.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.getQuestion().getTime() <= remainingTime &&
                            task.getQuestion().getPoints() <= remainingPoints) {
                        selectedTasks.add(task);
                        remainingPoints -= task.getQuestion().getPoints();
                        remainingTime -= task.getQuestion().getTime();
                        iterator.remove();
                    }
                }
            } else {
                System.out.println("Punkte übrig: " + remainingPoints);
                System.out.println("Zeit übrig: " + remainingTime);
            }
        } else {
            System.out.println("Punkte übrig: " + remainingPoints);
            System.out.println("Zeit übrig: " + remainingTime);
        }

        tasks = selectedTasks;
        System.out.println("Exam könnte erstellt werden mit: " + selectedTasks.size() + " Tasks");
        return 0; // Erfolgreich erstellt
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

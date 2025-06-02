package org.example.domain;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;


public class ExamService {

    static String name;
    static LocalDate date;
    int totalPoints;
    int totalTime;
    Modul modul;
    Map<QuestionType, Integer> questionType;
    List<BloomLevel> bloomLevels;
    int user_id;

    static List<Task> tasks;

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


    public int createKlausur() throws IOException {
        //ToDo: Algorithmus robuster machen und darauf achten, dass Algorithmus oprimale und nicht erste Lösung verwendet

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
                                iterator.remove();
                                remainingPoints -= task.getQuestion().getPoints();
                                remainingTime -= task.getQuestion().getTime();
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
            } else {}
        } else {}

        System.out.println("Punkte übrig: " + remainingPoints);
        System.out.println("Zeit übrig: " + remainingTime);
        tasks = selectedTasks;
        System.out.println("Exam könnte erstellt werden mit: " + selectedTasks.size() + " Tasks");
        generatePdf();
        return 0; // Erfolgreich erstellt
    }


    public static void generatePdf() throws IOException {
        //ToDo: Deckblatt erstellen, alle Arten von Aufgaben hinzufügen

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(name + ".pdf"));
        document.open();

        PdfContentByte cb = writer.getDirectContent();

        Font headerFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font questionHeaderFont = new Font(Font.HELVETICA, 14);
        questionHeaderFont.setColor(Color.blue);
        Font questionFont = new Font(Font.HELVETICA, 12);
        Font answerFont = new Font(Font.HELVETICA, 10);
        Font punktFont = new Font(Font.HELVETICA, 10, Font.ITALIC);

        document.add(new Paragraph("Klausur: " + name + "                                " + "Datum: " + date.toString(), headerFont));
        //document.add(new Paragraph("Prüfer: " + Pruefer));
        document.add(new Paragraph(" "));

        int aufgabeNummer = 1;
        for(Task task : tasks) {
            Paragraph taskHeadParagraph = new Paragraph();
            taskHeadParagraph.add(new Chunk("Aufgabe " + aufgabeNummer + " (" + task.getQuestion().getPoints() + " Punkte)", questionHeaderFont));
            taskHeadParagraph.setSpacingBefore(10);
            taskHeadParagraph.setSpacingAfter(2);
            document.add(taskHeadParagraph);

            Paragraph taskParagraph = new Paragraph();
            taskParagraph.add(new Chunk(task.getQuestion().getQuestionText(), questionFont));
            taskParagraph.setSpacingBefore(15);
            taskParagraph.setSpacingAfter(2);
            document.add(taskParagraph);

            QuestionType typ = task.getAnswer().getFirst().getTyp();

            if(typ == QuestionType.multipleChoiceFragen || typ == QuestionType.singleChoiceFragen || typ == QuestionType.wahrOderFalsch) {
                for(int i = 0; i < task.getAnswer().size(); i++) {
                    String antwortText = task.getAnswer().get(i).getAntwortText();

                    Paragraph antwortParagraph = new Paragraph("[  ]  " + antwortText, answerFont);
                    antwortParagraph.setIndentationLeft(20f);
                    antwortParagraph.setSpacingAfter(2f);
                    document.add(antwortParagraph);
                }
            }

            document.add(new Paragraph(" "));

            aufgabeNummer++;
        }

        document.close();
        System.out.println("PDF wurde erstellt");
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

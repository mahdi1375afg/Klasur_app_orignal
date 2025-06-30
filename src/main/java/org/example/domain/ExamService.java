package org.example.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.util.List;


public class ExamService {

    String name;
    LocalDate date;
    int totalPoints;
    int totalTime;
    Modul modul;
    String pruefer;
    String schule = "Hochschule Furtwangen";
    Map<QuestionType, Integer> questionType;
    List<BloomLevel> bloomLevels;
    int user_id;

    static List<Task> tasks;

    public String getName() {
        return name;
    }
    public LocalDate getDate() {
        return date;
    }
    public Integer getTotalPoints() {
        return totalPoints;
    }
    public Integer getTotalTime() { return totalTime; }
    public Modul getModul() { return modul; }
    public String getPruefer() { return pruefer; }
    public Map<QuestionType, Integer> getQuestionType() { return questionType; }
    public List<BloomLevel> getBloomLevels() { return bloomLevels; }




    public ExamService(String name, String pruefer, LocalDate date, int totalPoints, int TotalTime, Modul modul, Map<QuestionType, Integer> questionType, List<BloomLevel> bloomLevels, int user_id) {
        //ToDo: Prüfer namen richtig setzen
        this.name = name;
        this.date = date;
        this.totalPoints = totalPoints;
        this.totalTime = TotalTime;
        this.modul = modul;
        this.questionType = questionType;
        this.bloomLevels = bloomLevels;
        this.pruefer = pruefer;
        this.user_id = user_id;
    }

    public void save() throws IOException {
        generatePdf(name,date,totalPoints,0,modul,questionType,bloomLevels,pruefer,user_id,schule);
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

    public int createKlausur() throws IOException, SQLException {
        //ToDo: Algorithmus robuster machen und darauf achten, dass Algorithmus oprimale und nicht erste Lösung verwendet

        List<Task> allTasks = Task.tasks;
        List<Task> moduleTasks = new ArrayList<>();
        List<Task> bloomTasks = new ArrayList<>();
        List<Task> typeTasks = new ArrayList<>();
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
                if (task.getAnswer().getFirst().getType().equals(questionTypeKey)) {
                    typeTasks.add(task);
                }
            }
        }
        if (!checkIfTimeAndPoints(typeTasks)) return 3;

        List<Task> tempTasks = new ArrayList<>(typeTasks);
        int remainingPoints = totalPoints;
        int remainingTime = totalTime;

        // Kopie der erlaubten Mengen pro Typ
        Map<QuestionType, Integer> remainingTypeCounts = new java.util.HashMap<>(questionType);

        // 1. Für jeden gewünschten Bloom-Level eine Aufgabe auswählen
        for (BloomLevel bloomLevel : bloomLevels) {
            boolean found = false;
            Iterator<Task> it = tempTasks.iterator();
            while (it.hasNext()) {
                Task task = it.next();
                if (task.getQuestion().getTaxonomie().equals(bloomLevel)) {
                    QuestionType type = task.getAnswer().getFirst().getType();
                    if (remainingTypeCounts.getOrDefault(type, 0) > 0 && task.getQuestion().getPoints() <= remainingPoints && task.getQuestion().getTime() <= remainingTime) {
                        selectedTasks.add(task);
                        it.remove();
                        remainingPoints -= task.getQuestion().getPoints();
                        remainingTime -= task.getQuestion().getTime();
                        remainingTypeCounts.put(type, remainingTypeCounts.get(type) - 1);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) return 5; // Keine passende Bloom-Aufgabe gefunden
        }

        //2. Weitere Aufgaben füllen, solange Typanzahl & Ressourcen passen
        for (QuestionType type : questionType.keySet()) {
            int typeSum = remainingTypeCounts.getOrDefault(type, 0);
            while (typeSum > 0) {
                boolean foundTask = false;
                Iterator<Task> iterator = tempTasks.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.getAnswer().getFirst().getType().equals(type)) {
                        boolean retry = true;
                        while (retry) {
                            retry = false;
                            if (task.getQuestion().getTime() <= remainingTime &&
                                    task.getQuestion().getPoints() <= remainingPoints) {

                                selectedTasks.add(task);
                                iterator.remove();
                                remainingPoints -= task.getQuestion().getPoints();
                                remainingTime -= task.getQuestion().getTime();
                                --typeSum;
                                remainingTypeCounts.put(type, typeSum);
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
                                            if (exchTask.getAnswer().getFirst().getType().equals(tempTask.getAnswer().getFirst().getType())
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
                                            if (exchTask.getAnswer().getFirst().getType().equals(tempTask.getAnswer().getFirst().getType())
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
                if (!foundTask) break; // Kann keine weitere passende Aufgabe dieses Typs finden
            }
        }

        // 3. Falls noch Punkte/Zeit übrig → Rest füllen (ohne Typgrenzen zu verletzen)
        if (remainingPoints > 0 && remainingTime > 0) {
            Iterator<Task> iterator = tempTasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                QuestionType type = task.getAnswer().getFirst().getType();
                if (remainingTypeCounts.getOrDefault(type, 0) > 0 &&
                        task.getQuestion().getPoints() <= remainingPoints &&
                        task.getQuestion().getTime() <= remainingTime) {

                    selectedTasks.add(task);
                    remainingPoints -= task.getQuestion().getPoints();
                    remainingTime -= task.getQuestion().getTime();
                    remainingTypeCounts.put(type, remainingTypeCounts.get(type) - 1);
                    iterator.remove();
                }
            }
        }

        System.out.println("Punkte uebrig: " + remainingPoints);
        System.out.println("Zeit uebrig: " + remainingTime);
        tasks = selectedTasks;
        System.out.println("Exam konnte erstellt werden mit: " + selectedTasks.size() + " Tasks");
        save();
        return 0; // Erfolgreich erstellt
    }


    public static void generatePdf(String name, LocalDate date, int totalPoints, int TotalTime, Modul modul, Map<QuestionType, Integer> questionType, List<BloomLevel> bloomLevels, String pruefer, int user_id, String schule) throws IOException {

        // EXAM + DIRECTORY
        Document document = new Document(PageSize.A4);
        String outputDir = "target/GeneratedExams/";
        new File(outputDir).mkdirs();
        String filePath = outputDir + name + ".pdf";
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // MUSTERLOESUNG
        Document musterloesung = new Document(PageSize.A4);
        String outputDirLoesung = outputDir + "Loesungen/";
        new File(outputDirLoesung).mkdirs();
        String filePathLoesung = outputDirLoesung + name + "_Loesung.pdf";
        PdfWriter writerLoesung = PdfWriter.getInstance(musterloesung, new FileOutputStream(filePathLoesung));
        musterloesung.open();



        // Deckblatt erstellen
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font subTitleFont = new Font(Font.HELVETICA, 14, Font.NORMAL);
        Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12);

        //Header
        Paragraph header = new Paragraph("Klausur zur Vorlesung\n\n" + modul.getName() + "\n\n" + schule + "\n" + date + "\n\n" + pruefer, titleFont);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(30);
        document.add(header);

        Paragraph headerMuster = new Paragraph("MUSTERLOESUNG", subTitleFont);
        musterloesung.add(header);
        musterloesung.add(headerMuster);

        //Hinweis
        Paragraph hinweis = new Paragraph("Bitte tragen Sie Ihre Matrikelnummer und Ihren Namen ein!", boldFont);
        hinweis.setSpacingAfter(10);
        document.add(hinweis);
        musterloesung.add(hinweis);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        musterloesung.add(Chunk.NEWLINE);

        // Name & Matrikelnummer
        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100);

        PdfPCell nameCell = new PdfPCell(new Phrase("Name, Vorname", normalFont));
        nameCell.setFixedHeight(30);
        table1.addCell(nameCell);

        PdfPCell matrikelCell = new PdfPCell(new Phrase("Matrikelnummer", normalFont));
        matrikelCell.setFixedHeight(30);
        table1.addCell(matrikelCell);

        document.add(table1);
        musterloesung.add(table1);


        int anzahlAufgaben = tasks.size();
        int totalCols = anzahlAufgaben + 2;

        float[] spaltenBreiten = new float[totalCols];
        Arrays.fill(spaltenBreiten, 1f);

        PdfPTable punktetabelle = new PdfPTable(spaltenBreiten);
        punktetabelle.setWidthPercentage(70);
        punktetabelle.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell punkteHeader = new PdfPCell(new Phrase("Punkte", boldFont));
        punkteHeader.setColspan(anzahlAufgaben + 1);
        punkteHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        punktetabelle.addCell(punkteHeader);

        PdfPCell noteCell = new PdfPCell(new Phrase("Note", boldFont));
        noteCell.setRowspan(3);
        noteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        noteCell.setVerticalAlignment(Element.ALIGN_TOP);
        punktetabelle.addCell(noteCell);

        for (int i = 1; i <= anzahlAufgaben; i++) {
            PdfPCell nummerZelle = new PdfPCell(new Phrase(String.valueOf(i), normalFont));
            nummerZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
            punktetabelle.addCell(nummerZelle);
        }
        PdfPCell summeZelle = new PdfPCell(new Phrase("Σ", normalFont));
        summeZelle.setHorizontalAlignment(Element.ALIGN_CENTER);
        punktetabelle.addCell(summeZelle);

        for (int i = 0; i < anzahlAufgaben + 1; i++) {
            PdfPCell leer = new PdfPCell(new Phrase(" "));
            punktetabelle.addCell(leer);
        }

        document.add(punktetabelle);
        document.add(Chunk.NEWLINE);
        musterloesung.add(punktetabelle);
        musterloesung.add(Chunk.NEWLINE);

        //Hinweise
        Paragraph hinweise = new Paragraph("Bitte beachten Sie:\n\n" +"   • Ab "+ totalPoints/2 +" (von "+ totalPoints +" möglichen) Punkten ist die Klausur bestanden (Note 4.0).\n" + "   • Nur Stifte und leere Papierblätter sind als Hilfsmittel erlaubt.\n", normalFont);
        hinweise.setSpacingBefore(20);
        document.add(hinweise);
        document.add(Chunk.NEWLINE);
        musterloesung.add(hinweise);
        musterloesung.add(Chunk.NEWLINE);

        // Viel Erfolg
        Paragraph erfolg = new Paragraph("Viel Erfolg!", boldFont);
        erfolg.setSpacingBefore(20);
        document.add(erfolg);
        musterloesung.add(erfolg);

        document.newPage();
        musterloesung.newPage();


        // HIER GEHT AUFGABEN LOS

        Font headerFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font questionHeaderFont = new Font(Font.HELVETICA, 14);
        questionHeaderFont.setColor(Color.blue);
        Font questionFont = new Font(Font.HELVETICA, 12);
        Font answerFont = new Font(Font.HELVETICA, 10);
        Font answerFont2 = new Font(Font.HELVETICA, 8, Font.ITALIC);
        Font punktFont = new Font(Font.HELVETICA, 10, Font.ITALIC);

        document.add(new Paragraph("Klausur: " + name + "                                " + "Datum: " + date.toString(), headerFont));
        musterloesung.add(new Paragraph("Klausur: " + name + "                                " + "Datum: " + date.toString(), headerFont));
        document.add(new Paragraph(" "));
        musterloesung.add(new Paragraph(" "));

        int aufgabeNummer = 1;
        for(Task task : tasks) {
            Paragraph taskHeadParagraph = new Paragraph();
            taskHeadParagraph.add(new Chunk("Aufgabe " + aufgabeNummer + " (" + task.getQuestion().getPoints() + " Punkte)", questionHeaderFont));
            taskHeadParagraph.setSpacingBefore(10);
            taskHeadParagraph.setSpacingAfter(2);
            document.add(taskHeadParagraph);
            musterloesung.add(taskHeadParagraph);

            Paragraph taskParagraph = new Paragraph();
            taskParagraph.add(new Chunk(task.getQuestion().getQuestionText(), questionFont));
            taskParagraph.setSpacingBefore(15);
            taskParagraph.setSpacingAfter(2);
            document.add(taskParagraph);
            musterloesung.add(taskParagraph);

            QuestionType typ = task.getAnswer().getFirst().getType();


            if(typ == QuestionType.multipleChoiceFragen || typ == QuestionType.singleChoiceFragen || typ == QuestionType.wahrOderFalsch) {
                // Multiple Choice + Single Choice + Wahr/Falsch
                for(int i = 0; i < task.getAnswer().size(); i++) {
                    String antwortText = task.getAnswer().get(i).getAnswerText();
                    boolean right = task.getAnswer().get(i).isCorrect();
                    Paragraph antwortParagraph = new Paragraph("[  ]  " + antwortText, answerFont);
                    Paragraph antwortParagraphRight = new Paragraph("[ X ]  " + antwortText, answerFont);
                    antwortParagraph.setIndentationLeft(20f);
                    antwortParagraph.setSpacingAfter(2f);
                    antwortParagraphRight.setIndentationLeft(20f);
                    antwortParagraphRight.setSpacingAfter(2f);
                    document.add(antwortParagraph);
                    if(right) {
                        musterloesung.add(antwortParagraphRight);
                    } else {
                        musterloesung.add(antwortParagraph);
                    }
                }

            } else if(typ == QuestionType.offen) {
                String antwortText = task.getAnswer().getFirst().getAnswerText();
                Paragraph antwortParagraph = new Paragraph(antwortText, answerFont);
                musterloesung.add(antwortParagraph);
            } else if(typ == QuestionType.ranking) {
                // Ranking + Zuordnung
                for (int i = 0; i < task.getAnswer().size(); i++) {
                    String antwortText = task.getAnswer().get(i).getAnswerText();
                    int position = task.getAnswer().get(i).getAnswerRanking();
                    Paragraph antwortParagraph = new Paragraph("[   ] " + antwortText, answerFont);
                    Paragraph antwortParagraphRight = new Paragraph("[" + position + "] " + antwortText, answerFont);
                    antwortParagraph.setIndentationLeft(20f);
                    antwortParagraph.setSpacingAfter(2f);
                    antwortParagraphRight.setIndentationLeft(20f);
                    antwortParagraphRight.setSpacingAfter(2f);
                    document.add(antwortParagraph);
                    musterloesung.add(antwortParagraphRight);
                }
            } else if(typ == QuestionType.zuordnung) {
                // Zuordnung

                List<String> leftItems = new ArrayList<>();
                List<String> rightItems = new ArrayList<>();

                for (Answer answer : task.getAnswer()) {
                    leftItems.add(answer.getAnswerText());
                    rightItems.add(answer.getAnswerText2());
                }

                // Zufällige Reihenfolge der rechten Seite
                List<String> shuffledRightItems = new ArrayList<>(rightItems);
                Collections.shuffle(shuffledRightItems);

                PdfPTable table = new PdfPTable(2);
                table.setWidths(new int[]{1, 1});
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                char letter = 'a';
                for (int i = 0; i < leftItems.size(); i++) {
                    PdfPCell cellLeft = new PdfPCell(new Phrase((i + 1) + ". " + leftItems.get(i), answerFont));
                    PdfPCell cellRight = new PdfPCell(new Phrase((char)(letter + i) + ". " + shuffledRightItems.get(i), answerFont));
                    cellLeft.setPadding(5f);
                    cellRight.setPadding(5f);

                    table.addCell(cellLeft);
                    table.addCell(cellRight);
                }

                document.add(table);

                PdfPTable loesungstabelle = new PdfPTable(2);
                loesungstabelle.setWidths(new int[]{1, 1});
                loesungstabelle.setSpacingBefore(10f);
                loesungstabelle.setSpacingAfter(10f);

                for (int i = 0; i < leftItems.size(); i++) {
                    PdfPCell cellLeft = new PdfPCell(new Phrase((i + 1) + ". " + leftItems.get(i), answerFont));
                    PdfPCell cellRight = new PdfPCell(new Phrase((char)(letter + i) + ". " + rightItems.get(i), answerFont));
                    cellLeft.setPadding(5f);
                    cellRight.setPadding(5f);

                    loesungstabelle.addCell(cellLeft);
                    loesungstabelle.addCell(cellRight);
                }

                musterloesung.add(loesungstabelle);
            } else if(typ == QuestionType.leerstellen) {
                String vollerText = task.getAnswer().getFirst().getAnswerText2();
                String lueckenText = task.getAnswer().getFirst().getAnswerText();

                vollerText = vollerText.replaceAll("[.,;!?]", "").toLowerCase();
                lueckenText = lueckenText.replaceAll("[.,;!?]", "").toLowerCase();

                String[] vollerWoerter = vollerText.split("\\s+");
                String[] lueckenWoerter = lueckenText.split("\\s+");

                List<String> gefundeneLuecken = new ArrayList<>();

                for (int i = 0; i < Math.min(vollerWoerter.length, lueckenWoerter.length); i++) {
                    if (lueckenWoerter[i].equals("_")) {
                        gefundeneLuecken.add(vollerWoerter[i]);
                    }
                }

                Paragraph Lueckentext = new Paragraph(task.getAnswer().getFirst().getAnswerText(),answerFont);

                Paragraph Worte = new Paragraph(" ", answerFont2);
                for(String wort : gefundeneLuecken) {
                    Worte.add(wort + " ");
                }
                System.out.println("gefundeneLuecken:");
                for (String wort : gefundeneLuecken) {
                    System.out.println("'" + wort + "'");
                }

                document.add(Lueckentext);
                System.out.println("Worte: " + Worte);
                document.add(Worte);

                Paragraph VollerText = new Paragraph(task.getAnswer().getFirst().getAnswerText2(),answerFont);
                musterloesung.add(VollerText);
            }

            document.add(new Paragraph(" "));
            musterloesung.add(new Paragraph(" "));

            aufgabeNummer++;
        }

        document.close();
        musterloesung.close();
        System.out.println("PDF wurde erstellt");
    }




}

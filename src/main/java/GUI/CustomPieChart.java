package GUI;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.domain.Antwort;
import org.example.domain.Modul;
import org.example.domain.QuestionType;
import org.example.domain.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomPieChart extends PieChart {

    private final List<Color> colorPalette = new ArrayList<>();

    public CustomPieChart(ObservableList<Data> data, boolean setRightClickMenu) {
        //erstellt das chart, sowie die Legende mit benutzerdefinierten Farben
        super(data);
        setLegendSide(Side.RIGHT);
        setClockwise(true);

        sceneProperty().addListener((obs, oldScene, newScene) -> {
            generateColorPalette(getData().size());
            applyCustomColors();
            createCustomScrollableLegend(setRightClickMenu);
        });
    }

    private void createCustomScrollableLegend(boolean setRightClickMenu) {
        //erstellt eine passende scrollbare Legende für das chart

        Node oldLegend = lookup(".chart-legend");
        if (oldLegend == null) return;

        oldLegend.setVisible(false);
        oldLegend.setManaged(false);

        Node parentNode = oldLegend.getParent();
        if (parentNode == null) return;

        Node grandParentNode = parentNode.getParent();

        if (!(grandParentNode instanceof VBox pane)) return;

        VBox legendBox = new VBox(5);
        legendBox.setPadding(new Insets(5));
        legendBox.setStyle("-fx-background-color: transparent;");

        ObservableList<Data> dataList = getData();
        for (int i = 0; i < dataList.size(); i++) {
            Data d = dataList.get(i);
            HBox item = new HBox(5);
            item.setAlignment(Pos.CENTER_LEFT);

            Rectangle colorBox = new Rectangle(12, 12);
            Color color = colorPalette.get(i);
            colorBox.setFill(color);
            colorBox.setStroke(Paint.valueOf("transparent"));

            Label label = new Label(d.getName());
            label.setStyle("-fx-font-size: 12px;");

            item.getChildren().addAll(colorBox, label);

            item.setOnMouseEntered(e -> highlightSegment(d, item));
            item.setOnMouseExited(e -> resetSegment(d, item));

            if (setRightClickMenu) {
                addRightClickMenu(item, d);
            }

            legendBox.getChildren().add(item);
        }

        ScrollPane scrollPane = new ScrollPane(legendBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefViewportHeight(300);

        pane.getChildren().add(scrollPane);
    }

    private void addRightClickMenu(HBox item, Data data) {
        //fügt Menü ein, dass bei Rechtsklick geöffnet wird

        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Löschen");


        deleteItem.setOnAction(event -> {
            boolean confirm = showAlert();
            if (confirm) {
                List<Task> deletableTasks = new ArrayList<>();
                for (Task task : Task.tasks) {
                    if (task.getModul().getName().equals(data.getName())) {
                        deletableTasks.add(task);
                    }
                }

                for (Task task : deletableTasks) {
                    try {
                        Task.deleteTask(task);
                    } catch (SQLException ignored) {}
                }

                Modul deletableModul = null;
                for (Modul modul : Modul.modules) {
                    if (modul.getName().equals(data.getName())) {
                        deletableModul = modul;
                        break;
                    }
                }

                try {
                    assert deletableModul != null;
                    Modul.deleteModul(deletableModul);
                } catch (SQLException ignored) {
                }

                try {
                    Modul.getAllModul();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Deleted: " + data.getName());

                try {
                    MenuItem sourceMenuItem = (MenuItem) event.getSource();
                    ContextMenu contextMenuDelete = sourceMenuItem.getParentPopup();
                    Node ownerNode = contextMenuDelete.getOwnerNode();
                    Stage stage = (Stage) ownerNode.getScene().getWindow();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("startPage.fxml"));
                    Parent root = loader.load();

                    StartPageController controller = loader.getController();
                    controller.updateCharts();

                    double sceneWidth = stage.getScene().getWidth();
                    double sceneHeight = stage.getScene().getHeight();

                    Scene scene = new Scene(root, sceneWidth, sceneHeight);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException | SQLException ignored) {}
            }
        });



        MenuItem printItem = new MenuItem("Exportieren");
        printItem.setOnAction(e -> {
            //ToDo: Druckfunktion einfügen
            ObservableList<Task> selectedTasks = FXCollections.observableArrayList(Task.tasks.stream().filter(task -> task.getModul().getName().equals(data.getName())).toList());

            if (selectedTasks.isEmpty())
                return;
            Document document = new Document(PageSize.A4);
            String outputDir = "target/GeneratedExams/Tasks/";
            new File(outputDir).mkdirs();
            String filePath = outputDir + selectedTasks.getFirst().getModul().getName() + ".pdf";
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            } catch (FileNotFoundException de) {
                throw new RuntimeException(de);
            }
            document.open();
            Document musterloesung = new Document(PageSize.A4);
            String outputDirLoesung = outputDir + "Loesungen/";
            new File(outputDirLoesung).mkdirs();
            String filePathLoesung = outputDirLoesung + selectedTasks.getFirst().getModul().getName() + "_Loesung.pdf";
            try {
                PdfWriter writerLoesung = PdfWriter.getInstance(musterloesung, new FileOutputStream(filePathLoesung));
            } catch (FileNotFoundException de) {
                throw new RuntimeException(de);
            }
            musterloesung.open();

            Font headerFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font questionHeaderFont = new Font(Font.HELVETICA, 14);
            questionHeaderFont.setColor(java.awt.Color.blue);
            Font questionFont = new Font(Font.HELVETICA, 12);
            Font answerFont = new Font(Font.HELVETICA, 10);
            Font answerFont2 = new Font(Font.HELVETICA, 8, Font.ITALIC);
            Font punktFont = new Font(Font.HELVETICA, 10, Font.ITALIC);

            int aufgabeNummer = 1;
            for(Task task : selectedTasks) {
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

                QuestionType typ = task.getAnswer().getFirst().getTyp();


                if(typ == QuestionType.multipleChoiceFragen || typ == QuestionType.singleChoiceFragen || typ == QuestionType.wahrOderFalsch) {
                    // Multiple Choice + Single Choice + Wahr/Falsch
                    for(int i = 0; i < task.getAnswer().size(); i++) {
                        String antwortText = task.getAnswer().get(i).getAntwortText();
                        boolean right = task.getAnswer().get(i).isKorrekt();
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

                } else if(typ == QuestionType.ranking) {
                    // Ranking + Zuordnung
                    for (int i = 0; i < task.getAnswer().size(); i++) {
                        String antwortText = task.getAnswer().get(i).getAntwortText();
                        int position = task.getAnswer().get(i).getAntwortRanking();
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

                    for (Antwort answer : task.getAnswer()) {
                        leftItems.add(answer.getAntwortText());
                        rightItems.add(answer.getAntwortText2());
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

                    // Tabelle für Musterlösung (korrekte Paare)
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
                    //ToDO: Keine Ahnung ob das stimmt, hier muss noch dran gearbeitet werden
                    String vollerText = task.getAnswer().getFirst().getAntwortText2();
                    String lueckenText = task.getAnswer().getFirst().getAntwortText();

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

                    Paragraph Lueckentext = new Paragraph(task.getAnswer().getFirst().getAntwortText(),answerFont);

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

                    Paragraph VollerText = new Paragraph(task.getAnswer().getFirst().getAntwortText2(),answerFont);
                    musterloesung.add(VollerText);
                }

                document.add(new Paragraph(" "));
                musterloesung.add(new Paragraph(" "));

                aufgabeNummer++;
            }

            document.close();
            musterloesung.close();
            System.out.println("PDF wurde erstellt");

            File sourcePdfFile = new File(outputDir + selectedTasks.getFirst().getModul().getName() + ".pdf");
            File sourcePdfFileSolution = new File(outputDirLoesung + selectedTasks.getFirst().getModul().getName() + "_Loesung.pdf");
            String userHome = System.getProperty("user.home");
            Path downloadsPath = Paths.get(userHome, "Downloads");

            Path destinationPath = downloadsPath.resolve(sourcePdfFile.getName());
            Path uniquePath = getUniqueDestinationPath(destinationPath);
            Path destinationPathSolution = downloadsPath.resolve(sourcePdfFileSolution.getName());
            Path uniquePathSolution = getUniqueDestinationPath(destinationPathSolution);


            try {
                Files.copy(sourcePdfFile.toPath(), uniquePath);
                Files.copy(sourcePdfFileSolution.toPath(), uniquePathSolution);
            } catch (IOException ignored) {}

        });

        contextMenu.getItems().addAll(deleteItem, printItem);

        item.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(item, e.getScreenX(), e.getScreenY());
            } else if (e.getButton() == MouseButton.PRIMARY) {
                contextMenu.hide();
            }
        });
    }

    private Path getUniqueDestinationPath(Path destinationPath) {
        //erstellt einen einzigartigen Namen für das PDF

        Path parentDir = destinationPath.getParent();
        String fileName = destinationPath.getFileName().toString();

        String baseName;
        String extension;

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            baseName = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        } else {
            baseName = fileName;
            extension = "";
        }

        int counter = 1;
        Path newPath = destinationPath;

        while (Files.exists(newPath)) {
            String newFileName = String.format("%s(%d)%s", baseName, counter++, extension);
            newPath = parentDir.resolve(newFileName);
        }

        return newPath;
    }



    private void highlightSegment(Data d, HBox item) {
        // Alle zurücksetzen
        for (Data datum : getData()) {
            if (datum.getNode() != null) {
                datum.getNode().setScaleX(1.0);
                datum.getNode().setScaleY(1.0);
                datum.getNode().setOpacity(1.0);
            }
        }
        item.setStyle("-fx-background-color: lightgray; -fx-cursor: hand;");
        if (d.getNode() != null) {
            d.getNode().setScaleX(1.1);
            d.getNode().setScaleY(1.1);
            d.getNode().setOpacity(0.8);
        }
    }

    private void resetSegment(Data d, HBox item) {
        item.setStyle("-fx-background-color: transparent;");
        if (d.getNode() != null) {
            d.getNode().setScaleX(1.0);
            d.getNode().setScaleY(1.0);
            d.getNode().setOpacity(1.0);
        }
    }


    private void generateColorPalette(int size) {
        //erstellt eine individuelle Farbpalette für die Diagramme

        colorPalette.clear();
        for (int i = 0; i < size; i++) {
            double hue = 360.0 * i / size;
            Color color = Color.hsb(hue, 0.7, 0.9);
            colorPalette.add(color);
        }
    }

    private void applyCustomColors() {
        //setzt die individuellen Farben für die einzelnen Segmente des Charts

        ObservableList<Data> dataList = getData();
        for (int i = 0; i < dataList.size(); i++) {
            Data data = dataList.get(i);
            Color color = colorPalette.get(i);
            String colorStr = toHexString(color);
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-pie-color: " + colorStr + ";");
            }
        }
    }

    private String toHexString(Color color) {
        //wandelt die Farbwerte eines Color-Objekts in Hex-Werte um

        return String.format("#%02X%02X%02X",
                (int)(color.getRed()*255),
                (int)(color.getGreen()*255),
                (int)(color.getBlue()*255));
    }

    protected boolean showAlert() {
        //Zeigt Fehlermeldung an, wenn Daten beim Seitenwechsel verloren gehen würden

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Achtung!");
        alert.setHeaderText(null);
        alert.setContentText("Es werden alle zugehörigen Aufgaben gelöscht.");

        boolean[] result = new boolean[1];

        //Überprüft, welche Schaltfläche der Benutzer gedrückt hat
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                result[0] = true;
            }
        });

        return result[0];
    }
}

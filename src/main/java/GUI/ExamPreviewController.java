package GUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class ExamPreviewController extends SceneController {
//ToDo: Zweiter grüner Balken unten einfügen
//ToDo: Musterklausur im unteren Feld anzeigen
    //ToDo: Beim zurück gehen Informationen wieder zurück geben

    @FXML
    private MenuButton menuBar;


    @FXML
    private VBox pdfContainerExam;
    @FXML private VBox pdfContainerSampleSolution;

    private String pdfName;

    @FXML
    public void initialize() {
        //Listener setzen, um die Breite der PDF-Vorschau an die Fenstergröße anzupassen

        pdfContainerExam.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustImageViewWidths(pdfContainerExam, newWidth.doubleValue()));
        pdfContainerSampleSolution.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustImageViewWidths(pdfContainerSampleSolution, newWidth.doubleValue()));
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName.endsWith(".pdf") ? pdfName : pdfName + ".pdf";
    }


    public void loadPDFs() {
        //Lädt die PDF-Dateien und zeigt sie in den beiden Containern an

        File pdfFile = new File(pdfName);

        showPDFInVBox(pdfContainerExam, pdfFile);
        showPDFInVBox(pdfContainerSampleSolution, pdfFile);
    }

    private void adjustImageViewWidths(VBox container, double newWidth) {
        //Passt die Breite aller ImageViews in einem VBox-Container dynamisch an

        for (var node : container.getChildren()) {
            if (node instanceof ImageView imageView) {
                imageView.setFitWidth(newWidth);
            }
        }
    }

    private void showPDFInVBox(VBox container, File pdfFile) {
        //Lädt eine PDF-Datei, rendert sie in Bilder (eine Seite = ein Bild), und fügt sie in VBox-Container ein

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            container.getChildren().clear();
            int pageCount = document.getNumberOfPages();

            for (int page = 0; page < pageCount; page++) {
                var bim = pdfRenderer.renderImageWithDPI(page, 150);
                var fxImage = SwingFXUtils.toFXImage(bim, null);
                var imageView = new ImageView(fxImage);
                imageView.setPreserveRatio(true);
                container.getChildren().add(imageView);
            }
        } catch (IOException ignored) {}
    }

    @FXML
    public void exportExam(ActionEvent event) throws SQLException, IOException {
        //ToDo: Klausur exportieren
        File sourcePdfFile = new File(pdfName);
        String userHome = System.getProperty("user.home");
        Path downloadsPath = Paths.get(userHome, "Downloads");

        Path destinationPath = downloadsPath.resolve(sourcePdfFile.getName());

        try{
            Files.copy(sourcePdfFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ignores) {}

        super.switchToStartPage(event);
    }

    @FXML
    public void regenerateExam(ActionEvent event) throws IOException {
        //ToDo: Daten wieder zurück geben an ExamController zu Klausur

        super.switchToExamPage(event);
    }
    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Startseite

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }
    }
    @FXML
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Seite zum Aufgaben hinzufügen
        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToAddTaskPage(stage);
        }
    }

    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        //Wechsel mit Warnung zur Aufgabenübersicht
        if(showAlert()){
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToTaskOverview(stage);
        }
    }

    @FXML
    public void switchToExamOverview() throws IOException{
        //Wechsel mit Warnung zur Klausurübersicht

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamCollection(stage);
        }
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Seite zum Klausur erstellen

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamPage(stage);
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        //Abmelden mit Warnung

        if(showAlert()) {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.logout(stage);
        }
    }
}

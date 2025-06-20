package GUI;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.example.domain.ExamService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public class ExamPreviewController extends SceneController {

    @FXML
    private MenuButton menuBar;


    @FXML
    private VBox pdfContainerExam;
    @FXML
    private VBox pdfContainerSampleSolution;

    private String pdfName;
    private String pdfNameSolution;
    private ExamService exam;
    @FXML
    public void initialize() {
        //Listener setzen, um die Breite der PDF-Vorschau an die Fenstergröße anzupassen

        pdfContainerExam.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustImageViewWidths(pdfContainerExam, newWidth.doubleValue()));
        pdfContainerSampleSolution.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustImageViewWidths(pdfContainerSampleSolution, newWidth.doubleValue()));
    }

    public void setExamParams(ExamService exam) {
        this.exam = exam;
        setPdfName(exam.getName());
        setPdfNameSolution(exam.getName());

    }

    public void setPdfName(String pdfName) {
        if (!pdfName.endsWith(".pdf")) {
            pdfName += ".pdf";
        }
        this.pdfName = "target/GeneratedExams/" + pdfName;
    }

    public void setPdfNameSolution(String pdfName) {
        if (!pdfName.endsWith("_Loesung.pdf")) {
            pdfName += "_Loesung.pdf";
        }
        this.pdfNameSolution = "target/GeneratedExams/Loesungen/" + pdfName;
    }


    public void loadPDFs() {
        //Lädt die PDF-Dateien und zeigt sie in den beiden Containern an

        File pdfFile = new File(pdfName);
        File pdfFileSolution = new File(pdfNameSolution);

        showPDFInVBox(pdfContainerExam, pdfFile);
        showPDFInVBox(pdfContainerSampleSolution, pdfFileSolution);
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
        //Exportiert erstelle Klausur in den Download-Ordner des Nutzers

        File sourcePdfFile = new File(pdfName);
        File sourcePdfFileSolution = new File(pdfNameSolution);
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

        super.switchToStartPage(event);
    }

    private void deleteExam() {
        //löscht, das erstelle Pdf

        File pdfFile = new File(pdfName);
        File pdfFileSolution = new File(pdfNameSolution);
        if (pdfFile.exists()) {
            pdfFile.delete();
            pdfFileSolution.delete();
        }
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

    @FXML
    public void regenerateExam(ActionEvent event) throws IOException {
        //Methode die zur ExamPage zurückgeht und die Parameter der Klausur setzt

        deleteExam();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ExamPage.fxml"));
        Parent root = loader.load();

        ExamController controller = loader.getController();
        controller.setExamParams(exam);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double sceneWidth = stage.getScene().getWidth();
        double sceneHeight = stage.getScene().getHeight();

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Startseite

        if(showAlert()) {
            deleteExam();
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToStartPage(stage);
        }
    }
    @FXML
    public void switchToAddTaskPage(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Seite zum Aufgaben hinzufügen
        if(showAlert()) {
            deleteExam();
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToAddTaskPage(stage);
        }
    }

    @FXML
    public void switchToTaskOverview(ActionEvent event) throws IOException{
        //Wechsel mit Warnung zur Aufgabenübersicht
        if(showAlert()){
            deleteExam();
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToTaskOverview(stage);
        }
    }

    @FXML
    public void switchToExamOverview() throws IOException{
        //Wechsel mit Warnung zur Klausurübersicht
        if(showAlert()) {
            deleteExam();
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamCollection(stage);
        }
    }

    @FXML
    public void switchToExamPage(ActionEvent event) throws IOException {
        //Wechsel mit Warnung zur Seite zum Klausur erstellen
        if(showAlert()) {
            deleteExam();
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.switchToExamPage(stage);
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        //Abmelden mit Warnung
        if(showAlert()) {
            deleteExam();
            Stage stage = (Stage) menuBar.getScene().getWindow();
            super.logout(stage);
        }
    }
}

package GUI;

import javafx.scene.control.TextArea;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class AssignControllerTest {

    static {
        new JFXPanel();
    }

    private AssignController assignController;

    @BeforeEach
    void setUp() throws Exception {
        assignController = new AssignController();

        setPrivateField("textAreaQuestion", new TextArea());
        setPrivateField("answer1TextArea", new TextArea());
        setPrivateField("answer2TextArea", new TextArea());
        setPrivateField("solution1TextArea", new TextArea());
        setPrivateField("solution2TextArea", new TextArea());
        setPrivateField("answerContainer", new VBox());

        VBox answerContainer = getPrivateField("answerContainer", VBox.class);
        TextArea answer1TextArea = getPrivateField("answer1TextArea", TextArea.class);
        TextArea solution1TextArea = getPrivateField("solution1TextArea", TextArea.class);
        TextArea answer2TextArea = getPrivateField("answer2TextArea", TextArea.class);
        TextArea solution2TextArea = getPrivateField("solution2TextArea", TextArea.class);

        HBox hBox1 = new HBox(answer1TextArea, solution1TextArea);
        HBox hBox2 = new HBox(answer2TextArea, solution2TextArea);
        answerContainer.getChildren().addAll(hBox1, hBox2);




        assignController.initialize();
    }

    @Test
    void setAufgabe() {
        AufgabeService testTask = new AufgabeService();
        AssignController assignController = new AssignController();
        assignController.setAufgabe(testTask);
        assert(assignController.getAufgabe() == testTask);
    }

    @Test
    void initialize() throws Exception {
        assertEquals(2, getPrivateList("answerAreas").size());
        TextArea answer1 = getPrivateField("answer1TextArea", TextArea.class);
        assertTrue(getPrivateList("answerAreas").contains(answer1));
        TextArea answer2 = getPrivateField("answer2TextArea", TextArea.class);
        assertTrue(getPrivateList("answerAreas").contains(answer2));

        assertEquals(2, getPrivateList("solutionAreas").size());
        TextArea solution1 = getPrivateField("solution1TextArea", TextArea.class);
        assertTrue(getPrivateList("solutionAreas").contains(solution1));
        TextArea solution2 = getPrivateField("solution2TextArea", TextArea.class);
        assertTrue(getPrivateList("solutionAreas").contains(solution2));
    }

    @Test
    void addAnswerAndSolutionAreas() throws Exception {
        assignController.addAnswerAreas();
        assignController.addAnswerAreas();

        assertEquals(4, getPrivateList("answerAreas").size());
        assertEquals(4, getPrivateList("solutionAreas").size());
        assert(getPrivateList("solutionAreas").size() == getPrivateList("answerAreas").size());
    }

    @Test
    void removeAnswerAndSolutionAreas() throws Exception {
        assignController.removeLastAnswerField();
        assignController.removeLastAnswerField();
        assignController.removeLastAnswerField(); // sollte keine Exception werfen
        assertEquals(2, getPrivateList("answerAreas").size());
    }

    @Test
    void setEditMode() throws Exception{

        Frage frage = new Frage(1, "Testname", "Testfrage", 12, AntwortType.geschlosseneAntwort, 12, BloomLevel.analysieren);
        List<Antwort> answers = List.of( new Antwort(1, QuestionType.zuordnung, "Antwort 1", "Lösung 1", AntwortType.geschlosseneAntwort),
                                         new Antwort(2, QuestionType.zuordnung, "Antwort 2", "Lösung 2", AntwortType.geschlosseneAntwort),
                                         new Antwort(3, QuestionType.zuordnung, "Antwort 3", "Lösung 3", AntwortType.geschlosseneAntwort)

                );
        Modul modul = new Modul(1, "Testmodul");

        Task testTask = new Task(frage, answers, modul);
        assignController.setEditMode(testTask);
        Field editModeField = AssignController.class.getDeclaredField("editMode");
        editModeField.setAccessible(true);
        assertTrue((boolean) editModeField.get(assignController));

        Field selectedTaskField = AssignController.class.getDeclaredField("selectedTask");
        selectedTaskField.setAccessible(true);
        assertEquals(testTask, selectedTaskField.get(assignController));

        TextArea textAreaQuestion = getPrivateField("textAreaQuestion", TextArea.class);
        assertEquals("Testfrage", textAreaQuestion.getText());

        List<TextArea> answerAreas = getPrivateList("answerAreas");
        List<TextArea> solutionAreas = getPrivateList("solutionAreas");

        assertEquals(3, answerAreas.size());
        assertEquals(3, solutionAreas.size());

        assertEquals("Antwort 1", answerAreas.get(0).getText());
        assertEquals("Antwort 2", answerAreas.get(1).getText());
        assertEquals("Antwort 3", answerAreas.get(2).getText());

        assertEquals("Lösung 1", solutionAreas.get(0).getText());
        assertEquals("Lösung 2", solutionAreas.get(1).getText());
        assertEquals("Lösung 3", solutionAreas.get(2).getText());
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = AssignController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(assignController, value);
    }

    @SuppressWarnings("unchecked")
    private List<TextArea> getPrivateList(String fieldName) throws Exception {
        Field field = AssignController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (List<TextArea>) field.get(assignController);
    }

    private <T> T getPrivateField(String fieldName, Class<T> tClass) throws Exception {
        Field field = AssignController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return tClass.cast(field.get(assignController));
    }

}
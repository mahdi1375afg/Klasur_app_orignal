package GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.io.IOException;

public class LoginController extends SceneController {
    @FXML
    private TextField usernameLoginTextField;

    @FXML
    private PasswordField passwordLoginField;

    private String username;
    private String password;

    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException {

        username = usernameLoginTextField.getText().trim();
        password = passwordLoginField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Fehler", "Bitte alle Felder ausfüllen.");
            return;
        }



        System.out.println("Benutzername: " + username);
        System.out.println("Passwort: " + password);

        super.switchToStartPage(event);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
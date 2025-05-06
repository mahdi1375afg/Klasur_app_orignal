package GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class SignUpController  extends SceneController {
    @FXML
    private TextField usernameSignUpTextField;

    @FXML
    private PasswordField passwordSignUp1Field;

    @FXML
    private PasswordField passwordSignUp2Field;

    private String username;
    private String password1;
    private String password2;

    @FXML
    public void switchToLoginPage(ActionEvent event) throws IOException {
        //ToDo: Daten an Anwendungsschicht senden

        username = usernameSignUpTextField.getText().trim();
        password1 = passwordSignUp1Field.getText();
        password2 = passwordSignUp2Field.getText();

        if (username.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            showAlert("Bitte alle Felder ausfüllen.");
            return;
        }

        if (!password1.equals(password2)) {
            showAlert("Passwörter stimmen nicht überein.");
            return;
        }

        if (password1.length() < 6) {
            showAlert("Passwort muss mindestens 6 Zeichen lang sein.");
            return;
        }

        System.out.println("Benutzername: " + username);
        System.out.println("Passwort: " + password1);

        super.switchToLoginPage(event);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

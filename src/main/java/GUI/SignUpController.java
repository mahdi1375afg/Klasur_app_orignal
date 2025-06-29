package GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.domain.UserAccount;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController  extends SceneController {
    @FXML
    private TextField usernameSignUpTextField;

    @FXML
    private PasswordField passwordSignUpField;

    @FXML
    private PasswordField passwordSignUpVerifyField;

    @FXML
    public void switchToLoginPage(ActionEvent event) throws IOException, SQLException {
        //registriert den neuen User bei der DB und wechselt bein erfolgreichem registrieren
        //zur Anmeldeseite

        String username = usernameSignUpTextField.getText().trim();
        String password = passwordSignUpField.getText();
        String passwordVerify = passwordSignUpVerifyField.getText();

        if (username.isEmpty() || password.isEmpty() || passwordVerify.isEmpty()) {
            showAlert("Bitte alle Felder ausfüllen.");
            return;
        }

        if (!password.equals(passwordVerify)) {
            showAlert("Passwörter stimmen nicht überein.");
            return;
        }

        if (password.length() < 6) {
            showAlert("Passwort muss mindestens 6 Zeichen lang sein.");
            return;
        }

        UserAccount konto = new UserAccount();

        if(!konto.register(username, password)){
            showAlert("Der Benutzername ist schon vergeben. Bitte wählen Sie einen anderen Benutzernamen. ");
            return;
        }

        super.switchToLoginPage(event);
    }
}

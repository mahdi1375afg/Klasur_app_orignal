package GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.domain.benutzerKonto;

import java.io.IOException;
import java.sql.SQLException;

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
    public void switchToLoginPage(ActionEvent event) throws IOException, SQLException {
        //registriert den neuen User bei der DB und wechselt bein erfolgreichem registrieren
        //zur LoginPage

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

        benutzerKonto konto = new benutzerKonto();

        if(!konto.register(username,password1)){
            showAlert("Der Benutzername ist schon vergeben. Bitte wählen Sie einen anderen Benutzernamen. ");
            return;
        }

        super.switchToLoginPage(event);
    }

    public void switchToTitlePage(ActionEvent event) throws IOException {
        super.switchToTitlePage(event);
    }
}

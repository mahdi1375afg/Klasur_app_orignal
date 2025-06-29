package GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.domain.UserAccount;


import java.io.IOException;
import java.sql.SQLException;

public class LoginController extends SceneController {
    @FXML
    private TextField usernameLoginTextField;

    @FXML
    private PasswordField passwordLoginField;

    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException, SQLException {
        //Anmelden des Users mit Laden der Daten aus der Datenbank

        String username = usernameLoginTextField.getText().trim();
        String password = passwordLoginField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert( "Bitte alle Felder ausfüllen.");
            return;
        }

        UserAccount konto = new UserAccount();
        int signIn = konto.login(username, password);

        if(signIn == 1) {
            showAlert("Falsches Passwort");
            return;
        }
        else if(signIn == 2) {
            showAlert("Benutzername existiert nicht");
        }
        try {
            if(UserAccount.aktuellerBenutzer != null){
                super.switchToStartPage(event);
            }
        } catch (NullPointerException e) {
            super.switchToTitlePage(event);
        }
    }
}
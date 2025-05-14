package GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.domain.benutzerKonto;


import java.io.IOException;
import java.sql.SQLException;

public class LoginController extends SceneController {
    @FXML
    private TextField usernameLoginTextField;

    @FXML
    private PasswordField passwordLoginField;

    private String username;
    private String password;

    @FXML
    public void switchToStartPage(ActionEvent event) throws IOException, SQLException {
        username = usernameLoginTextField.getText().trim();
        password = passwordLoginField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Fehler", "Bitte alle Felder ausfüllen.");
            return;
        }

        benutzerKonto konto = new benutzerKonto();
        int signIn = konto.anmelden(username,password);

        if(signIn == 1) {
            showAlert("Falsches Passwort");
            return;
        }
        else if(signIn == 2) {
            showAlert("Benutzername existiert nicht");
        }
        try {
            //konto.aktuellerBenutzer.printNutzer();
            if(konto.aktuellerBenutzer != null){
                super.switchToStartPage(event);
            }
        } catch (NullPointerException e) {
            super.switchToTitlePage(event);
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
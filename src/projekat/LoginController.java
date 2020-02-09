package projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class LoginController {
    //TODO negdje cuvati login informacije
    private String korisnickoIme = "emir";
    private String sifra = "test";

    public TextField korisnickoImeField;
    public TextField sifraField;

    public LoginController() {
    }

    public void prijaviSeAction(ActionEvent actionEvent){
        boolean greska = false;

        if(korisnickoIme.equals(korisnickoImeField.getText())){
            korisnickoImeField.getStyleClass().removeAll("poljeNeispravno");
            korisnickoImeField.getStyleClass().add("poljeIspravno");
        }else{
            greska = true;
            korisnickoImeField.getStyleClass().removeAll("poljeIspravno");
            korisnickoImeField.getStyleClass().add("poljeNeispravno");
        }

        if(sifra.equals(sifraField.getText())){
            sifraField.getStyleClass().removeAll("poljeNeispravno");
            sifraField.getStyleClass().add("poljeIspravno");
        }else{
            greska = true;
            sifraField.getStyleClass().removeAll("poljeIspravno");
            sifraField.getStyleClass().add("poljeNeispravno");
        }

        if(!greska){
            zatvoriLogin();
            otvoriGlavniProzor();
        }
    }

    public void odustaniAction(ActionEvent actionEvent){
        System.exit(0);
    }

    private void zatvoriLogin() {
        Stage stage = (Stage) korisnickoImeField.getScene().getWindow();
        stage.close();
    }

    private void otvoriGlavniProzor() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavni.fxml"));
            GlavniController ctrl = new GlavniController();
            loader.setController(ctrl);
            Parent root = null;
            root = loader.load();
            stage.setTitle("Udruženje");
            stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

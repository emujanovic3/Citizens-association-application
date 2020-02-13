package projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class LoginController {
    //TODO negdje cuvati login informacije
    private String korisnickoIme;
    private String sifra;

    public TextField korisnickoImeField;
    public TextField sifraField;

    public LoginController() {
        Scanner ulaz;
        try {
            ulaz = new Scanner(new FileReader("osnivacki_akt.txt"));
            String predzadnja = ulaz.nextLine();
            String zadnja = ulaz.nextLine();

            while(ulaz.hasNextLine()){
                predzadnja = zadnja;
                zadnja = ulaz.nextLine();
            }

            korisnickoIme = predzadnja;
            sifra = zadnja;

            ulaz.close();
        } catch (FileNotFoundException e) {
            System.out.println("Datoteka osnivacki_akt.txt ne postoji ili se ne može otvoriti");
        }

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
            Parent root = loader.load();
            stage.setTitle("Udruženje");
            stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

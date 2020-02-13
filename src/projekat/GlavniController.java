package projekat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class GlavniController {
    public Label nazivLabel;
    private String naziv;

    public GlavniController() {
        naziv = ucitajNaziv();
    }

    @FXML
    public void initialize(){
        nazivLabel.setText("\"" + naziv +"\"");
    }

    private String ucitajNaziv(){
        Scanner ulaz;
        try {
            ulaz = new Scanner(new FileReader("osnivacki_akt.txt"));
            String naziv = ulaz.nextLine();
            ulaz.close();
            return naziv;
        } catch (FileNotFoundException e) {
            System.out.println("Datoteka osnivacki_akt.txt ne postoji ili se ne može otvoriti");
            return null;
        }
    }
}

package projekat;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

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
            otvoriGlavniProzor();
        }
    }

    public void odustaniAction(ActionEvent actionEvent){
        System.exit(0);
    }

    private void otvoriGlavniProzor() {

    }
}

package projekat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ClanController {
    public TextField imeClanField;
    public TextField prezimeClanField;
    public TextField datumRodjenjaClanField;
    public TextField adresaClanField;
    public TextField gradClanField;
    public TextField drzavaClanField;
    public RadioButton bihClanRadioBtn;
    public RadioButton drugoClanRadioBtn;

    private ToggleGroup grupa = new ToggleGroup();
    private Clan novi = null;

    @FXML
    public void initialize(){
        bihClanRadioBtn.setToggleGroup(grupa);
        drugoClanRadioBtn.setToggleGroup(grupa);

        bihClanRadioBtn.setSelected(true);
    }

    public void dodajClanAction(ActionEvent actionEvent){
        boolean sveOk = true;

        if(!(imeClanField.getText().isEmpty())){;
            imeClanField.getStyleClass().removeAll("poljeNeispravno");
            imeClanField.getStyleClass().add("poljeIspravno");
        }else {
            imeClanField.getStyleClass().removeAll("poljeIspravno");
            imeClanField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(prezimeClanField.getText().isEmpty())){;
            prezimeClanField.getStyleClass().removeAll("poljeNeispravno");
            prezimeClanField.getStyleClass().add("poljeIspravno");
        }else{
            prezimeClanField.getStyleClass().removeAll("poljeIspravno");
            prezimeClanField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(datumRodjenjaClanField.getText().isEmpty())){;
            datumRodjenjaClanField.getStyleClass().removeAll("poljeNeispravno");
            datumRodjenjaClanField.getStyleClass().add("poljeIspravno");
        }else{
            datumRodjenjaClanField.getStyleClass().removeAll("poljeIspravno");
            datumRodjenjaClanField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(adresaClanField.getText().isEmpty())){;
            adresaClanField.getStyleClass().removeAll("poljeNeispravno");
            adresaClanField.getStyleClass().add("poljeIspravno");
        }else{
            adresaClanField.getStyleClass().removeAll("poljeIspravno");
            adresaClanField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(gradClanField.getText().isEmpty())){;
            gradClanField.getStyleClass().removeAll("poljeNeispravno");
            gradClanField.getStyleClass().add("poljeIspravno");
        }else{
            gradClanField.getStyleClass().removeAll("poljeIspravno");
            gradClanField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(drzavaClanField.getText().isEmpty())){;
            drzavaClanField.getStyleClass().removeAll("poljeNeispravno");
            drzavaClanField.getStyleClass().add("poljeIspravno");
        }else{
            drzavaClanField.getStyleClass().removeAll("poljeIspravno");
            drzavaClanField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(sveOk){
            novi = new Clan(0, imeClanField.getText(),prezimeClanField.getText(),dajDatumIzTeksta(datumRodjenjaClanField.getText()),
                    new Prebivaliste(adresaClanField.getText(),gradClanField.getText(),drzavaClanField.getText()),Drzavljanstvo.BIH);

            if(((RadioButton)grupa.getSelectedToggle()).getText().equals("Drugo")){
                novi.setDrzavljanstvo(Drzavljanstvo.STRANO);
            }

            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
    }

    public void odustaniClanAction(ActionEvent actionEvent){
        novi = null;
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public Clan getClan(){
        return novi;
    }

    private LocalDate dajDatumIzTeksta(String text){
        String[] odvojeno = text.split(".");
        LocalDate datum = LocalDate.of(Integer.parseInt(odvojeno[2]),Integer.parseInt(odvojeno[1]),Integer.parseInt(odvojeno[0]));
        return datum;
    }
}

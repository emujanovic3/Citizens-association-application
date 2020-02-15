package projekat;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class DetaljiController {
    public Label nazivLbl;
    public Label vodjaLbl;
    public Label opisLbl;

    private Projekat projekat = null;

    public DetaljiController(Projekat p){
        projekat = p;
    }

    public void initialize(){
        nazivLbl.setText(projekat.getNaziv());
        if(projekat.getVodja()==null){
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            vodjaLbl.setText(bundle.getString("nikoNeVodi"));
        }else{
            vodjaLbl.setText(projekat.getVodja().getIme() + " " + projekat.getVodja().getPrezime());
        }
        opisLbl.setText(projekat.getOpis());
    }

    public void okAction(ActionEvent actionEvent){
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
}

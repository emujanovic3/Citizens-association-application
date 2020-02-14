package projekat;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
            vodjaLbl.setText("Niko ne vodi ovaj projekat");
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

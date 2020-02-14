package projekat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProjekatController {
    public TextField nazivProjekat;
    public ChoiceBox<Clan> projekatChoiceBox;
    public TextArea opisProjekat;

    private Projekat p = null;
    private ObservableList<Clan> clanovi;

    public ProjekatController(ObservableList<Clan> clanovi) {
        this.clanovi = clanovi;
    }

    @FXML
    public void initialize(){
        projekatChoiceBox.setItems(clanovi);
    }

    public void dodajAction(ActionEvent actionEvent){
        boolean sveOk = true;

        if(!(nazivProjekat.getText().isEmpty())){;
            nazivProjekat.getStyleClass().removeAll("poljeNeispravno");
            nazivProjekat.getStyleClass().add("poljeIspravno");
        }else {
            nazivProjekat.getStyleClass().removeAll("poljeIspravno");
            nazivProjekat.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(projekatChoiceBox.getSelectionModel().getSelectedItem()==null){
            projekatChoiceBox.getStyleClass().removeAll("poljeIspravno");
            projekatChoiceBox.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }else{
            projekatChoiceBox.getStyleClass().removeAll("poljeNeispravno");
            projekatChoiceBox.getStyleClass().add("poljeIspravno");
        }

        if(!(opisProjekat.getText().isEmpty())){;
            opisProjekat.getStyleClass().removeAll("poljeNeispravno");
            opisProjekat.getStyleClass().add("poljeIspravno");
        }else {
            opisProjekat.getStyleClass().removeAll("poljeIspravno");
            opisProjekat.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(sveOk){
            p = new Projekat(0,nazivProjekat.getText(),projekatChoiceBox.getSelectionModel().getSelectedItem(),opisProjekat.getText());

            Node n = (Node) actionEvent.getSource();
            Stage stage = (Stage) n.getScene().getWindow();
            stage.close();
        }
    }

    public void odustaniAction(ActionEvent actionEvent){
        p = null;

        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public Projekat getProjekat(){
        return p;
    }
}

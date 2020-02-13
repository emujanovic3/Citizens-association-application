package projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class SkupstinaController {
    public ListView<Clan> skupstinaListView;

    private UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
    private ObservableList<Clan> skupstina = FXCollections.observableArrayList(dao.dajSkupstinu());

    @FXML
    public void initialize(){
        skupstinaListView.setItems(skupstina);
    }

    public void okAction(ActionEvent actionEvent){
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
}

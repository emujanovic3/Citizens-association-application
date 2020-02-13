package projekat;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class PocetniController {
    public TableView<Clan> osnivaciTableView;
    public TableColumn<Clan,String> imeColumn;
    public TableColumn<Clan,String> prezimeColumn;
    public TableColumn<Clan,String> datumRodjenjaColumn;
    public TableColumn<Clan,String> drzavljanstvoColumn;

    private ArrayList<Clan> osnivaci = new ArrayList<>();

    @FXML
    public void initialize(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        imeColumn.setCellValueFactory(new PropertyValueFactory<>("Ime"));
        prezimeColumn.setCellValueFactory(new PropertyValueFactory<>("Prezime"));
        datumRodjenjaColumn.setCellValueFactory(new PropertyValueFactory<>("DatumRodjenja"));
        drzavljanstvoColumn.setCellValueFactory(new PropertyValueFactory<>("Drzavljanstvo"));
        osnivaciTableView.setItems(FXCollections.observableArrayList(osnivaci));
        osnivaciTableView.setPlaceholder(new Label(""));
    }

    public void dodajOsnivacaAction(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clan.fxml"));
            ClanController ctrl = new ClanController();
            loader.setController(ctrl);
            Parent root = loader.load();
            stage.setTitle("Dodaj osnivača");
            stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            stage.show();
            stage.setOnHiding((event) -> {
                Clan novi = ctrl.getClan();
                if(novi!=null){
                    novi.setId(osnivaci.size()+1);
                    osnivaci.add(novi);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void odustaniPocetniAction(ActionEvent actionEvent){

        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
}

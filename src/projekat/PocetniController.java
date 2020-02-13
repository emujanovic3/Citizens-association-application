package projekat;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class PocetniController {
    public TableView<Clan> osnivaciTableView;
    public TableColumn<Clan,String> imeColumn;
    public TableColumn<Clan,String> prezimeColumn;
    public TableColumn<Clan,String> datumRodjenjaColumn;
    public TableColumn<Clan,String> drzavljanstvoColumn;

    public TextField nazivField;
    public TextField sjedisteField;
    public TextField adresaField;
    public TextField kImeField;
    public TextField sifraField;

    private ArrayList<Clan> osnivaci = new ArrayList<>();

    @FXML
    public void initialize(){
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
                    osnivaciTableView.setItems(FXCollections.observableArrayList(osnivaci));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void brisiOsnivacaAction(ActionEvent actionEvent){
        Clan zaBrisanje = osnivaciTableView.getSelectionModel().getSelectedItem();

        if(zaBrisanje!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda brisanja osnivača:");
            alert.setHeaderText("Obrisat ćete odabranog osnivača");
            alert.setContentText("Da li ste sigurni?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                osnivaciTableView.getItems().remove(zaBrisanje);
                osnivaci.remove(zaBrisanje);
            }
        }
    }

    public void napraviAction(ActionEvent actionEvent){
        boolean sveOk = true;

        if(!(nazivField.getText().isEmpty())){;
            nazivField.getStyleClass().removeAll("poljeNeispravno");
            nazivField.getStyleClass().add("poljeIspravno");
        }else {
            nazivField.getStyleClass().removeAll("poljeIspravno");
            nazivField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(sjedisteField.getText().isEmpty())){;
            sjedisteField.getStyleClass().removeAll("poljeNeispravno");
            sjedisteField.getStyleClass().add("poljeIspravno");
        }else {
            sjedisteField.getStyleClass().removeAll("poljeIspravno");
            sjedisteField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(adresaField.getText().isEmpty())){;
            adresaField.getStyleClass().removeAll("poljeNeispravno");
            adresaField.getStyleClass().add("poljeIspravno");
        }else {
            adresaField.getStyleClass().removeAll("poljeIspravno");
            adresaField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(kImeField.getText().isEmpty())){;
            kImeField.getStyleClass().removeAll("poljeNeispravno");
            kImeField.getStyleClass().add("poljeIspravno");
        }else {
            kImeField.getStyleClass().removeAll("poljeIspravno");
            kImeField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(!(sifraField.getText().isEmpty())){;
            sifraField.getStyleClass().removeAll("poljeNeispravno");
            sifraField.getStyleClass().add("poljeIspravno");
        }else {
            sifraField.getStyleClass().removeAll("poljeIspravno");
            sifraField.getStyleClass().add("poljeNeispravno");
            sveOk = false;
        }

        if(sveOk){

                try {
                    if(osnivaci.size()<3) {
                        throw new NeispravniOsnivaciException("Potrebna su najmanje 3 osnivača!");
                    }

                    boolean baremJedanBIH = false;

                    for(Clan x : osnivaci){
                        LocalDate now = LocalDate.now();

                        if(Period.between(now,x.getDatumRodjenja()).getYears()<18){
                            throw new NeispravniOsnivaciException("Svi osnivači moraju imati najmanje 18 godina!");
                        }

                        if(x.getDrzavljanstvo().equals(Drzavljanstvo.BIH)){
                            baremJedanBIH = true;
                        }
                    }

                    if(baremJedanBIH==false){
                        throw new NeispravniOsnivaciException("Barem jedan osnivač mora imati državljanstvo BIH!");
                    }
                } catch (NeispravniOsnivaciException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Greška");
                    alert.setHeaderText("Došlo je do greške!");
                    alert.setContentText(e.getMessage());

                    alert.showAndWait();
                }
        }
    }

    public void odustaniPocetniAction(ActionEvent actionEvent){

        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();

    }
}

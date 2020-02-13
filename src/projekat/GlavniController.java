package projekat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class GlavniController {
    public Label nazivLabel;
    public TableView<Clan> clanoviTableView;
    public TableColumn<Clan,String> imeCol;
    public TableColumn<Clan,String> prezimeCol;

    private UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
    private String naziv;
    private ObservableList<Clan> clanovi = FXCollections.observableArrayList(dao.dajSveClanove());

    public GlavniController() {
        naziv = ucitajNaziv();
    }

    @FXML
    public void initialize(){
        nazivLabel.setText("\"" + naziv +"\"");
        imeCol.setCellValueFactory(new PropertyValueFactory<>("Ime"));
        prezimeCol.setCellValueFactory(new PropertyValueFactory<>("Prezime"));
        clanoviTableView.setItems(clanovi);
    }

    public void dodajClanaAction(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clan.fxml"));
            ClanController ctrl = new ClanController();
            loader.setController(ctrl);
            Parent root = loader.load();
            stage.setTitle("Dodaj člana");
            stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            stage.show();
            stage.setOnHiding((event) -> {
                Clan novi = ctrl.getClan();
                if(novi!=null){
                    novi.setId(clanovi.size()+1);
                    clanovi.add(novi);
                    dao.dodajClana(novi);
                    clanoviTableView.setItems(FXCollections.observableArrayList(clanovi));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obrisiClanaAction(ActionEvent actionEvent){
        Clan zaBrisanje = clanoviTableView.getSelectionModel().getSelectedItem();

        if(zaBrisanje!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda brisanja osnivača:");
            alert.setHeaderText("Obrisat ćete odabranog osnivača");
            alert.setContentText("Da li ste sigurni?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                clanoviTableView.getItems().remove(zaBrisanje);
                clanovi.remove(zaBrisanje);
                dao.obrisiClana(zaBrisanje.getId());
            }
        }
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

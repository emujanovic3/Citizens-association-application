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

    public void skupstinaAction(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/skupstina.fxml"));
            SkupstinaController ctrl = new SkupstinaController();
            loader.setController(ctrl);
            Parent root = loader.load();
            stage.setTitle("Skupština");
            stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dodajSkupstinaAction(ActionEvent actionEvent){
        Clan clan = clanoviTableView.getSelectionModel().getSelectedItem();

        if(clan instanceof Predsjednik){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Izabrana osoba je predsjednik!");

            alert.showAndWait();
        }else if(clan==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Niste izabrali nijednog člana!");

            alert.showAndWait();
        }else{
            if(!(clan instanceof Skupstina)){
                Clan c = new Skupstina(clan);
                dao.promijeniClana(c);
                clanovi.remove(clan);
                clanovi.add(c);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Obavještenje");
                alert.setHeaderText(null);
                alert.setContentText("Izabrana osoba je sada u skupštini!");

                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Obavještenje");
                alert.setHeaderText(null);
                alert.setContentText("Izabrana osoba je već u skupštini!");

                alert.showAndWait();
            }
        }
    }

    public void izbaciSkupstinaAction(ActionEvent actionEvent){
        Clan clan = clanoviTableView.getSelectionModel().getSelectedItem();

        if(clan instanceof Predsjednik){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Izabrana osoba je predsjednik!");

            alert.showAndWait();
        }else if(clan==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Niste izabrali nijednog člana!");

            alert.showAndWait();
        }else{
            if(!(clan instanceof Skupstina)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Obavještenje");
                alert.setHeaderText(null);
                alert.setContentText("Izabrana osoba nije u skupštini!");

                alert.showAndWait();
            }else{
                Clan c = new Clan(clan);
                dao.promijeniClana(c);
                clanovi.remove(clan);
                clanovi.add(c);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Obavještenje");
                alert.setHeaderText(null);
                alert.setContentText("Izabrana osoba je izbačena iz skupštine!");

                alert.showAndWait();
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

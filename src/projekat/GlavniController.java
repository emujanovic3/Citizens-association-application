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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class GlavniController {
    public Label nazivLabel;
    public Label imePredsjednika;
    public TableView<Clan> clanoviTableView;
    public TableColumn<Clan,String> imeCol;
    public TableColumn<Clan,String> prezimeCol;
    public TableView<Projekat> projektiTableView;
    public TableColumn<Projekat,String> nazivCol;

    private UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
    private String naziv;
    private ObservableList<Clan> clanovi = FXCollections.observableArrayList(dao.dajSveClanove());
    private ObservableList<Projekat> projekti = FXCollections.observableArrayList(dao.dajSveProjekte());

    public GlavniController() {
        naziv = ucitajNaziv();
    }

    @FXML
    public void initialize(){
        nazivLabel.setText("\"" + naziv +"\"");
        for(Clan x: clanovi){
            if(x instanceof Predsjednik){
                imePredsjednika.setText(x.getIme() + " " + x.getPrezime());
                break;
            }
        }
        imeCol.setCellValueFactory(new PropertyValueFactory<>("Ime"));
        prezimeCol.setCellValueFactory(new PropertyValueFactory<>("Prezime"));
        clanoviTableView.setItems(clanovi);
        clanoviTableView.setPlaceholder(new Label("Trenutno nema članova"));
        nazivCol.setCellValueFactory((new PropertyValueFactory<>("Naziv")));
        projektiTableView.setItems(projekti);
        projektiTableView.setPlaceholder(new Label("Trenutno nema projekata"));
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

    public void promijeniPredsjednikaAction(ActionEvent actionEvent){
        List<Clan> choices = new ArrayList<>();
        Clan stariP = null;
        for(Clan x:clanovi){
            if(!(x instanceof Predsjednik)){
                choices.add(x);
            }else{
                stariP = x;
            }
        }

        ChoiceDialog<Clan> dialog = new ChoiceDialog<>(null, choices);
        dialog.setTitle("Promjena predsjednika");
        dialog.setHeaderText(null);
        dialog.setContentText("Izaberite novog predsjednika:");

// Traditional way to get the response value.
        Optional<Clan> result = dialog.showAndWait();
        if (result.isPresent()){
            Clan noviP = new Predsjednik(result.get());
            if(stariP!=null){
                Clan exPredsjednik = new Clan(stariP);
                dao.promijeniClana(exPredsjednik);
                clanovi.remove(stariP);
                clanovi.add(exPredsjednik);
            }

            //TODO ovdje se mozda moze nesto ubrzati

            clanovi.remove(result.get());
            clanovi.add(noviP);
            dao.promijeniClana(noviP);

            imePredsjednika.setText(noviP.getIme() + " " + noviP.getPrezime());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Izabrana osoba je sada predsjednik!");

            alert.showAndWait();
        }

// The Java 8 way to get the response value (with lambda expression).
        //result.ifPresent(letter -> System.out.println("Your choice: " + letter));
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

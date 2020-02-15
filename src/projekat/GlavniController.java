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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class GlavniController {
    public Label nazivLabel;
    public Label imePredsjednika;
    public TableView<Clan> clanoviTableView;
    public TableColumn<Clan,String> imeCol;
    public TableColumn<Clan,String> prezimeCol;
    public TableView<Projekat> projektiTableView;
    public TableColumn<Projekat,String> nazivCol;
    public ImageView slika;

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

        slika.setImage(new Image(ucitajSliku(),128,128,false,false));
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
                    novi.setId(dajIdClan(dao.dajSveClanove()));
                    clanovi.add(novi);
                    dao.dodajClana(novi);
                    clanoviTableView.setItems(clanovi);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obrisiClanaAction(ActionEvent actionEvent){
        Clan zaBrisanje = clanoviTableView.getSelectionModel().getSelectedItem();
        if(zaBrisanje instanceof Predsjednik){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Ne možete izbaciti predsjednika!");

            alert.showAndWait();
            return;
        }

        if(zaBrisanje!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda brisanja osnivača:");
            alert.setHeaderText("Obrisat ćete odabranog člana");
            alert.setContentText("Da li ste sigurni?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                ArrayList<Projekat> pr = dao.dajSveProjekte();
                for(Projekat x : pr){
                    if(x.getVodja().getId() == zaBrisanje.getId()){
                        x.setVodja(null);
                        dao.promijeniProjekat(x);
                    }
                }

                projekti = FXCollections.observableArrayList(pr);
                projektiTableView.setItems(projekti);

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

    public void dodajProjekatAction(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/projekat.fxml"));
            ProjekatController ctrl = new ProjekatController(clanovi);
            loader.setController(ctrl);
            Parent root = loader.load();
            stage.setTitle("Dodaj projekat");
            stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            stage.show();
            stage.setOnHiding((event) -> {
                Projekat novi = ctrl.getProjekat();
                if(novi!=null){
                    novi.setId(dajIdProjekat(dao.dajSveProjekte()));
                    projekti.add(novi);
                    dao.dodajProjekat(novi);
                    projektiTableView.setItems(FXCollections.observableArrayList(projekti));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obrisiProjekatAction(ActionEvent actionEvent){
        Projekat zaBrisanje = projektiTableView.getSelectionModel().getSelectedItem();

        if(zaBrisanje==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Niste izabrali nijedan projekat!");

            alert.showAndWait();
        }else{
            dao.obrisiProjekat(zaBrisanje);
            projekti.remove(zaBrisanje);
            projektiTableView.setItems(projekti);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Izabrani projekat je uspješno obrisan!");

            alert.showAndWait();
        }
    }

    public void detaljiAction(ActionEvent actionEvent){
        Projekat p = projektiTableView.getSelectionModel().getSelectedItem();

        if(p==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Niste izabrali nijedan projekat!");

            alert.showAndWait();
        }else{
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detalji.fxml"));
                DetaljiController ctrl = new DetaljiController(p);
                loader.setController(ctrl);
                Parent root = loader.load();
                stage.setTitle("Detalji");
                stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void promijeniVodjuAction(ActionEvent actionEvent){
        Projekat p = projektiTableView.getSelectionModel().getSelectedItem();

        if(p==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Obavještenje");
            alert.setHeaderText(null);
            alert.setContentText("Niste izabrali nijedan projekat!");

            alert.showAndWait();
        }else{
            List<Clan> choices = new ArrayList<>();

            for(Clan x:clanovi){
                choices.add(x);
            }

            ChoiceDialog<Clan> dialog = new ChoiceDialog<>(null, choices);
            dialog.setTitle("Promjena vođe");
            dialog.setHeaderText(null);
            dialog.setContentText("Izaberite vođu:");

            Optional<Clan> result = dialog.showAndWait();
            if (result.isPresent()){
                Clan noviV = new Clan(result.get());

                projekti.remove(p);
                p.setVodja(noviV);
                projekti.add(p);
                dao.promijeniProjekat(p);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Obavještenje");
                alert.setHeaderText(null);
                alert.setContentText("Izabrana osoba je sada vođa!");

                alert.showAndWait();
            }
        }
    }

    public void promijeniNazivAction(ActionEvent actionEvent){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Promjena naziva");
        dialog.setHeaderText(null);
        dialog.setContentText("Unesite novi naziv:");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            ArrayList<String> fajl = new ArrayList<>();
            fajl.add(result.get());

            Scanner ulaz;
            try {
                ulaz = new Scanner(new FileReader("osnivacki_akt.txt"));
                String stariNaziv = ulaz.nextLine();

                while(ulaz.hasNextLine()){
                    fajl.add(ulaz.nextLine());
                }

                ulaz.close();
            } catch (FileNotFoundException e) {
                System.out.println("Datoteka osnivacki_akt.txt ne postoji ili se ne može otvoriti");
                return;
            }

            PrintWriter izlaz;
            try {
                izlaz = new PrintWriter(new FileWriter("osnivacki_akt.txt"));
                for(String x : fajl){
                    izlaz.println(x);
                }

                nazivLabel.setText("\"" + fajl.get(0) + "\"");
                izlaz.close();
            } catch (IOException e) {
                System.out.println("Datoteka osnivacki_akt.txt se ne može otvoriti");
            }
        }

// The Java 8 way to get the response value (with lambda expression).
        //result.ifPresent(name -> System.out.println("Your name: " + name));
    }

    public void ugasiUdruzenjeAction(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Gašenje udruženja");
        alert.setHeaderText(null);
        alert.setContentText("Da li ste sigurni da želite ugasiti udruženje?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            UdruzenjeDAO.removeInstance();
            File dbfile = new File("baza.db");
            dbfile.delete();

            System.exit(0);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    public void izadjiAction(ActionEvent actionEvent){
        System.exit(0);
    }

    public void slikaAction(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Promjena slike");
        alert.setHeaderText(null);
        alert.setContentText("Odakle želite izabrati sliku");

        ButtonType buttonTypeOne = new ButtonType("Računar");
        ButtonType buttonTypeTwo = new ButtonType("Internet");
        ButtonType buttonTypeCancel = new ButtonType("Odustani", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // ... user chose "One"
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Slika");
            dialog.setHeaderText(null);
            dialog.setContentText("Unesite put do slike:");

// Traditional way to get the response value.
            Optional<String> result2 = dialog.showAndWait();
            if (result.isPresent()){
                File novaSlika = new File(result2.get());

                PrintWriter izlaz;
                try {
                    izlaz = new PrintWriter(new FileWriter("slika.txt"));
                    File s = new File("resources/img/etf.png");

                    izlaz.println(novaSlika.toURI());
                    izlaz.close();
                    slika.setImage(new Image(ucitajSliku(),128,128,false,false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

// The Java 8 way to get the response value (with lambda expression).
            //result.ifPresent(name -> System.out.println("Your name: " + name));
        } else if (result.get() == buttonTypeTwo) {
            // ... user chose "Two"
            Parent root = null;
            try {
                Stage myStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pretraga.fxml"));
                PretragaController ctrl = new PretragaController();
                loader.setController(ctrl);
                root = loader.load();
                myStage.setTitle("Preteraga");
                myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                myStage.show();
                myStage.setOnHiding(windowEvent -> {
                    String adresa = ctrl.getAdresa();
                    if(adresa!=null){
                        PrintWriter izlaz;
                        try {
                            izlaz = new PrintWriter(new FileWriter("slika.txt"));
                            File s = new File("resources/img/etf.png");

                            izlaz.println(adresa);
                            izlaz.close();
                            slika.setImage(new Image(ucitajSliku(),128,128,false,false));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // ... user chose CANCEL or closed the dialog
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

    //Mozda ovdje mogu napraviti nesto genericko

    private int dajIdClan(ArrayList<Clan> clanovi){
        int maxId = 0;
        for(Clan x : clanovi){
            if(x.getId()>maxId){
                maxId = x.getId();
            }
        }
        return maxId+1;
    }

    private int dajIdProjekat(ArrayList<Projekat> projekti){
        int maxId = 0;
        for(Projekat x : projekti){
            if(x.getId()>maxId){
                maxId = x.getId();
            }
        }
        return maxId+1;
    }

    private String ucitajSliku(){
        Scanner ulaz;
        try {
            ulaz = new Scanner(new FileReader("slika.txt"));
            String naziv = ulaz.nextLine();
            ulaz.close();
            return naziv;
        } catch (FileNotFoundException e) {
            System.out.println("Datoteka slika.txt ne postoji ili se ne može otvoriti");
            return null;
        }
    }
}

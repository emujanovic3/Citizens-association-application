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
import net.sf.jasperreports.engine.JRException;

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
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
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
        clanoviTableView.setPlaceholder(new Label(bundle.getString("nemaClanova")));
        nazivCol.setCellValueFactory((new PropertyValueFactory<>("Naziv")));
        projektiTableView.setItems(projekti);
        projektiTableView.setPlaceholder(new Label(bundle.getString("nemaProjekata")));

        slika.setImage(new Image(ucitajSliku(),128,128,false,false));
    }

    public void dodajClanaAction(ActionEvent actionEvent){
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clan.fxml"),bundle);
            ClanController ctrl = new ClanController();
            loader.setController(ctrl);
            Parent root = loader.load();
            stage.setTitle(bundle.getString("dodajClana"));
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
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        if(zaBrisanje instanceof Predsjednik){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("neMozeteIzbacitiPredsjedinka"));

            alert.showAndWait();
            return;
        }

        if(zaBrisanje!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("potvrdaBrisanjaClana"));
            alert.setHeaderText(bundle.getString("obrisatCeteOdabranogClana"));
            alert.setContentText(bundle.getString("daLiSteSigurni"));

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
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/skupstina.fxml"),bundle);
            SkupstinaController ctrl = new SkupstinaController();
            loader.setController(ctrl);
            Parent root = loader.load();
            stage.setTitle(bundle.getString("skupstina"));
            stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dodajSkupstinaAction(ActionEvent actionEvent){
        Clan clan = clanoviTableView.getSelectionModel().getSelectedItem();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        if(clan instanceof Predsjednik){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("izabranaOsobaJePredsjednik"));

            alert.showAndWait();
        }else if(clan==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("nisteIzabraliNijednogClana"));

            alert.showAndWait();
        }else{
            if(!(clan instanceof Skupstina)){
                Clan c = new Skupstina(clan);
                dao.promijeniClana(c);
                clanovi.remove(clan);
                clanovi.add(c);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("obavjestenje"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("izabranaOsobaJeSadaUSkupstini"));

                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("obavjestenje"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("izabranaOsobaJeVecUSkupstini"));

                alert.showAndWait();
            }
        }
    }

    public void izbaciSkupstinaAction(ActionEvent actionEvent){
        Clan clan = clanoviTableView.getSelectionModel().getSelectedItem();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        if(clan instanceof Predsjednik){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("izabranaOsobaJePredsjednik"));

            alert.showAndWait();
        }else if(clan==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("nisteIzabraliNijednogClana"));

            alert.showAndWait();
        }else{
            if(!(clan instanceof Skupstina)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("obavjestenje"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("izabranaOsobaNijeUSkupstini"));

                alert.showAndWait();
            }else{
                Clan c = new Clan(clan);
                dao.promijeniClana(c);
                clanovi.remove(clan);
                clanovi.add(c);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("obavjestenje"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("izabranaOsobaJeIzbacenaIzSkupstine"));

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


        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        ChoiceDialog<Clan> dialog = new ChoiceDialog<>(null, choices);
        dialog.setTitle(bundle.getString("promjenaPredsjednika"));
        dialog.setHeaderText(null);
        dialog.setContentText(bundle.getString("izaberiteNovogPredsjednika"));

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
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("izabranaOsobaJePredsjednik"));

            alert.showAndWait();
        }

// The Java 8 way to get the response value (with lambda expression).
        //result.ifPresent(letter -> System.out.println("Your choice: " + letter));
    }

    public void dodajProjekatAction(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/projekat.fxml"),bundle);
            ProjekatController ctrl = new ProjekatController(clanovi);
            loader.setController(ctrl);
            Parent root = loader.load();
            stage.setTitle(bundle.getString("dodajProjekat"));
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
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        if(zaBrisanje==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("nisteIzabraliNijedanProjekat"));

            alert.showAndWait();
        }else{
            dao.obrisiProjekat(zaBrisanje);
            projekti.remove(zaBrisanje);
            projektiTableView.setItems(projekti);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("izabraniProjekatJeUspjesnoObrisan"));

            alert.showAndWait();
        }
    }

    public void detaljiAction(ActionEvent actionEvent){
        Projekat p = projektiTableView.getSelectionModel().getSelectedItem();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        if(p==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("nisteIzabraliNijedanProjekat"));

            alert.showAndWait();
        }else{
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detalji.fxml"),bundle);
                DetaljiController ctrl = new DetaljiController(p);
                loader.setController(ctrl);
                Parent root = loader.load();
                stage.setTitle(bundle.getString("detalji"));
                stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void promijeniVodjuAction(ActionEvent actionEvent){
        Projekat p = projektiTableView.getSelectionModel().getSelectedItem();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        if(p==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("obavjestenje"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("nisteIzabraliNijedanProjekat"));

            alert.showAndWait();
        }else{
            List<Clan> choices = new ArrayList<>();

            for(Clan x:clanovi){
                choices.add(x);
            }

            ChoiceDialog<Clan> dialog = new ChoiceDialog<>(null, choices);
            dialog.setTitle(bundle.getString("promjenaVodje"));
            dialog.setHeaderText(null);
            dialog.setContentText(bundle.getString("izaberiteVodju"));

            Optional<Clan> result = dialog.showAndWait();
            if (result.isPresent()){
                Clan noviV = new Clan(result.get());

                projekti.remove(p);
                p.setVodja(noviV);
                projekti.add(p);
                dao.promijeniProjekat(p);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("obavjestenje"));
                alert.setHeaderText(null);
                alert.setContentText(bundle.getString("izabranaOsobaJeSadaVodja"));

                alert.showAndWait();
            }
        }
    }

    public void promijeniNazivAction(ActionEvent actionEvent){
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(bundle.getString("promjenaNaziva"));
        dialog.setHeaderText(null);
        dialog.setContentText(bundle.getString("unesiteNoviNaziv"));

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
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("gasenjeUdruzenja"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("daLiSteSigurniDaZeliteUgasitiUdruzenje"));

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
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("promjenaSlike"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("odakleZeliteIzabratiSliku"));

        ButtonType buttonTypeOne = new ButtonType(bundle.getString("racunar"));
        ButtonType buttonTypeTwo = new ButtonType(bundle.getString("internet"));
        ButtonType buttonTypeCancel = new ButtonType(bundle.getString("odustani"), ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // ... user chose "One"
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle(bundle.getString("slika"));
            dialog.setHeaderText(null);
            dialog.setContentText(bundle.getString("unesitePutDoSlike"));

// Traditional way to get the response value.
            Optional<String> result2 = dialog.showAndWait();
            if (result2.isPresent()){
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pretraga.fxml"),bundle);
                PretragaController ctrl = new PretragaController();
                loader.setController(ctrl);
                root = loader.load();
                myStage.setTitle(bundle.getString("pretraga"));
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

    public void bosanskiAction(ActionEvent actionEvent){
        Locale.setDefault(new Locale("bs","BA"));
        restart();
    }

    public void engleskiAction(ActionEvent actionEvent){
        Locale.setDefault(new Locale("en","US"));
        restart();
    }

    public void printajClanoveAction(ActionEvent actionEvent){
        try {
            new PrintReportClanovi().showReport(dao.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }

    }

    public void oNamaAction(ActionEvent actionEvent){
        Parent root = null;
        try {
            Stage myStage = new Stage();
            ResourceBundle bundle = ResourceBundle.getBundle("Translation");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"),bundle);
            AboutController ctrl = new AboutController();
            loader.setController(ctrl);
            root = loader.load();
            myStage.setTitle(bundle.getString("aboutTitle"));
            myStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printajProjekteAction(ActionEvent actionEvent){
        try {
            new PrintReportProjekti().showReport(dao.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
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

    private void restart(){
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");

        Stage prozor = (Stage) projektiTableView.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavni.fxml"),bundle);
        loader.setController(this);

        try {
            prozor.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

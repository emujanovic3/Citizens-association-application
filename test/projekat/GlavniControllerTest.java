package projekat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static javafx.scene.layout.Region.USE_PREF_SIZE;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ResourceBundle;

@ExtendWith(ApplicationExtension.class)

class GlavniControllerTest {

    @Start
    public void start (Stage stage) throws Exception {
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.resetujBazu();
        dao.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        dao.dodajClana(new Predsjednik(1,"Emir","Mujanovic", LocalDate.of(1999,9,18),
                new Prebivaliste("MBB","Goražde","Bosna"),Drzavljanstvo.BIH));
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        dao.dodajClana(new Skupstina(2,"Nedim","Hastor",LocalDate.of(2000,9,18),
                new Prebivaliste("Mravinjac bb.","Goražde","Bosna"),Drzavljanstvo.BIH));
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        dao.dodajClana(new Skupstina(3,"Filip","Kulenovic",LocalDate.of(1994,5,19),
                new Prebivaliste("Titova","Sarajevo","Bosna"),Drzavljanstvo.BIH));
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();

        PrintWriter izlaz;
        try {
            izlaz = new PrintWriter(new FileWriter("osnivacki_akt.txt"));
            izlaz.println("Test\n" +
                    "Emir Mujanovic 1999-09-18 MBB Goražde Bosna BIH\n" +
                    "Nedim Hastor 2000-09-18 Mravinjac bb. Goražde Bosna BIH\n" +
                    "Filip Kulenovic 1994-05-19 Titova Sarajevo Bosna BIH\n" +
                    "Sarajevo\n" +
                    "Aleja Bosne Srebrne\n" +
                    "udruzenje\n" +
                    "test");
            izlaz.close();
        } catch (IOException e) {
            System.out.println("Datoteka osnivacki_akt.txt se ne može otvoriti");
            return;
        }

        try {
            izlaz = new PrintWriter(new FileWriter("slika.txt"));
            File s = new File("resources/img/etf.png");

            izlaz.println(s.toURI());
            izlaz.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = new Stage();
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavni.fxml"),bundle);
        GlavniController ctrl = new GlavniController();
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle(bundle.getString("udruzenje"));
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.show();
        stage.toFront();
    }

    @Test
    void dodajClanaAction(FxRobot robot) {
        robot.clickOn("#dodajClanaBtn");
        robot.clickOn("#imeClanField").write("Emironi");
        robot.clickOn("#prezimeClanField").write("Mujanović");
        DatePicker datumRodjenja = robot.lookup("#datePickerClan").queryAs(DatePicker.class);
        datumRodjenja.setValue(LocalDate.of(1999,9,18));
        robot.clickOn("#adresaClanField").write("Mevsuda Bajica Baje br.23");
        robot.clickOn("#gradClanField").write("Goražde");
        robot.clickOn("#drzavaClanField").write("Bosna i Hercegovina");
        robot.clickOn("#dodajBtn");

        TableView<Clan> osnivaci = robot.lookup("#clanoviTableView").queryAs(TableView.class);
        assertEquals(4,osnivaci.getItems().size());
        assertEquals("Emironi",osnivaci.getItems().get(3).getIme());
    }

    @Test
    void obrisiClanaAction(FxRobot robot) {
        TableView<Clan> clanovi = robot.lookup("#clanoviTableView").queryAs(TableView.class);
        assertEquals(3,clanovi.getItems().size());
        assertEquals("Emir",clanovi.getItems().get(0).getIme());

        clanovi.getSelectionModel().selectLast();
        robot.clickOn("#obrisiClanaBtn");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Klik na dugme Ok
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        assertEquals(2,clanovi.getItems().size());
    }

    @Test
    void dodajProjekatAction(FxRobot robot) {
        robot.clickOn("#dodajProjekatBtn");

        robot.clickOn("#nazivProjekat").write("Test projekat");
        ChoiceBox<Projekat> vodje = robot.lookup("#projekatChoiceBox").queryAs(ChoiceBox.class);
        robot.clickOn(vodje);
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);


        robot.clickOn("#opisProjekat").write("Ovo je testni projekat");
        robot.clickOn("#dodajPBtn");

        TableView projekti = robot.lookup("#projektiTableView").queryAs(TableView.class);

        assertEquals(1,projekti.getItems().size());
    }

    @Test
    void obrisiProjekatAction(FxRobot robot) {
        robot.clickOn("#dodajProjekatBtn");

        robot.clickOn("#nazivProjekat").write("Test projekat");
        ChoiceBox<Projekat> vodje = robot.lookup("#projekatChoiceBox").queryAs(ChoiceBox.class);
        robot.clickOn(vodje);
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);

        robot.clickOn("#opisProjekat").write("Ovo je testni projekat");
        robot.clickOn("#dodajPBtn");

        TableView projekti = robot.lookup("#projektiTableView").queryAs(TableView.class);

        assertEquals(1,projekti.getItems().size());

        projekti.getSelectionModel().selectLast();
        robot.clickOn("obrisiProjekatBtn");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Klik na dugme Ok
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        assertEquals(0,projekti.getItems().size());
    }

    @Test
    void detaljiAction(FxRobot robot) {
        robot.clickOn("#dodajProjekatBtn");

        robot.clickOn("#nazivProjekat").write("Test projekat");
        ChoiceBox<Projekat> vodje = robot.lookup("#projekatChoiceBox").queryAs(ChoiceBox.class);
        robot.clickOn(vodje);
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);


        robot.clickOn("#opisProjekat").write("Ovo je testni projekat");
        robot.clickOn("#dodajPBtn");

        TableView projekti = robot.lookup("#projektiTableView").queryAs(TableView.class);

        projekti.getSelectionModel().selectLast();
        robot.clickOn("#detaljiBtn");

        Label naziv = robot.lookup("#nazivLbl").queryAs(Label.class);

        assertEquals("Test projekat",naziv.getText());
    }

    @Test
    void ugasiUdruzenjeAction(FxRobot robot) {
        robot.clickOn("#ugasiBtn");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Klik na dugme Ok
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        robot.clickOn(cancelButton);

    }

    @Test
    void bosanskiAction(FxRobot robot) {
        robot.clickOn("#menuDatoteka");
        robot.clickOn("#menuJezik");
        robot.clickOn("#menuBosanski");

        Button dodajClanaBtn = robot.lookup("#dodajClanaBtn").queryAs(Button.class);

        assertEquals("Dodaj člana",dodajClanaBtn.getText());
    }

    @Test
    void engleskiAction(FxRobot robot) {
        robot.clickOn("#menuDatoteka");
        robot.clickOn("#menuJezik");
        robot.clickOn("#menuBosanski");

        robot.sleep(200);

        robot.clickOn("#menuDatoteka");
        robot.clickOn("#menuJezik");
        robot.drag("#menuBosanski");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);


        Button dodajClanaBtn = robot.lookup("#dodajClanaBtn").queryAs(Button.class);

        assertEquals("Add member",dodajClanaBtn.getText());
    }
}
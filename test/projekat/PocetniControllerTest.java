package projekat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.File;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

@ExtendWith(ApplicationExtension.class)

class PocetniControllerTest {

    @Start
    public void start (Stage stage) throws Exception {
        File dbfile = new File("baza.db");
        dbfile.delete();

        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        PocetniController ctrl = new PocetniController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pocetni.fxml"),bundle);
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Start");
        stage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
        stage.show();
        stage.toFront();
    }

    @Test
    void dodajOsnivacaAction(FxRobot robot) {
        robot.clickOn("#dodajOsnivacaBtn");
        robot.clickOn("#imeClanField").write("Emir");
        robot.clickOn("#prezimeClanField").write("Mujanović");
        DatePicker datumRodjenja = robot.lookup("#datePickerClan").queryAs(DatePicker.class);
        datumRodjenja.setValue(LocalDate.of(1999,9,18));
        robot.clickOn("#adresaClanField").write("Mevsuda Bajica Baje br.23");
        robot.clickOn("#gradClanField").write("Goražde");
        robot.clickOn("#drzavaClanField").write("Bosna i Hercegovina");
        robot.clickOn("#dodajBtn");

        TableView<Clan> osnivaci = robot.lookup("#osnivaciTableView").queryAs(TableView.class);
        assertEquals(1,osnivaci.getItems().size());
        assertEquals("Emir",osnivaci.getItems().get(0).getIme());
    }

    @Test
    void brisiOsnivacaAction(FxRobot robot) {
        robot.clickOn("#dodajOsnivacaBtn");
        robot.clickOn("#imeClanField").write("Emir");
        robot.clickOn("#prezimeClanField").write("Mujanović");
        DatePicker datumRodjenja = robot.lookup("#datePickerClan").queryAs(DatePicker.class);
        datumRodjenja.setValue(LocalDate.of(1999,9,18));
        robot.clickOn("#adresaClanField").write("Mevsuda Bajica Baje br.23");
        robot.clickOn("#gradClanField").write("Goražde");
        robot.clickOn("#drzavaClanField").write("Bosna i Hercegovina");
        robot.clickOn("#dodajBtn");

        TableView<Clan> osnivaci = robot.lookup("#osnivaciTableView").queryAs(TableView.class);
        assertEquals(1,osnivaci.getItems().size());
        assertEquals("Emir",osnivaci.getItems().get(0).getIme());

        osnivaci.getSelectionModel().selectFirst();
        robot.clickOn("#brisiOsnivacaBtn");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Klik na dugme Ok
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        assertEquals(0,osnivaci.getItems().size());
    }

    @Test
    void napraviAction(FxRobot robot) {
        robot.clickOn("#dodajOsnivacaBtn");
        robot.clickOn("#imeClanField").write("Emir");
        robot.clickOn("#prezimeClanField").write("Mujanović");
        DatePicker datumRodjenja = robot.lookup("#datePickerClan").queryAs(DatePicker.class);
        datumRodjenja.setValue(LocalDate.of(1999,9,18));
        robot.clickOn("#adresaClanField").write("Mevsuda Bajica Baje br.23");
        robot.clickOn("#gradClanField").write("Goražde");
        robot.clickOn("#drzavaClanField").write("Bosna i Hercegovina");
        robot.clickOn("#dodajBtn");

        robot.clickOn("#dodajOsnivacaBtn");
        robot.clickOn("#imeClanField").write("Nedim");
        robot.clickOn("#prezimeClanField").write("Hastor");
        DatePicker datumRodjenja2 = robot.lookup("#datePickerClan").queryAs(DatePicker.class);
        datumRodjenja2.setValue(LocalDate.of(1998,10,25));
        robot.clickOn("#adresaClanField").write("Mravinjac bb.");
        robot.clickOn("#gradClanField").write("Goražde");
        robot.clickOn("#drzavaClanField").write("Bosna i Hercegovina");
        robot.clickOn("#dodajBtn");

        robot.clickOn("#dodajOsnivacaBtn");
        robot.clickOn("#imeClanField").write("Dinko");
        robot.clickOn("#prezimeClanField").write("Omeragić");
        DatePicker datumRodjenja3 = robot.lookup("#datePickerClan").queryAs(DatePicker.class);
        datumRodjenja3.setValue(LocalDate.of(1999,9,18));
        robot.clickOn("#adresaClanField").write("Bihać bb.");
        robot.clickOn("#gradClanField").write("Bihać");
        robot.clickOn("#drzavaClanField").write("Bosna i Hercegovina");
        robot.clickOn("#dodajBtn");

        robot.clickOn("#nazivField").write("Test udruženje");
        robot.clickOn("#sjedisteField").write("ETF");
        robot.clickOn("#adresaField").write("Zmaja od Bosne");
        robot.clickOn("#kImeField").write("udruzenje");
        robot.clickOn("#sifraField").write("test");

        robot.clickOn("#napraviUdruzenjeBtn");

        Button izadji = robot.lookup("#izadjiBtn").queryAs(Button.class);

        robot.closeCurrentWindow();
    }

    @Test
    void osnicaviIzuzetak(FxRobot robot){
        robot.clickOn("#dodajOsnivacaBtn");
        robot.clickOn("#imeClanField").write("Emir");
        robot.clickOn("#prezimeClanField").write("Mujanović");
        DatePicker datumRodjenja = robot.lookup("#datePickerClan").queryAs(DatePicker.class);
        datumRodjenja.setValue(LocalDate.of(1999,9,18));
        robot.clickOn("#adresaClanField").write("Mevsuda Bajica Baje br.23");
        robot.clickOn("#gradClanField").write("Goražde");
        robot.clickOn("#drzavaClanField").write("Bosna i Hercegovina");
        robot.clickOn("#dodajBtn");

        robot.clickOn("#nazivField").write("Test udruženje");
        robot.clickOn("#sjedisteField").write("ETF");
        robot.clickOn("#adresaField").write("Zmaja od Bosne");
        robot.clickOn("#kImeField").write("udruzenje");
        robot.clickOn("#sifraField").write("test");

        robot.clickOn("#napraviUdruzenjeBtn");

        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Klik na dugme Ok
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        robot.clickOn("#odustaniPocetni");
    }

    @Test
    void odustaniPocetniAction(FxRobot robot) {
        Button odustani = robot.lookup("#odustaniPocetni").queryAs(Button.class);
        robot.clickOn(odustani);
    }
}
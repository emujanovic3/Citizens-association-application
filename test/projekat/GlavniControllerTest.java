package projekat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static javafx.scene.layout.Region.USE_PREF_SIZE;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

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
    void dodajClanaAction() {
    }

    @Test
    void obrisiClanaAction() {
    }

    @Test
    void promijeniPredsjednikaAction() {
    }

    @Test
    void dodajProjekatAction() {
    }

    @Test
    void obrisiProjekatAction() {
    }

    @Test
    void detaljiAction() {
    }

    @Test
    void promijeniVodjuAction() {
    }

    @Test
    void promijeniNazivAction() {
    }

    @Test
    void ugasiUdruzenjeAction() {
    }

    @Test
    void izadjiAction() {
    }

    @Test
    void slikaAction() {
    }

    @Test
    void bosanskiAction() {
    }

    @Test
    void engleskiAction() {
    }
}
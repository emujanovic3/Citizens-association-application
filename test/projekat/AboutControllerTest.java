package projekat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_PREF_SIZE;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class AboutControllerTest {
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
    void otvaranjeProzora(FxRobot robot) {

        robot.clickOn("#menuPomoc");
        robot.clickOn("#menuONama");

        Label cs = robot.lookup("#csV42").queryAs(Label.class);

        assertEquals("v4.2",cs.getText());
    }
}
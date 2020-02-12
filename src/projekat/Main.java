package projekat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class Main extends Application {

    private boolean prviPut() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement dajSveClanoveUpit = conn.prepareStatement("SELECT * FROM clan;");
        } catch (SQLException e) {
            return true;
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        if(prviPut()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pocetni.fxml"));
            LoginController ctrl = new LoginController();
            loader.setController(ctrl);
            Parent root = loader.load();
            primaryStage.setTitle("Start");
            primaryStage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            primaryStage.show();
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            LoginController ctrl = new LoginController();
            loader.setController(ctrl);
            Parent root = loader.load();
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root, USE_PREF_SIZE, USE_PREF_SIZE));
            primaryStage.show();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

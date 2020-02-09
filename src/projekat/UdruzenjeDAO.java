package projekat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UdruzenjeDAO {
    private static UdruzenjeDAO instance;
    private Connection conn;

    private PreparedStatement dajSveClanoveUpit;

    public static UdruzenjeDAO getInstance(){
        if(instance==null){
            instance = new UdruzenjeDAO();
        }
        return instance;
    }

    private UdruzenjeDAO(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            dajSveClanoveUpit = conn.prepareStatement("SELECT * FROM clan;");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

package projekat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

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
            regenerisiBazu();

            try {
                dajSveClanoveUpit = conn.prepareStatement("SELECT * FROM clan;");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void removeInstance(){
        if(instance==null){
            return;
        }
        instance.close();
        instance = null;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetujBazu() {
        UdruzenjeDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
    }

    private void regenerisiBazu() {
        try {
            Scanner ulaz = new Scanner(new FileInputStream("baza.db.sql"));
            String sqlUpit = "";
            while(ulaz.hasNextLine()){
                sqlUpit += ulaz.nextLine();

                if(sqlUpit.charAt(sqlUpit.length()-1) == ';'){
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

package projekat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class UdruzenjeDAO {
    private static UdruzenjeDAO instance;
    private Connection conn;

    private PreparedStatement dajSveClanoveUpit, dodajClanaUpit, obrisiClanaUpit, dajSkupstinuUpit, promijeniClanaUpit;
    private PreparedStatement dajSveProjekteUpit, nadjiClanaUpit, dodajProjekatUpit, obrisiProjekatUpit;

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

        try {
            dodajClanaUpit = conn.prepareStatement("INSERT INTO clan VALUES(?,?,?,?,?,?,?,?,?,?);");
            obrisiClanaUpit = conn.prepareStatement("DELETE FROM clan WHERE id=?;");
            dajSkupstinuUpit = conn.prepareStatement("SELECT * FROM clan WHERE skupstina=1;");
            promijeniClanaUpit = conn.prepareStatement("UPDATE clan SET ime=?, prezime=?, datum_rodjenja=?, adresa_stanovanja=?, " +
                    "grad=?, drzava=?, drzavljanstvo=?, predsjednik=?, skupstina=? WHERE id=?;");
            dajSveProjekteUpit = conn.prepareStatement("SELECT * FROM projekat;");
            nadjiClanaUpit = conn.prepareStatement("SELECT * FROM clan WHERE id=?;");
            dodajProjekatUpit = conn.prepareStatement("INSERT INTO projekat VALUES(?,?,?,?);");
            obrisiProjekatUpit = conn.prepareStatement("DELETE FROM projekat WHERE id=?;");
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void dodajClana(Clan clan){
        try {
            dodajClanaUpit.setInt(1,clan.getId());
            dodajClanaUpit.setString(2,clan.getIme());
            dodajClanaUpit.setString(3,clan.getPrezime());
            dodajClanaUpit.setString(4,clan.getDatumRodjenja().toString());
            dodajClanaUpit.setString(5,clan.getPrebivaliste().getAdresa());
            dodajClanaUpit.setString(6,clan.getPrebivaliste().getGrad());
            dodajClanaUpit.setString(7,clan.getPrebivaliste().getDrzava());
            dodajClanaUpit.setString(8,clan.getDrzavljanstvo().toString());

            if(clan instanceof Predsjednik){
                dodajClanaUpit.setInt(9,1);
            }else{
                dodajClanaUpit.setInt(9,0);
            }

            if(clan instanceof Skupstina){
                dodajClanaUpit.setInt(10,1);
            }else{
                dodajClanaUpit.setInt(10,0);
            }

            dodajClanaUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Clan> dajSveClanove(){
        ArrayList<Clan> clanovi = new ArrayList<>();
        try {
            ResultSet rs = dajSveClanoveUpit.executeQuery();
            while(rs.next()){
                Clan novi = dajClanaIzResultSeta(rs);
                clanovi.add(novi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clanovi;
    }

    public ArrayList<Clan> dajSkupstinu(){
        ArrayList<Clan> skupstina = new ArrayList<>();
        try {
            ResultSet rs = dajSkupstinuUpit.executeQuery();
            while(rs.next()){
                Clan s = dajClanaIzResultSeta(rs);
                skupstina.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skupstina;
    }

    public void promijeniClana(Clan clan){
        try {
            promijeniClanaUpit.setString(1,clan.getIme());
            promijeniClanaUpit.setString(2,clan.getPrezime());
            promijeniClanaUpit.setString(3,clan.getDatumRodjenja().toString());
            promijeniClanaUpit.setString(4,clan.getPrebivaliste().getAdresa());
            promijeniClanaUpit.setString(5,clan.getPrebivaliste().getGrad());
            promijeniClanaUpit.setString(6,clan.getPrebivaliste().getDrzava());
            promijeniClanaUpit.setString(7,clan.getDrzavljanstvo().toString());

            if(clan instanceof Predsjednik){
                promijeniClanaUpit.setInt(8,1);
            }else{
                promijeniClanaUpit.setInt(8,0);
            }

            if(clan instanceof Skupstina){
                promijeniClanaUpit.setInt(9,1);
            }else{
                promijeniClanaUpit.setInt(9,0);
            }

            promijeniClanaUpit.setInt(10,clan.getId());

            promijeniClanaUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Clan dajClanaIzResultSeta(ResultSet rs) {
        Clan novi = null;
        try {
            if(rs.getInt(9)==1){
                novi = new Predsjednik(rs.getInt(1),rs.getString(2),rs.getString(3),dajDatumIzResultSeta(rs.getString(4)),
                        new Prebivaliste(rs.getString(5),rs.getString(6),rs.getString(7)),Drzavljanstvo.BIH);

                if(rs.getString(8).equals("STRANO")){
                    novi.setDrzavljanstvo(Drzavljanstvo.STRANO);
                }
            }else if(rs.getInt(10)==1){
                novi = new Skupstina(rs.getInt(1),rs.getString(2),rs.getString(3),dajDatumIzResultSeta(rs.getString(4)),
                        new Prebivaliste(rs.getString(5),rs.getString(6),rs.getString(7)),Drzavljanstvo.BIH);

                if(rs.getString(8).equals("STRANO")){
                    novi.setDrzavljanstvo(Drzavljanstvo.STRANO);
                }
            }else{
                novi = new Clan(rs.getInt(1),rs.getString(2),rs.getString(3),dajDatumIzResultSeta(rs.getString(4)),
                        new Prebivaliste(rs.getString(5),rs.getString(6),rs.getString(7)),Drzavljanstvo.BIH);

                if(rs.getString(8).equals("STRANO")){
                    novi.setDrzavljanstvo(Drzavljanstvo.STRANO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return novi;
    }

    public void obrisiClana(int id){
        try {
            obrisiClanaUpit.setInt(1,id);
            obrisiClanaUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Projekat> dajSveProjekte(){
        ArrayList<Projekat> projekti = new ArrayList<>();
        try {
            ResultSet rs = dajSveProjekteUpit.executeQuery();
            while(rs.next()){
                Projekat p = dajProjekatIzResultSeta(rs);
                projekti.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projekti;
    }

    private Projekat dajProjekatIzResultSeta(ResultSet rs) {
        try {
            return new Projekat(rs.getInt(1),rs.getString(2),nadjiClana(rs.getInt(3)),rs.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void obrisiProjekat(Projekat p){
        try {
            obrisiProjekatUpit.setInt(1,p.getId());
            obrisiProjekatUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Clan nadjiClana(int id) {
        try {
            nadjiClanaUpit.setInt(1,id);
            ResultSet rs = nadjiClanaUpit.executeQuery();

            if(rs.next()){
                return dajClanaIzResultSeta(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private LocalDate dajDatumIzResultSeta(String datum) {
        String[] odvojeno = datum.split("-");
        return LocalDate.of(Integer.parseInt(odvojeno[0]),Integer.parseInt(odvojeno[1]),Integer.parseInt(odvojeno[2]));
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

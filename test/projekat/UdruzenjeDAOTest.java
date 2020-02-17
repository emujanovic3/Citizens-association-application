package projekat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UdruzenjeDAOTest {

    @BeforeEach
    void resetovanjeBaze(){
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
        dao.dodajClana(new Clan(4,"John","Smith",LocalDate.of(1985,3,22),
                new Prebivaliste("Streettt","New York","USA"),Drzavljanstvo.STRANO));
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        dao.dodajProjekat(new Projekat(1,"Humanitarna akcija",dao.nadjiClana(2),"Ovo je humanitarna akcija"));
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        dao.dodajProjekat(new Projekat(2,"Akcija cišćenja",dao.nadjiClana(4),"Projekat cišćenja grada"));
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
    }

    @Test
    void dodajClana() {
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.dodajClana(new Clan(5,"Emin","Mujanovic",LocalDate.of(2008,6,23),
                new Prebivaliste("MBB","Goražde","Bosna"),Drzavljanstvo.BIH));
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        assertEquals(5,sviClanovi.size());

        assertEquals("Emin",sviClanovi.get(4).getIme());
        assertEquals("MBB",sviClanovi.get(4).getPrebivaliste().getAdresa());
    }

    @Test
    void dajSveClanove() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        assertEquals(4,sviClanovi.size());
    }

    @Test
    void dajSkupstinu() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Clan> skupstina = dao.dajSkupstinu();
        assertEquals(2,skupstina.size());
        assertEquals("Nedim",skupstina.get(0).getIme());
        assertEquals("Hastor",skupstina.get(0).getPrezime());
    }

    @Test
    void promijeniClana() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        Clan zaPromjenu = sviClanovi.get(3);
        zaPromjenu.setIme("Promijenjeno");
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        dao.promijeniClana(zaPromjenu);
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        sviClanovi = dao.dajSveClanove();
        assertEquals("Promijenjeno",sviClanovi.get(3).getIme());
    }

    @Test
    void obrisiClana() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.obrisiClana(4);
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        assertEquals(3,sviClanovi.size());
    }

    @Test
    void dajSveProjekte() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Projekat> sviProjekti = dao.dajSveProjekte();
        assertEquals(2,sviProjekti.size());
    }

    @Test
    void promijeniProjekat() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Projekat> sviProjekti = dao.dajSveProjekte();
        Projekat zaPromjenu = sviProjekti.get(1);
        zaPromjenu.setNaziv("Promijenjeno");
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        dao.promijeniProjekat(zaPromjenu);
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        sviProjekti = dao.dajSveProjekte();
        assertEquals("Promijenjeno",sviProjekti.get(1).getNaziv());
    }

    @Test
    void dodajProjekat() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.dodajProjekat(new Projekat(3,"Testni projekat",dao.nadjiClana(2),"Svrha ovog projekta je testiranje"));
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        ArrayList<Projekat> sviProjekti = dao.dajSveProjekte();
        assertEquals(3,sviProjekti.size());

        assertEquals("Svrha ovog projekta je testiranje",sviProjekti.get(2).getOpis());
    }

    @Test
    void obrisiProjekat() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.obrisiClana(4);
        UdruzenjeDAO.removeInstance();
        dao = UdruzenjeDAO.getInstance();
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        assertEquals(3,sviClanovi.size());
    }

    @Test
    void nadjiClana() {
        UdruzenjeDAO.removeInstance();
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        Clan c = dao.nadjiClana(3);
        assertEquals("Filip",c.getIme());
        assertEquals(LocalDate.of(1994,5,19),c.getDatumRodjenja());
    }
}
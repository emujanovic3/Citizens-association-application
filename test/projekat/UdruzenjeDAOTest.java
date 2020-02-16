package projekat;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UdruzenjeDAOTest {
    @BeforeEach
    static void resetovanjeBaze(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.resetujBazu();
        dao.dodajClana(new Predsjednik(1,"Emir","Mujanović", LocalDate.of(1999,18,9),
                new Prebivaliste("MBB","Goražde","Bosna"),Drzavljanstvo.BIH));
        dao.dodajClana(new Skupstina(2,"Nedim","Hastor",LocalDate.of(2000,18,9),
                new Prebivaliste("Mravinjac bb.","Goražde","Bosna"),Drzavljanstvo.BIH));
        dao.dodajClana(new Skupstina(3,"Filip","Kulenović",LocalDate.of(1994,19,5),
                new Prebivaliste("Titova","Sarajevo","Bosna"),Drzavljanstvo.BIH));
        dao.dodajClana(new Clan(4,"John","Smith",LocalDate.of(1985,22,3),
                new Prebivaliste("Streettt","New York","USA"),Drzavljanstvo.STRANO));
        dao.dodajProjekat(new Projekat(1,"Humanitarna akcija",dao.nadjiClana(2),"Ovo je humanitarna akcija"));
        dao.dodajProjekat(new Projekat(2,"Akcija čišćenja",dao.nadjiClana(4),"Projekat čišćenja grada"));
    }

    @Test
    void dajSveClanoveTest(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        assertEquals(4,sviClanovi.size());
    }

    @Test
    void dajSveProjekte(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Projekat> sviProjekti = dao.dajSveProjekte();
        assertEquals(2,sviProjekti.size());
    }

    @Test
    void dodavanjeClana(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.dodajClana(new Clan(5,"Emin","Mujanović",LocalDate.of(2008,23,6),
                new Prebivaliste("MBB","Goražde","Bosna"),Drzavljanstvo.BIH));
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        assertEquals(5,sviClanovi.size());

        assertEquals("Emin",sviClanovi.get(4).getIme());
        assertEquals("MBB",sviClanovi.get(4).getPrebivaliste().getAdresa());
    }

    @Test
    void brisanjeClana(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.obrisiClana(4);
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        assertEquals(3,sviClanovi.size());
    }

    @Test
    public void dodavanjeProjekta(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        dao.dodajProjekat(new Projekat(3,"Testni projekat",dao.nadjiClana(2),"Svrha ovog projekta je testiranje"));
        ArrayList<Projekat> sviProjekti = dao.dajSveProjekte();
        assertEquals(3,sviProjekti.size());

        assertEquals("Svrha ovog projekta je testiranje",sviProjekti.get(2).getOpis());
    }

    @Test
    void brisanjeProjekta(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Projekat> sviProjekti = dao.dajSveProjekte();
        dao.obrisiProjekat(sviProjekti.get(2));

        sviProjekti = dao.dajSveProjekte();

        assertEquals(1,sviProjekti.size());
    }

    @Test
    void skupstina(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Clan> skupstina = dao.dajSkupstinu();
        assertEquals(2,skupstina.size());
        assertEquals("Nedim",skupstina.get(0).getIme());
        assertEquals("Hastor",skupstina.get(0).getPrezime());
    }

    @Test
    void promjenaClana(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Clan> sviClanovi = dao.dajSveClanove();
        Clan zaPromjenu = sviClanovi.get(3);
        zaPromjenu.setIme("Promijenjeno");
        dao.promijeniClana(zaPromjenu);
        sviClanovi = dao.dajSveClanove();
        assertEquals("Promijenjeno",sviClanovi.get(3).getIme());
    }

    @Test
    void promjenaProjekta(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        ArrayList<Projekat> sviProjekti = dao.dajSveProjekte();
        Projekat zaPromjenu = sviProjekti.get(1);
        zaPromjenu.setNaziv("Promijenjeno");
        dao.promijeniProjekat(zaPromjenu);
        sviProjekti = dao.dajSveProjekte();
        assertEquals("Promijenjeno",sviProjekti.get(1).getNaziv());
    }

    @Test
    void nadjiClanaTest(){
        UdruzenjeDAO dao = UdruzenjeDAO.getInstance();
        Clan c = dao.nadjiClana(3);
        assertEquals("Filip",c.getIme());
        assertEquals(LocalDate.of(1994,19,5),c.getDatumRodjenja());
    }
}
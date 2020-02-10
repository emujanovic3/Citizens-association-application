package projekat;

public class Projekat {
    private int id;
    private String naziv;
    private Clan vodja;
    private String opis;

    public Projekat(int id, String naziv, Clan vodja, String opis) {
        this.id = id;
        this.naziv = naziv;
        this.vodja = vodja;
        this.opis = opis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Clan getVodja() {
        return vodja;
    }

    public void setVodja(Clan vodja) {
        this.vodja = vodja;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}

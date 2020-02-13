package projekat;

import java.time.LocalDate;

public class Skupstina extends Clan {
    public Skupstina(int id, String ime, String prezime, LocalDate datumRodjenja, Prebivaliste prebivaliste, Drzavljanstvo drzavljanstvo) {
        super(id, ime, prezime, datumRodjenja, prebivaliste, drzavljanstvo);
    }

    public Skupstina(Clan clan) {
        super(clan);
    }
}

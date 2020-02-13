package projekat;

import java.time.LocalDate;

public class Predsjednik extends Clan {
    public Predsjednik(int id, String ime, String prezime, LocalDate datumRodjenja, Prebivaliste prebivaliste, Drzavljanstvo drzavljanstvo) {
        super(id, ime, prezime, datumRodjenja, prebivaliste, drzavljanstvo);
    }

    public Predsjednik(Clan clan) {
        super(clan);
    }
}

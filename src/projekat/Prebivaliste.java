package projekat;

public class Prebivaliste {
    private String adresa;
    private String grad;
    private String drzava;

    public Prebivaliste(String adresa, String grad, String drzava) {
        this.adresa = adresa;
        this.grad = grad;
        this.drzava = drzava;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }
}

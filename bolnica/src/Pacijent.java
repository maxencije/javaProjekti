public class Pacijent implements Izleciv, Comparable<Pacijent> {
    private String ime, prezime;
    private int idKnjizice, duzinaLecenja;
    private boolean zarazen;
    private ZaraznaBolest dijagnoza;

    public Pacijent(String ime, String prezime, int idKnjizice, ZaraznaBolest dijagnoza) {
        this.ime = ime;
        this.prezime = prezime;
        this.idKnjizice = idKnjizice;
        this.dijagnoza = dijagnoza;
        this.zarazen = false;
        this.duzinaLecenja = 0;
    }

    public void setDuzinaLecenja(int duzinaLecenja) {
        this.duzinaLecenja = duzinaLecenja;
    }

    public void setZarazen(boolean zarazen) {
        this.zarazen = zarazen;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public int getIdKnjizice() {
        return idKnjizice;
    }

    public int getDuzinaLecenja() {
        return duzinaLecenja;
    }

    public boolean isZarazen() {
        return zarazen;
    }

    public ZaraznaBolest getDijagnoza() {
        return dijagnoza;
    }

    @Override
    public void leci(int brojDana) {
        duzinaLecenja += brojDana;
    }

    @Override
    public boolean izlecen() {
        return duzinaLecenja >= dijagnoza.getDuzinaBolesti();
    }

    @Override
    public String toString() {
        int vremeDoIzlecenja = Math.max((dijagnoza.getDuzinaBolesti() - duzinaLecenja), 0);
        String _izlecen = (izlecen() ? "" : "boluje od " + dijagnoza + " Vreme do izlecenja " + vremeDoIzlecenja);
        return "ID: " + idKnjizice + " " + ime + " " + prezime + " " + _izlecen;
    }

    @Override
    public int compareTo(Pacijent o) {
        if(dijagnoza instanceof Korona && o.getDijagnoza() instanceof Grip) {
            return -1;
        } else if(dijagnoza instanceof Korona && o.getDijagnoza() instanceof Korona ||
                    dijagnoza instanceof Grip && o.getDijagnoza() instanceof Grip) {
            return Integer.compare(o.getDijagnoza().getDuzinaBolesti(), dijagnoza.getDuzinaBolesti());
        } else if(dijagnoza instanceof Grip && o.getDijagnoza() instanceof Korona) {
            return 1;
        } else {
            return 0;}
    }
}

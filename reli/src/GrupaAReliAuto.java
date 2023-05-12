public class GrupaAReliAuto extends ReliAuto {

    public GrupaAReliAuto(String model, Pogon tipPogona, int godiste) {
        super(model, tipPogona, Math.max(godiste, 1990));
    }

    @Override
    public String toString() {
        return "Grupa A: " + super.toString();
    }
}
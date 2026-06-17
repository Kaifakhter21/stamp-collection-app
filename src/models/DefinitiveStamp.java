package models;

/**
 * DefinitiveStamp class extends Stamp.
 * Demonstrates inheritance and polymorphism.
 */
public class DefinitiveStamp extends Stamp {
    private double denomination;

    public DefinitiveStamp(int id, String name, int year, String country, double denomination) {
        super(id, name, year, country, "Definitive");
        this.denomination = denomination;
    }

    public double getDenomination() { return denomination; }
    public void setDenomination(double denomination) { this.denomination = denomination; }

    @Override
    public String toString() {
        return super.toString() + " - " + denomination + "p";
    }
}

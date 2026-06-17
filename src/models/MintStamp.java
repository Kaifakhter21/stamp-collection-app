package models;

/**
 * MintStamp class extends Stamp.
 * Represents an unused/mint stamp.
 */
public class MintStamp extends Stamp {
    private boolean hinged;

    public MintStamp(int id, String name, int year, String country, boolean hinged) {
        super(id, name, year, country, "Mint");
        this.hinged = hinged;
    }

    public boolean isHinged() { return hinged; }
    public void setHinged(boolean hinged) { this.hinged = hinged; }

    @Override
    public String toString() {
        return super.toString() + " - " + (hinged ? "Hinged" : "Unhinged");
    }
}

package models;

/**
 * CommemoratveStamp class extends Stamp.
 * Demonstrates inheritance for a specific stamp type.
 */
public class CommemoratveStamp extends Stamp {
    private String theme;

    public CommemoratveStamp(int id, String name, int year, String country, String theme) {
        super(id, name, year, country, "Commemorative");
        this.theme = theme;
    }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    @Override
    public String toString() {
        return super.toString() + " - Theme: " + theme;
    }
}

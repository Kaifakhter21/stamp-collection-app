package models;

/**
 * Abstract base class representing a stamp.
 * Demonstrates encapsulation and serves as parent for specialized stamp types.
 */
public abstract class Stamp {
    private int id;
    private String name;
    private int year;
    private String country;
    private String category;

    /**
     * Constructor for Stamp
     */
    public Stamp(int id, String name, int year, String country, String category) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.country = country;
        this.category = category;
    }

    // Getters and Setters (Encapsulation)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + year + ") - " + country + " [" + category + "]";
    }
}

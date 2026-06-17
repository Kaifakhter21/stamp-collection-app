package models;

/**
 * UsedStamp class extends Stamp.
 * Represents a used/postmarked stamp.
 */
public class UsedStamp extends Stamp {
    private String postmark;

    public UsedStamp(int id, String name, int year, String country, String postmark) {
        super(id, name, year, country, "Used");
        this.postmark = postmark;
    }

    public String getPostmark() { return postmark; }
    public void setPostmark(String postmark) { this.postmark = postmark; }

    @Override
    public String toString() {
        return super.toString() + " - Postmark: " + postmark;
    }
}

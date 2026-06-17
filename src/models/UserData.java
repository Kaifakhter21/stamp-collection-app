package models;

import java.util.ArrayList;
import java.util.List;

/**
 * UserData class manages a user's stamp collections.
 * Demonstrates composition and encapsulation.
 */
public class UserData {
    private User user;
    private List<Stamp> ownedStamps;
    private List<Stamp> wishlistStamps;

    public UserData(User user) {
        this.user = user;
        this.ownedStamps = new ArrayList<>();
        this.wishlistStamps = new ArrayList<>();
    }

    // Getters and Setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Stamp> getOwnedStamps() { return ownedStamps; }
    public List<Stamp> getWishlistStamps() { return wishlistStamps; }

    // Owned Stamps Operations
    public void addOwnedStamp(Stamp stamp) { this.ownedStamps.add(stamp); }
    public void removeOwnedStamp(Stamp stamp) { this.ownedStamps.remove(stamp); }
    public boolean containsOwnedStamp(int stampId) {
        return ownedStamps.stream().anyMatch(s -> s.getId() == stampId);
    }

    // Wishlist Operations
    public void addWishlistStamp(Stamp stamp) { this.wishlistStamps.add(stamp); }
    public void removeWishlistStamp(Stamp stamp) { this.wishlistStamps.remove(stamp); }
    public boolean containsWishlistStamp(int stampId) {
        return wishlistStamps.stream().anyMatch(s -> s.getId() == stampId);
    }

    public int getTotalOwnedStamps() { return ownedStamps.size(); }
    public int getTotalWishlistStamps() { return wishlistStamps.size(); }
}

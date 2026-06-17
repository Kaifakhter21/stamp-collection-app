package database;

import models.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

/**
 * DatabaseManager class using Singleton pattern.
 * Manages all data loading, saving, and retrieval operations.
 * Demonstrates proper separation of concerns.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private List<Stamp> allStamps;
    private List<User> allUsers;
    private List<UserData> allUserData;
    private static final String STAMPS_FILE = "data/stamps.csv";
    private static final String USERS_FILE = "data/users.csv";
    private static final String COLLECTIONS_FILE = "data/user_collections.csv";

    private DatabaseManager() {
        allStamps = new ArrayList<>();
        allUsers = new ArrayList<>();
        allUserData = new ArrayList<>();
        ensureDataDirectory();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void ensureDataDirectory() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            System.err.println("Could not create data directory");
        }
    }

    /**
     * Load all data from CSV files
     */
    public void loadAllData() {
        loadStamps();
        loadUsers();
        loadUserCollections();
    }

    /**
     * Load stamps from stamps.csv
     * Format: id, name, year, country, category, extra_field
     */
    private void loadStamps() {
        allStamps.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(STAMPS_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                String country = parts[3].trim();
                String category = parts[4].trim();
                String extra = parts[5].trim();

                Stamp stamp = null;
                switch (category.toLowerCase()) {
                    case "definitive":
                        stamp = new DefinitiveStamp(id, name, year, country, Double.parseDouble(extra));
                        break;
                    case "commemorative":
                        stamp = new CommemoratveStamp(id, name, year, country, extra);
                        break;
                    case "used":
                        stamp = new UsedStamp(id, name, year, country, extra);
                        break;
                    case "mint":
                        stamp = new MintStamp(id, name, year, country, Boolean.parseBoolean(extra));
                        break;
                }
                if (stamp != null) {
                    allStamps.add(stamp);
                }
            }
        } catch (IOException e) {
            System.out.println("stamps.csv not found. Creating sample data...");
            createSampleStamps();
        }
    }

    /**
     * Load users from users.csv
     * Format: username, password
     */
    private void loadUsers() {
        allUsers.clear();
        allUserData.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String username = parts[0].trim();
                String password = parts[1].trim();
                User user = new User(username, password);
                allUsers.add(user);
                allUserData.add(new UserData(user));
            }
        } catch (IOException e) {
            System.out.println("users.csv not found. Creating sample users...");
            createSampleUsers();
        }
    }

    /**
     * Load user collections from user_collections.csv
     * Format: username, stamp_id, type (owned/wishlist)
     */
    private void loadUserCollections() {
        try (BufferedReader br = new BufferedReader(new FileReader(COLLECTIONS_FILE))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String username = parts[0].trim();
                int stampId = Integer.parseInt(parts[1].trim());
                String type = parts[2].trim();

                UserData userData = getUserData(username);
                Stamp stamp = getStampById(stampId);

                if (userData != null && stamp != null) {
                    if (type.equalsIgnoreCase("owned")) {
                        userData.addOwnedStamp(stamp);
                    } else if (type.equalsIgnoreCase("wishlist")) {
                        userData.addWishlistStamp(stamp);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("user_collections.csv not found or empty.");
        }
    }

    /**
     * Save all data to CSV files
     */
    public void saveAllData() {
        saveStamps();
        saveUsers();
        saveUserCollections();
    }

    private void saveStamps() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(STAMPS_FILE))) {
            pw.println("id,name,year,country,category,extra");
            for (Stamp s : allStamps) {
                String extra = "";
                if (s instanceof DefinitiveStamp) {
                    extra = String.valueOf(((DefinitiveStamp) s).getDenomination());
                } else if (s instanceof CommemoratveStamp) {
                    extra = ((CommemoratveStamp) s).getTheme();
                } else if (s instanceof UsedStamp) {
                    extra = ((UsedStamp) s).getPostmark();
                } else if (s instanceof MintStamp) {
                    extra = String.valueOf(((MintStamp) s).isHinged());
                }
                pw.println(s.getId() + "," + s.getName() + "," + s.getYear() + "," +
                        s.getCountry() + "," + s.getCategory() + "," + extra);
            }
        } catch (IOException e) {
            System.err.println("Error saving stamps: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            pw.println("username,password");
            for (User u : allUsers) {
                pw.println(u.getUsername() + "," + u.getPassword());
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private void saveUserCollections() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(COLLECTIONS_FILE))) {
            pw.println("username,stamp_id,type");
            for (UserData ud : allUserData) {
                String username = ud.getUser().getUsername();
                for (Stamp s : ud.getOwnedStamps()) {
                    pw.println(username + "," + s.getId() + ",owned");
                }
                for (Stamp s : ud.getWishlistStamps()) {
                    pw.println(username + "," + s.getId() + ",wishlist");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving collections: " + e.getMessage());
        }
    }

    // Helper Methods
    public Stamp getStampById(int id) {
        return allStamps.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public List<Stamp> getStampsByCategory(String category) {
        List<Stamp> result = new ArrayList<>();
        for (Stamp s : allStamps) {
            if (s.getCategory().equalsIgnoreCase(category)) {
                result.add(s);
            }
        }
        return result;
    }

    public UserData getUserData(String username) {
        for (UserData ud : allUserData) {
            if (ud.getUser().getUsername().equals(username)) {
                return ud;
            }
        }
        return null;
    }

    public User authenticateUser(String username, String password) {
        for (User u : allUsers) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public boolean userExists(String username) {
        return allUsers.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public void registerUser(String username, String password) {
        if (!userExists(username)) {
            User newUser = new User(username, password);
            allUsers.add(newUser);
            allUserData.add(new UserData(newUser));
            saveAllData();
        }
    }

    public List<Stamp> getAllStamps() {
        return new ArrayList<>(allStamps);
    }

    public void addStampToUserOwned(String username, int stampId) {
        UserData userData = getUserData(username);
        Stamp stamp = getStampById(stampId);
        if (userData != null && stamp != null && !userData.containsOwnedStamp(stampId)) {
            userData.addOwnedStamp(stamp);
            saveAllData();
        }
    }

    public void removeStampFromUserOwned(String username, int stampId) {
        UserData userData = getUserData(username);
        Stamp stamp = getStampById(stampId);
        if (userData != null && stamp != null) {
            userData.removeOwnedStamp(stamp);
            saveAllData();
        }
    }

    public void addStampToUserWishlist(String username, int stampId) {
        UserData userData = getUserData(username);
        Stamp stamp = getStampById(stampId);
        if (userData != null && stamp != null && !userData.containsWishlistStamp(stampId)) {
            userData.addWishlistStamp(stamp);
            saveAllData();
        }
    }

    public void removeStampFromUserWishlist(String username, int stampId) {
        UserData userData = getUserData(username);
        Stamp stamp = getStampById(stampId);
        if (userData != null && stamp != null) {
            userData.removeWishlistStamp(stamp);
            saveAllData();
        }
    }

    // Sample Data Creation
    private void createSampleStamps() {
        allStamps.add(new DefinitiveStamp(1, "King George VI", 1937, "United Kingdom", 0.5));
        allStamps.add(new DefinitiveStamp(2, "Queen Elizabeth II", 1953, "United Kingdom", 1.0));
        allStamps.add(new CommemoratveStamp(3, "Space Exploration", 1969, "United States", "Apollo 11"));
        allStamps.add(new CommemoratveStamp(4, "Royal Wedding", 1981, "United Kingdom", "Charles & Diana"));
        allStamps.add(new UsedStamp(5, "Victorian Era", 1880, "United Kingdom", "London"));
        allStamps.add(new UsedStamp(6, "Early 1900s", 1905, "France", "Paris"));
        allStamps.add(new MintStamp(7, "Modern UK", 2020, "United Kingdom", false));
        allStamps.add(new MintStamp(8, "Olympic Games", 2016, "Brazil", true));
        saveStamps();
    }

    private void createSampleUsers() {
        allUsers.add(new User("collector1", "pass123"));
        allUsers.add(new User("collector2", "pass456"));
        for (User u : allUsers) {
            allUserData.add(new UserData(u));
        }
        saveUsers();
    }
}

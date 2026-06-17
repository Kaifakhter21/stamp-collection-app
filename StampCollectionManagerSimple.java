import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * STANDALONE STAMP COLLECTION MANAGER
 * Single file version - No packages needed
 * Just save as StampCollectionManagerSimple.java and run!
 */

// User Class
class User {
    private String username, password;
    public User(String u, String p) { username = u; password = p; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}

// Stamp Abstract Class
abstract class Stamp {
    protected int id;
    protected String name, year, country, category;
    public Stamp(int id, String name, String year, String country, String category) {
        this.id = id; this.name = name; this.year = year;
        this.country = country; this.category = category;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String toString() { return id + " - " + name + " (" + year + ") - " + country + " [" + category + "]"; }
}

// Stamp Types
class DefinitiveStamp extends Stamp {
    private double denomination;
    public DefinitiveStamp(int id, String name, String year, String country, double denom) {
        super(id, name, year, country, "Definitive"); this.denomination = denom;
    }
    public String toString() { return super.toString() + " - " + denomination + "p"; }
}

class CommemoratveStamp extends Stamp {
    private String theme;
    public CommemoratveStamp(int id, String name, String year, String country, String theme) {
        super(id, name, year, country, "Commemorative"); this.theme = theme;
    }
    public String toString() { return super.toString() + " - Theme: " + theme; }
}

class UsedStamp extends Stamp {
    private String postmark;
    public UsedStamp(int id, String name, String year, String country, String postmark) {
        super(id, name, year, country, "Used"); this.postmark = postmark;
    }
    public String toString() { return super.toString() + " - Postmark: " + postmark; }
}

class MintStamp extends Stamp {
    private boolean hinged;
    public MintStamp(int id, String name, String year, String country, boolean hinged) {
        super(id, name, year, country, "Mint"); this.hinged = hinged;
    }
    public String toString() { return super.toString() + " - " + (hinged ? "Hinged" : "Unhinged"); }
}

// UserData Class
class UserData {
    private User user;
    private List<Stamp> owned = new ArrayList<>();
    private List<Stamp> wishlist = new ArrayList<>();
    public UserData(User u) { user = u; }
    public User getUser() { return user; }
    public List<Stamp> getOwned() { return owned; }
    public List<Stamp> getWishlist() { return wishlist; }
    public void addOwned(Stamp s) { owned.add(s); }
    public void removeOwned(Stamp s) { owned.remove(s); }
    public void addWishlist(Stamp s) { wishlist.add(s); }
    public void removeWishlist(Stamp s) { wishlist.remove(s); }
}

// Database Manager
class DatabaseManager {
    private List<Stamp> stamps = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<UserData> userDatas = new ArrayList<>();
    
    public DatabaseManager() {
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        stamps.add(new DefinitiveStamp(1, "King George VI", "1937", "UK", 0.5));
        stamps.add(new DefinitiveStamp(2, "Queen Elizabeth II", "1953", "UK", 1.0));
        stamps.add(new CommemoratveStamp(3, "Space Exploration", "1969", "USA", "Apollo 11"));
        stamps.add(new CommemoratveStamp(4, "Royal Wedding", "1981", "UK", "Charles & Diana"));
        stamps.add(new UsedStamp(5, "Victorian Era", "1880", "UK", "London"));
        stamps.add(new UsedStamp(6, "Early 1900s", "1905", "France", "Paris"));
        stamps.add(new MintStamp(7, "Modern UK", "2020", "UK", false));
        stamps.add(new MintStamp(8, "Olympic Games", "2016", "Brazil", true));
        
        users.add(new User("collector1", "pass123"));
        users.add(new User("collector2", "pass456"));
        
        for (User u : users) {
            UserData ud = new UserData(u);
            userDatas.add(ud);
        }
        
        userDatas.get(0).addOwned(stamps.get(0));
        userDatas.get(0).addOwned(stamps.get(1));
        userDatas.get(0).addWishlist(stamps.get(2));
        userDatas.get(1).addOwned(stamps.get(3));
        userDatas.get(1).addWishlist(stamps.get(4));
    }
    
    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }
    
    public void registerUser(String username, String password) {
        if (userExists(username)) return;
        User u = new User(username, password);
        users.add(u);
        userDatas.add(new UserData(u));
    }
    
    public boolean userExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }
    
    public UserData getUserData(String username) {
        for (UserData ud : userDatas) {
            if (ud.getUser().getUsername().equals(username)) return ud;
        }
        return null;
    }
    
    public List<Stamp> getStampsByCategory(String category) {
        List<Stamp> result = new ArrayList<>();
        for (Stamp s : stamps) {
            if (s.getCategory().equalsIgnoreCase(category)) result.add(s);
        }
        return result;
    }
    
    public List<Stamp> getAllStamps() { return stamps; }
}

// Main Application
public class StampCollectionManagerSimple extends JFrame {
    private DatabaseManager db = new DatabaseManager();
    private User currentUser;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    
    public StampCollectionManagerSimple() {
        setTitle("Newcastle Philatelist Club - Stamp Collection Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createMenuPanel(), "MENU");
        mainPanel.add(createStampsPanel(), "STAMPS");
        mainPanel.add(createCollectionPanel("owned"), "OWNED");
        mainPanel.add(createCollectionPanel("wishlist"), "WISHLIST");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Newcastle Philatelist Club");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("Stamp Collection Manager - Login");
        gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(subtitleLabel, gbc);
        
        gbc.gridwidth = 1; gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User user = db.authenticate(username, password);
            if (user != null) {
                currentUser = user;
                cardLayout.show(mainPanel, "MENU");
            } else {
                messageLabel.setText("Invalid credentials!");
            }
        });
        gbc.gridwidth = 1; gbc.gridy = 5; gbc.gridx = 0;
        panel.add(loginButton, gbc);
        
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password");
                return;
            }
            if (db.userExists(username)) {
                messageLabel.setText("Username already exists");
            } else {
                db.registerUser(username, password);
                messageLabel.setText("Registration successful!");
                messageLabel.setForeground(Color.GREEN);
            }
        });
        gbc.gridx = 1;
        panel.add(registerButton, gbc);
        
        return panel;
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + (currentUser != null ? currentUser.getUsername() : "User") + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);
        
        JLabel menuLabel = new JLabel("Main Menu");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 1;
        panel.add(menuLabel, gbc);
        
        JButton[] buttons = new JButton[4];
        String[] labels = {"Manage Stamps", "My Stamp Collection", "My Wishlist", "Logout"};
        String[] panels = {"STAMPS", "OWNED", "WISHLIST", "LOGIN"};
        
        for (int i = 0; i < 4; i++) {
            buttons[i] = new JButton(labels[i]);
            buttons[i].setPreferredSize(new Dimension(200, 50));
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            final int index = i;
            buttons[i].addActionListener(e -> {
                if (index == 3) currentUser = null;
                cardLayout.show(mainPanel, panels[index]);
            });
            gbc.gridy = i + 2; gbc.gridwidth = 2;
            panel.add(buttons[i], gbc);
        }
        
        return panel;
    }
    
    private JPanel createStampsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.add(new JLabel("Select Category:"));
        
        DefaultListModel<Stamp> listModel = new DefaultListModel<>();
        JList<Stamp> stampList = new JList<>(listModel);
        
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"Definitive", "Commemorative", "Used", "Mint"});
        categoryCombo.addActionListener(e -> {
            listModel.clear();
            for (Stamp s : db.getStampsByCategory((String) categoryCombo.getSelectedItem())) {
                listModel.addElement(s);
            }
        });
        topPanel.add(categoryCombo);
        panel.add(topPanel, BorderLayout.NORTH);
        
        panel.add(new JScrollPane(stampList), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(240, 248, 255));
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCollectionPanel(String type) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(240, 248, 255));
        String title = type.equals("owned") ? "My Stamp Collection" : "My Wishlist";
        topPanel.add(new JLabel(title));
        panel.add(topPanel, BorderLayout.NORTH);
        
        DefaultListModel<Stamp> listModel = new DefaultListModel<>();
        JList<Stamp> stampList = new JList<>(listModel);
        
        JComboBox<Stamp> addCombo = new JComboBox<>();
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");
        
        addButton.addActionListener(e -> {
            if (currentUser != null && addCombo.getSelectedItem() != null) {
                Stamp stamp = (Stamp) addCombo.getSelectedItem();
                UserData ud = db.getUserData(currentUser.getUsername());
                if (type.equals("owned")) ud.addOwned(stamp);
                else ud.addWishlist(stamp);
                refreshList(type, listModel);
            }
        });
        
        removeButton.addActionListener(e -> {
            Stamp stamp = stampList.getSelectedValue();
            if (stamp != null && currentUser != null) {
                UserData ud = db.getUserData(currentUser.getUsername());
                if (type.equals("owned")) ud.removeOwned(stamp);
                else ud.removeWishlist(stamp);
                refreshList(type, listModel);
            }
        });
        
        panel.add(new JScrollPane(stampList), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(240, 248, 255));
        bottomPanel.add(new JLabel("Add:"));
        bottomPanel.add(addCombo);
        bottomPanel.add(addButton);
        bottomPanel.add(removeButton);
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void refreshList(String type, DefaultListModel<Stamp> listModel) {
        listModel.clear();
        if (currentUser != null) {
            UserData ud = db.getUserData(currentUser.getUsername());
            List<Stamp> stamps = type.equals("owned") ? ud.getOwned() : ud.getWishlist();
            for (Stamp s : stamps) listModel.addElement(s);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StampCollectionManagerSimple app = new StampCollectionManagerSimple();
            app.setVisible(true);
        });
    }
}

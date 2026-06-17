package ui;

import database.DatabaseManager;
import models.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main application frame.
 * Demonstrates GUI design and user interaction management.
 */
public class MainFrame extends JFrame {
    private DatabaseManager dbManager;
    private User currentUser;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("Newcastle Philatelist Club - Stamp Collection Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        dbManager = DatabaseManager.getInstance();
        dbManager.loadAllData();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add panels
        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new MainMenuPanel(this), "MENU");
        mainPanel.add(new StampCategoryPanel(this), "STAMPS");
        mainPanel.add(new UserCollectionPanel(this, "owned"), "OWNED");
        mainPanel.add(new UserCollectionPanel(this, "wishlist"), "WISHLIST");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void switchPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public DatabaseManager getDatabase() {
        return dbManager;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

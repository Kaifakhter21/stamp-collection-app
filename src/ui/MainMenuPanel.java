package ui;

import models.User;

import javax.swing.*;
import java.awt.*;

/**
 * Main menu panel - homepage of the application.
 */
public class MainMenuPanel extends JPanel {
    private MainFrame mainFrame;

    public MainMenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome Label
        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        // Update welcome label with current user
        User currentUser = mainFrame.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
        }

        // Menu Title
        JLabel menuLabel = new JLabel("Main Menu");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 1;
        add(menuLabel, gbc);

        // Manage Stamps Button
        JButton manageStampsButton = new JButton("Manage Stamps");
        manageStampsButton.setPreferredSize(new Dimension(200, 50));
        manageStampsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        manageStampsButton.addActionListener(e -> mainFrame.switchPanel("STAMPS"));
        gbc.gridy = 2;
        add(manageStampsButton, gbc);

        // My Collection Button
        JButton myCollectionButton = new JButton("My Stamp Collection");
        myCollectionButton.setPreferredSize(new Dimension(200, 50));
        myCollectionButton.setFont(new Font("Arial", Font.PLAIN, 14));
        myCollectionButton.addActionListener(e -> mainFrame.switchPanel("OWNED"));
        gbc.gridy = 3;
        add(myCollectionButton, gbc);

        // Wishlist Button
        JButton wishlistButton = new JButton("My Wishlist");
        wishlistButton.setPreferredSize(new Dimension(200, 50));
        wishlistButton.setFont(new Font("Arial", Font.PLAIN, 14));
        wishlistButton.addActionListener(e -> mainFrame.switchPanel("WISHLIST"));
        gbc.gridy = 4;
        add(wishlistButton, gbc);

        // Logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(200, 50));
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setBackground(new Color(255, 100, 100));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            mainFrame.setCurrentUser(null);
            mainFrame.getDatabase().saveAllData();
            mainFrame.switchPanel("LOGIN");
        });
        gbc.gridy = 5;
        add(logoutButton, gbc);
    }
}

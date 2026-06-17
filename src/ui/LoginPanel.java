package ui;

import database.DatabaseManager;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Login panel for user authentication.
 */
public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Newcastle Philatelist Club");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Stamp Collection Manager - Login");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        add(subtitleLabel, gbc);

        // Username Label
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        add(new JLabel("Username:"), gbc);

        // Username Field
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Password:"), gbc);

        // Password Field
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Message Label
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(loginButton, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        gbc.gridx = 1;
        add(registerButton, gbc);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        DatabaseManager dbManager = mainFrame.getDatabase();
        User user = dbManager.authenticateUser(username, password);

        if (user != null) {
            mainFrame.setCurrentUser(user);
            mainFrame.switchPanel("MENU");
            clearFields();
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username and password");
            return;
        }

        DatabaseManager dbManager = mainFrame.getDatabase();
        if (dbManager.userExists(username)) {
            messageLabel.setText("Username already exists");
        } else {
            dbManager.registerUser(username, password);
            messageLabel.setText("Registration successful! Please login.");
            messageLabel.setForeground(Color.GREEN);
            clearFields();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
    }
}

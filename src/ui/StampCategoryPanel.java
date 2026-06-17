package ui;

import database.DatabaseManager;
import models.Stamp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel to display stamps by category.
 */
public class StampCategoryPanel extends JPanel {
    private MainFrame mainFrame;
    private JComboBox<String> categoryCombo;
    private JList<Stamp> stampList;
    private DefaultListModel<Stamp> listModel;

    public StampCategoryPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel with Title and Category Selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(240, 248, 255));
        JLabel titleLabel = new JLabel("Manage Stamps - Select Category:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(titleLabel);

        categoryCombo = new JComboBox<>(new String[]{"Definitive", "Commemorative", "Used", "Mint"});
        categoryCombo.addActionListener(e -> updateStampList());
        topPanel.add(categoryCombo);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel with Stamp List
        listModel = new DefaultListModel<>();
        stampList = new JList<>(listModel);
        stampList.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(stampList);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with Navigation
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(240, 248, 255));
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainFrame.switchPanel("MENU"));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        updateStampList();
    }

    private void updateStampList() {
        listModel.clear();
        String selectedCategory = (String) categoryCombo.getSelectedItem();
        DatabaseManager dbManager = mainFrame.getDatabase();
        List<Stamp> stamps = dbManager.getStampsByCategory(selectedCategory);
        for (Stamp s : stamps) {
            listModel.addElement(s);
        }
    }
}

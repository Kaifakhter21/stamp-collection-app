package ui;

import database.DatabaseManager;
import models.Stamp;
import models.UserData;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing user's owned stamps and wishlist.
 */
public class UserCollectionPanel extends JPanel {
    private MainFrame mainFrame;
    private String collectionType; // "owned" or "wishlist"
    private JList<Stamp> stampList;
    private DefaultListModel<Stamp> listModel;
    private JComboBox<Stamp> addStampCombo;

    public UserCollectionPanel(MainFrame mainFrame, String collectionType) {
        this.mainFrame = mainFrame;
        this.collectionType = collectionType;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String title = collectionType.equals("owned") ? "My Stamp Collection" : "My Wishlist";

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(240, 248, 255));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel with List
        listModel = new DefaultListModel<>();
        stampList = new JList<>(listModel);
        stampList.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(stampList);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel with Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(240, 248, 255));

        // Add Stamp Section
        JLabel addLabel = new JLabel("Add Stamp:");
        bottomPanel.add(addLabel);
        addStampCombo = new JComboBox<>();
        bottomPanel.add(addStampCombo);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> handleAddStamp());
        bottomPanel.add(addButton);

        // Remove Button
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(e -> handleRemoveStamp());
        bottomPanel.add(removeButton);

        // Back Button
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> mainFrame.switchPanel("MENU"));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            refreshList();
        }
    }

    private void refreshList() {
        listModel.clear();
        UserData userData = mainFrame.getDatabase().getUserData(mainFrame.getCurrentUser().getUsername());

        if (userData != null) {
            List<Stamp> stamps = collectionType.equals("owned") 
                ? userData.getOwnedStamps() 
                : userData.getWishlistStamps();
            for (Stamp s : stamps) {
                listModel.addElement(s);
            }
        }

        // Update combo box
        DatabaseManager dbManager = mainFrame.getDatabase();
        addStampCombo.removeAllItems();
        for (Stamp s : dbManager.getAllStamps()) {
            addStampCombo.addItem(s);
        }
    }

    private void handleAddStamp() {
        if (addStampCombo.getSelectedItem() == null) return;

        Stamp selectedStamp = (Stamp) addStampCombo.getSelectedItem();
        String username = mainFrame.getCurrentUser().getUsername();
        DatabaseManager dbManager = mainFrame.getDatabase();

        if (collectionType.equals("owned")) {
            dbManager.addStampToUserOwned(username, selectedStamp.getId());
        } else {
            dbManager.addStampToUserWishlist(username, selectedStamp.getId());
        }

        refreshList();
    }

    private void handleRemoveStamp() {
        Stamp selectedStamp = stampList.getSelectedValue();
        if (selectedStamp == null) {
            JOptionPane.showMessageDialog(this, "Please select a stamp to remove");
            return;
        }

        String username = mainFrame.getCurrentUser().getUsername();
        DatabaseManager dbManager = mainFrame.getDatabase();

        if (collectionType.equals("owned")) {
            dbManager.removeStampFromUserOwned(username, selectedStamp.getId());
        } else {
            dbManager.removeStampFromUserWishlist(username, selectedStamp.getId());
        }

        refreshList();
    }
}

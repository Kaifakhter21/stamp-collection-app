import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Digital Clock Application that displays current time in different time zones.
 * Features:
 * - Real-time clock updates
 * - Multiple time zone support
 * - 12-hour and 24-hour format toggle
 * - Add/Remove custom time zones
 * - Clean and intuitive GUI
 */
public class DigitalClockApp extends JFrame {
    private JPanel clockPanel;
    private JPanel timeZonePanel;
    private JButton addTimeZoneButton;
    private JButton removeTimeZoneButton;
    private JButton toggleFormatButton;
    private JLabel formatLabel;
    private boolean is24HourFormat = true;
    private Timer updateTimer;

    public DigitalClockApp() {
        setTitle("Digital Clock - Multiple Time Zones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(new Color(20, 20, 40));

        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(20, 20, 40));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top Panel - Title and Format Toggle
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 40));

        JLabel titleLabel = new JLabel("World Clock");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(100, 200, 255));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setBackground(new Color(20, 20, 40));

        formatLabel = new JLabel("Format: 24-Hour");
        formatLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        formatLabel.setForeground(Color.WHITE);
        controlPanel.add(formatLabel);

        toggleFormatButton = new JButton("Toggle 12/24");
        toggleFormatButton.setBackground(new Color(50, 100, 150));
        toggleFormatButton.setForeground(Color.WHITE);
        toggleFormatButton.setFocusPainted(false);
        toggleFormatButton.addActionListener(e -> toggleTimeFormat());
        controlPanel.add(toggleFormatButton);

        topPanel.add(controlPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel - Clock Display
        clockPanel = new JPanel();
        clockPanel.setLayout(new GridLayout(0, 2, 15, 15));
        clockPanel.setBackground(new Color(20, 20, 40));
        clockPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(clockPanel);
        scrollPane.setBackground(new Color(20, 20, 40));
        scrollPane.getViewport().setBackground(new Color(20, 20, 40));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel - Add/Remove Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(20, 20, 40));

        addTimeZoneButton = new JButton("+ Add Time Zone");
        addTimeZoneButton.setBackground(new Color(50, 150, 100));
        addTimeZoneButton.setForeground(Color.WHITE);
        addTimeZoneButton.setFocusPainted(false);
        addTimeZoneButton.setFont(new Font("Arial", Font.BOLD, 12));
        addTimeZoneButton.addActionListener(e -> addNewTimeZone());
        bottomPanel.add(addTimeZoneButton);

        removeTimeZoneButton = new JButton("- Remove Time Zone");
        removeTimeZoneButton.setBackground(new Color(200, 80, 80));
        removeTimeZoneButton.setForeground(Color.WHITE);
        removeTimeZoneButton.setFocusPainted(false);
        removeTimeZoneButton.setFont(new Font("Arial", Font.BOLD, 12));
        removeTimeZoneButton.addActionListener(e -> removeTimeZone());
        bottomPanel.add(removeTimeZoneButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Initialize with default time zones
        addDefaultTimeZones();

        // Start update timer
        startClockUpdate();
    }

    /**
     * Add default time zones
     */
    private void addDefaultTimeZones() {
        addTimeZone("America/New_York", "New York (EST)");
        addTimeZone("Europe/London", "London (GMT)");
        addTimeZone("Asia/Tokyo", "Tokyo (JST)");
        addTimeZone("Australia/Sydney", "Sydney (AEDT)");
    }

    /**
     * Add a new time zone clock
     */
    private void addTimeZone(String timeZoneId, String displayName) {
        ClockDisplay clock = new ClockDisplay(timeZoneId, displayName, is24HourFormat);
        clockPanel.add(clock);
        clockPanel.revalidate();
        clockPanel.repaint();
    }

    /**
     * Add new time zone via dialog
     */
    private void addNewTimeZone() {
        String[] timeZones = TimeZone.getAvailableIDs();
        JComboBox<String> comboBox = new JComboBox<>(timeZones);
        
        int result = JOptionPane.showConfirmDialog(
            this,
            comboBox,
            "Select a Time Zone",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String selected = (String) comboBox.getSelectedItem();
            addTimeZone(selected, selected);
        }
    }

    /**
     * Remove the last time zone
     */
    private void removeTimeZone() {
        if (clockPanel.getComponentCount() > 0) {
            clockPanel.remove(clockPanel.getComponentCount() - 1);
            clockPanel.revalidate();
            clockPanel.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No time zones to remove!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Toggle between 12-hour and 24-hour format
     */
    private void toggleTimeFormat() {
        is24HourFormat = !is24HourFormat;
        formatLabel.setText("Format: " + (is24HourFormat ? "24-Hour" : "12-Hour"));
        
        // Update all clocks with new format
        for (Component comp : clockPanel.getComponents()) {
            if (comp instanceof ClockDisplay) {
                ((ClockDisplay) comp).setFormat(is24HourFormat);
            }
        }
    }

    /**
     * Start the clock update timer
     */
    private void startClockUpdate() {
        updateTimer = new Timer(1000, e -> {
            for (Component comp : clockPanel.getComponents()) {
                if (comp instanceof ClockDisplay) {
                    ((ClockDisplay) comp).updateTime();
                }
            }
        });
        updateTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DigitalClockApp app = new DigitalClockApp();
            app.setVisible(true);
        });
    }
}

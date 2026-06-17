import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ClockDisplay component that shows time for a specific time zone.
 */
public class ClockDisplay extends JPanel {
    private String timeZoneId;
    private String displayName;
    private boolean is24HourFormat;
    private JLabel timeLabel;
    private JLabel zoneLabel;
    private JLabel dateLabel;

    public ClockDisplay(String timeZoneId, String displayName, boolean is24HourFormat) {
        this.timeZoneId = timeZoneId;
        this.displayName = displayName;
        this.is24HourFormat = is24HourFormat;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(40, 40, 70));
        setBorder(BorderFactory.createLineBorder(new Color(100, 150, 200), 2));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Zone Name Label
        zoneLabel = new JLabel(displayName);
        zoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        zoneLabel.setForeground(new Color(100, 200, 255));
        zoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(zoneLabel);

        add(Box.createVerticalStrut(10));

        // Time Label
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Courier New", Font.BOLD, 32));
        timeLabel.setForeground(new Color(0, 255, 100));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(timeLabel);

        add(Box.createVerticalStrut(8));

        // Date Label
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        dateLabel.setForeground(new Color(150, 150, 200));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(dateLabel);

        // Initial update
        updateTime();
    }

    /**
     * Update the time display
     */
    public void updateTime() {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZoneId));
            
            // Format time
            String timeFormat = is24HourFormat ? "HH:mm:ss" : "hh:mm:ss a";
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
            String timeString = zonedDateTime.format(timeFormatter);
            timeLabel.setText(timeString);

            // Format date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");
            String dateString = zonedDateTime.format(dateFormatter);
            dateLabel.setText(dateString);
        } catch (Exception e) {
            timeLabel.setText("Error");
            dateLabel.setText("Invalid Time Zone");
        }
    }

    /**
     * Set time format (12-hour or 24-hour)
     */
    public void setFormat(boolean is24Hour) {
        this.is24HourFormat = is24Hour;
        updateTime();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}

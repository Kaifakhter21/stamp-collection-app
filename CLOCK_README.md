# Digital Clock Application

## Overview
A Java Swing application that displays the current time in multiple time zones with a modern GUI.

## Features

✅ **Real-time Clock Updates**
- Updates every second automatically
- Shows current time, date, and day of week

✅ **Multiple Time Zone Support**
- Pre-loaded with 4 major time zones (New York, London, Tokyo, Sydney)
- Add any time zone from 500+ available zones
- Remove time zones dynamically

✅ **Format Toggle**
- Switch between 12-hour (AM/PM) and 24-hour format
- Applies to all time zone clocks instantly

✅ **Modern UI Design**
- Dark theme with bright text for easy viewing
- Color-coded displays (green time, cyan zone names)
- Responsive grid layout (2 columns)
- Smooth scrolling for multiple clocks

## How to Run

### Step 1: Compile
```bash
javac DigitalClockApp.java ClockDisplay.java
```

### Step 2: Run
```bash
java DigitalClockApp
```

## Features Explained

### Default Time Zones
1. **New York (EST)** - Eastern Standard Time
2. **London (GMT)** - Greenwich Mean Time
3. **Tokyo (JST)** - Japan Standard Time
4. **Sydney (AEDT)** - Australian Eastern Daylight Time

### Button Functions

**+ Add Time Zone**
- Opens dropdown with all available time zones
- Select any zone to add it to the display

**- Remove Time Zone**
- Removes the last added time zone
- Cannot remove if no zones exist

**Toggle 12/24**
- Switches between 12-hour and 24-hour format
- Updates all clocks instantly

## Code Structure

### DigitalClockApp.java
- Main application frame
- Manages multiple ClockDisplay components
- Handles user interactions (add/remove/toggle)
- Runs update timer for real-time updates

### ClockDisplay.java
- Individual clock display for one time zone
- Shows time, date, and zone name
- Supports format switching
- Uses Java Time API (ZonedDateTime)

## Technical Details

**Java Version**: Java 8+
**Frameworks**: Swing, Java Time API
**Update Frequency**: 1 second
**Time Zones Supported**: 500+ (all IANA time zones)

## Example Usage

1. Launch the application
2. See 4 clocks with different time zones
3. Click "Toggle 12/24" to change format
4. Click "+ Add Time Zone" to add more clocks
5. Click "- Remove Time Zone" to remove the last clock
6. Watch as all clocks update in real-time

## Customization

To add different default time zones, modify `addDefaultTimeZones()` in DigitalClockApp.java:

```java
private void addDefaultTimeZones() {
    addTimeZone("America/Los_Angeles", "Los Angeles (PST)");
    addTimeZone("Asia/Dubai", "Dubai (GST)");
    // Add more...
}
```

## License
Free to use and modify.

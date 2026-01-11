package net.bopbopstudios.timer_hud.commands;

import net.bopbopstudios.timer_hud.notification.NotificationManager;
import net.bopbopstudios.timer_hud.notification.NotificationStorage;
import net.bopbopstudios.timer_hud.utils.DurationParser;

import java.time.Duration;

/**
 * Handles the logic for notification-related commands.
 * Processes user input and coordinates between NotificationManager and NotificationStorage.
 */
public class NotificationCommandLogic {
    
    private final NotificationManager notificationManager;
    private final NotificationStorage notificationStorage;
    
    public NotificationCommandLogic(NotificationManager notificationManager, 
                                   NotificationStorage notificationStorage) {
        this.notificationManager = notificationManager;
        this.notificationStorage = notificationStorage;
    }
    
    /**
     * Handles notification command input from user.
     * 
     * @param timerId the current timer ID
     * @param argument the command argument (duration, "off", or "status")
     * @return user-friendly response message
     */
    public String handleNotifyCommand(String timerId, String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            return showUsage();
        }
        
        argument = argument.trim().toLowerCase();
        
        switch (argument) {
            case "off":
                return disableNotification(timerId);
            case "status":
                return showStatus();
            default:
                return setNotification(timerId, argument);
        }
    }
    
    private String setNotification(String timerId, String durationStr) {
        try {
            Duration duration = DurationParser.parse(durationStr);
            
            // Set in manager
            notificationManager.setNotificationDuration(duration);
            
            // Save to storage
            notificationStorage.saveNotificationDuration(timerId, duration);
            
            return "Notification set for " + formatDuration(duration);
            
        } catch (IllegalArgumentException e) {
            return "Invalid duration format. Use formats like: 30m, 1h30m, 2h";
        }
    }
    
    private String disableNotification(String timerId) {
        notificationManager.clearNotification();
        notificationStorage.clearNotificationDuration(timerId);
        return "Notifications disabled";
    }
    
    private String showStatus() {
        if (notificationManager.isNotificationSet()) {
            Duration duration = notificationManager.getNotificationDuration();
            return "Notification set for " + formatDuration(duration);
        } else {
            return "No notification set";
        }
    }
    
    private String showUsage() {
        return "Usage: /playtimer notify <duration|off|status>\n" +
               "Examples: /playtimer notify 30m, /playtimer notify 1h30m, /playtimer notify off";
    }
    
    /**
     * Formats a duration in a user-friendly way.
     * 
     * @param duration the duration to format
     * @return formatted string like "1 hour 30 minutes"
     */
    private String formatDuration(Duration duration) {
        long totalSeconds = duration.getSeconds();
        
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        
        StringBuilder result = new StringBuilder();
        
        if (hours > 0) {
            result.append(hours).append(hours == 1 ? " hour" : " hours");
        }
        
        if (minutes > 0) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(minutes).append(minutes == 1 ? " minute" : " minutes");
        }
        
        if (seconds > 0 && hours == 0) { // Only show seconds if no hours
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(seconds).append(seconds == 1 ? " second" : " seconds");
        }
        
        return result.toString();
    }
}
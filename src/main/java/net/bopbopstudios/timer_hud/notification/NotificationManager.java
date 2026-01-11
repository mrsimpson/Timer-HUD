package net.bopbopstudios.timer_hud.notification;

import java.time.Duration;

/**
 * Manages timer notifications for the Minecraft Timer plugin.
 * Handles setting notification durations, checking trigger conditions,
 * and preventing duplicate notifications.
 */
public class NotificationManager {
    
    private Duration notificationDuration;
    private boolean notificationShown;
    
    /**
     * Creates a new NotificationManager with no notification set.
     */
    public NotificationManager() {
        this.notificationDuration = null;
        this.notificationShown = false;
    }
    
    /**
     * Sets the duration after which a notification should be shown.
     * 
     * @param duration the duration to wait before showing notification
     */
    public void setNotificationDuration(Duration duration) {
        this.notificationDuration = duration;
        this.notificationShown = false; // Reset shown status for new notification
    }
    
    /**
     * Gets the currently set notification duration.
     * 
     * @return the notification duration, or null if not set
     */
    public Duration getNotificationDuration() {
        return notificationDuration;
    }
    
    /**
     * Checks if a notification duration is currently set.
     * 
     * @return true if a notification is set, false otherwise
     */
    public boolean isNotificationSet() {
        return notificationDuration != null;
    }
    
    /**
     * Clears the current notification setting.
     */
    public void clearNotification() {
        this.notificationDuration = null;
        this.notificationShown = false;
    }
    
    /**
     * Determines if a notification should be triggered based on current playtime.
     * Will only trigger once per notification setting until reset.
     * 
     * @param currentPlaytime the current playtime duration
     * @return true if notification should be shown, false otherwise
     */
    public boolean shouldTriggerNotification(Duration currentPlaytime) {
        if (!isNotificationSet() || notificationShown) {
            return false;
        }
        
        return currentPlaytime.compareTo(notificationDuration) >= 0;
    }
    
    /**
     * Marks the current notification as having been shown.
     * Prevents the same notification from triggering again.
     */
    public void markNotificationShown() {
        this.notificationShown = true;
    }
}
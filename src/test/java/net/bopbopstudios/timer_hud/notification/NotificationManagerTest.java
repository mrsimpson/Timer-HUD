package net.bopbopstudios.timer_hud.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

@DisplayName("NotificationManager Tests")
public class NotificationManagerTest {
    
    private NotificationManager manager;
    
    @BeforeEach
    public void setUp() {
        manager = new NotificationManager();
    }
    
    @Test
    @DisplayName("Should set notification duration")
    public void testSetNotificationDuration() {
        Duration duration = Duration.ofMinutes(30);
        manager.setNotificationDuration(duration);
        
        assertEquals(duration, manager.getNotificationDuration());
    }
    
    @Test
    @DisplayName("Should indicate when notification is not set")
    public void testNotificationNotSet() {
        assertFalse(manager.isNotificationSet());
        assertNull(manager.getNotificationDuration());
    }
    
    @Test
    @DisplayName("Should indicate when notification is set")
    public void testNotificationSet() {
        manager.setNotificationDuration(Duration.ofMinutes(60));
        assertTrue(manager.isNotificationSet());
    }
    
    @Test
    @DisplayName("Should clear notification")
    public void testClearNotification() {
        manager.setNotificationDuration(Duration.ofMinutes(30));
        assertTrue(manager.isNotificationSet());
        
        manager.clearNotification();
        assertFalse(manager.isNotificationSet());
        assertNull(manager.getNotificationDuration());
    }
    
    @Test
    @DisplayName("Should check if notification should trigger")
    public void testShouldTriggerNotification() {
        Duration notificationDuration = Duration.ofMinutes(30);
        manager.setNotificationDuration(notificationDuration);
        
        // Current playtime less than notification duration - should not trigger
        Duration currentPlaytime = Duration.ofMinutes(20);
        assertFalse(manager.shouldTriggerNotification(currentPlaytime));
        
        // Current playtime equal to notification duration - should trigger
        currentPlaytime = Duration.ofMinutes(30);
        assertTrue(manager.shouldTriggerNotification(currentPlaytime));
        
        // Current playtime greater than notification duration - should trigger
        currentPlaytime = Duration.ofMinutes(35);
        assertTrue(manager.shouldTriggerNotification(currentPlaytime));
    }
    
    @Test
    @DisplayName("Should not trigger when notification is not set")
    public void testShouldNotTriggerWhenNotSet() {
        Duration currentPlaytime = Duration.ofMinutes(100);
        assertFalse(manager.shouldTriggerNotification(currentPlaytime));
    }
    
    @Test
    @DisplayName("Should track if notification was already shown")
    public void testNotificationShownTracking() {
        Duration notificationDuration = Duration.ofMinutes(30);
        manager.setNotificationDuration(notificationDuration);
        
        Duration currentPlaytime = Duration.ofMinutes(35);
        
        // First time - should trigger
        assertTrue(manager.shouldTriggerNotification(currentPlaytime));
        
        // Mark as shown
        manager.markNotificationShown();
        
        // Should not trigger again for same session
        assertFalse(manager.shouldTriggerNotification(currentPlaytime));
    }
    
    @Test
    @DisplayName("Should reset notification shown status on new notification")
    public void testResetNotificationShownOnNewNotification() {
        manager.setNotificationDuration(Duration.ofMinutes(30));
        manager.markNotificationShown();
        
        // Setting new notification should reset shown status
        manager.setNotificationDuration(Duration.ofMinutes(60));
        Duration currentPlaytime = Duration.ofMinutes(65);
        assertTrue(manager.shouldTriggerNotification(currentPlaytime));
    }
}
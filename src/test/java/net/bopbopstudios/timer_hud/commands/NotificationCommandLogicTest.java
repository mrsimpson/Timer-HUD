package net.bopbopstudios.timer_hud.commands;

import net.bopbopstudios.timer_hud.notification.NotificationManager;
import net.bopbopstudios.timer_hud.notification.NotificationStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.time.Duration;

@DisplayName("NotificationCommandLogic Tests")
public class NotificationCommandLogicTest {
    
    @TempDir
    File tempDir;
    
    private NotificationCommandLogic commandLogic;
    private NotificationManager mockManager;
    private NotificationStorage storage;
    private String testTimerId;
    
    @BeforeEach
    public void setUp() {
        mockManager = mock(NotificationManager.class);
        storage = new NotificationStorage(tempDir);
        testTimerId = "MP(test-server:25565,test-player)";
        commandLogic = new NotificationCommandLogic(mockManager, storage);
    }
    
    @Test
    @DisplayName("Should parse and set notification duration")
    public void testSetNotificationDuration() {
        String result = commandLogic.handleNotifyCommand(testTimerId, "30m");
        
        verify(mockManager).setNotificationDuration(Duration.ofMinutes(30));
        assertEquals("Notification set for 30 minutes", result);
    }
    
    @Test
    @DisplayName("Should handle complex duration formats")
    public void testComplexDurationFormat() {
        String result = commandLogic.handleNotifyCommand(testTimerId, "2h15m");
        
        Duration expected = Duration.ofHours(2).plusMinutes(15);
        verify(mockManager).setNotificationDuration(expected);
        assertEquals("Notification set for 2 hours 15 minutes", result);
    }
    
    @Test
    @DisplayName("Should turn off notifications")
    public void testTurnOffNotifications() {
        String result = commandLogic.handleNotifyCommand(testTimerId, "off");
        
        verify(mockManager).clearNotification();
        assertEquals("Notifications disabled", result);
    }
    
    @Test
    @DisplayName("Should show status when notification is set")
    public void testStatusWithNotificationSet() {
        when(mockManager.isNotificationSet()).thenReturn(true);
        when(mockManager.getNotificationDuration()).thenReturn(Duration.ofMinutes(45));
        
        String result = commandLogic.handleNotifyCommand(testTimerId, "status");
        
        assertEquals("Notification set for 45 minutes", result);
    }
    
    @Test
    @DisplayName("Should show status when notification is not set")
    public void testStatusWithoutNotification() {
        when(mockManager.isNotificationSet()).thenReturn(false);
        
        String result = commandLogic.handleNotifyCommand(testTimerId, "status");
        
        assertEquals("No notification set", result);
    }
    
    @Test
    @DisplayName("Should handle invalid duration format")
    public void testInvalidDurationFormat() {
        String result = commandLogic.handleNotifyCommand(testTimerId, "invalid");
        
        assertTrue(result.startsWith("Invalid duration format"));
        verify(mockManager, never()).setNotificationDuration(any());
    }
    
    @Test
    @DisplayName("Should save notification duration to storage")
    public void testSaveToStorage() {
        commandLogic.handleNotifyCommand(testTimerId, "1h");
        
        Duration saved = storage.getNotificationDuration(testTimerId);
        assertEquals(Duration.ofHours(1), saved);
    }
    
    @Test
    @DisplayName("Should clear notification from storage")
    public void testClearFromStorage() {
        // First set a notification
        storage.saveNotificationDuration(testTimerId, Duration.ofMinutes(30));
        
        // Then clear it
        commandLogic.handleNotifyCommand(testTimerId, "off");
        
        Duration saved = storage.getNotificationDuration(testTimerId);
        assertNull(saved);
    }
    
    @Test
    @DisplayName("Should format duration nicely for user feedback")
    public void testDurationFormatting() {
        String result1 = commandLogic.handleNotifyCommand(testTimerId, "1h");
        assertEquals("Notification set for 1 hour", result1);
        
        String result2 = commandLogic.handleNotifyCommand(testTimerId, "90m");
        assertEquals("Notification set for 1 hour 30 minutes", result2);
        
        String result3 = commandLogic.handleNotifyCommand(testTimerId, "45s");
        assertEquals("Notification set for 45 seconds", result3);
    }
}
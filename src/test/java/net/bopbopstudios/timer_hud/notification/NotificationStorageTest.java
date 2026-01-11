package net.bopbopstudios.timer_hud.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.Duration;

@DisplayName("NotificationStorage Tests")
public class NotificationStorageTest {
    
    @TempDir
    File tempDir;
    
    private NotificationStorage storage;
    private String testTimerId;
    
    @BeforeEach
    public void setUp() {
        storage = new NotificationStorage(tempDir);
        testTimerId = "MP(test-server:25565,test-player)";
    }
    
    @Test
    @DisplayName("Should save and load notification duration")
    public void testSaveAndLoadNotificationDuration() {
        Duration duration = Duration.ofMinutes(45);
        
        storage.saveNotificationDuration(testTimerId, duration);
        Duration loaded = storage.getNotificationDuration(testTimerId);
        
        assertEquals(duration, loaded);
    }
    
    @Test
    @DisplayName("Should return null for non-existent timer")
    public void testNonExistentTimer() {
        Duration loaded = storage.getNotificationDuration("non-existent-timer");
        assertNull(loaded);
    }
    
    @Test
    @DisplayName("Should handle clearing notification duration")
    public void testClearNotificationDuration() {
        Duration duration = Duration.ofHours(2);
        
        // Save duration
        storage.saveNotificationDuration(testTimerId, duration);
        assertEquals(duration, storage.getNotificationDuration(testTimerId));
        
        // Clear duration
        storage.clearNotificationDuration(testTimerId);
        assertNull(storage.getNotificationDuration(testTimerId));
    }
    
    @Test
    @DisplayName("Should persist data across storage instances")
    public void testPersistenceAcrossInstances() {
        Duration duration = Duration.ofMinutes(90);
        
        // Save with first instance
        storage.saveNotificationDuration(testTimerId, duration);
        
        // Create new instance with same directory
        NotificationStorage newStorage = new NotificationStorage(tempDir);
        Duration loaded = newStorage.getNotificationDuration(testTimerId);
        
        assertEquals(duration, loaded);
    }
    
    @Test
    @DisplayName("Should handle multiple timer IDs")
    public void testMultipleTimerIds() {
        String timerId1 = "MP(server1:25565,player1)";
        String timerId2 = "SP(SingleplayerWorld,player2)";
        
        Duration duration1 = Duration.ofMinutes(30);
        Duration duration2 = Duration.ofHours(1);
        
        storage.saveNotificationDuration(timerId1, duration1);
        storage.saveNotificationDuration(timerId2, duration2);
        
        assertEquals(duration1, storage.getNotificationDuration(timerId1));
        assertEquals(duration2, storage.getNotificationDuration(timerId2));
    }
    
    @Test
    @DisplayName("Should overwrite existing notification duration")
    public void testOverwriteExisting() {
        Duration originalDuration = Duration.ofMinutes(30);
        Duration newDuration = Duration.ofMinutes(60);
        
        storage.saveNotificationDuration(testTimerId, originalDuration);
        assertEquals(originalDuration, storage.getNotificationDuration(testTimerId));
        
        storage.saveNotificationDuration(testTimerId, newDuration);
        assertEquals(newDuration, storage.getNotificationDuration(testTimerId));
    }
}
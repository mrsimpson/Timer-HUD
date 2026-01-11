package net.bopbopstudios.timer_hud.notification;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles persistent storage of notification settings for different timer IDs.
 * Uses standard Java IO instead of Guava for better compatibility.
 */
public class SimpleNotificationStorage {
    
    private final File storageDirectory;
    private final File notificationFile;
    private final String separator = " -> ";
    private Map<String, Duration> notificationDurations;
    
    public SimpleNotificationStorage(File storageDirectory) {
        this.storageDirectory = storageDirectory;
        this.notificationFile = new File(storageDirectory, "notifications.txt");
        this.notificationDurations = new HashMap<>();
        readFile();
    }
    
    public void saveNotificationDuration(String timerId, Duration duration) {
        notificationDurations.put(timerId, duration);
        writeFile();
    }
    
    public Duration getNotificationDuration(String timerId) {
        return notificationDurations.get(timerId);
    }
    
    public void clearNotificationDuration(String timerId) {
        notificationDurations.remove(timerId);
        writeFile();
    }
    
    private void readFile() {
        notificationDurations.clear();
        
        if (!notificationFile.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(notificationFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(separator, 2);
                if (parts.length == 2) {
                    String timerId = parts[0];
                    try {
                        long millis = Long.parseLong(parts[1]);
                        Duration duration = Duration.ofMillis(millis);
                        notificationDurations.put(timerId, duration);
                    } catch (NumberFormatException e) {
                        // Skip invalid lines
                    }
                }
            }
        } catch (IOException e) {
            // Failed to read file, start with empty data
        }
    }
    
    private void writeFile() {
        try {
            storageDirectory.mkdirs();
            
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(notificationFile), StandardCharsets.UTF_8))) {
                for (Map.Entry<String, Duration> entry : notificationDurations.entrySet()) {
                    String timerId = entry.getKey();
                    long millis = entry.getValue().toMillis();
                    writer.write(timerId + separator + millis + "\n");
                }
                writer.flush();
            }
        } catch (IOException e) {
            // Failed to write file
        }
    }
}
package net.bopbopstudios.timer_hud.utils;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing duration strings like "2h30m", "90s", etc.
 * Used for timer notification configuration.
 */
public class DurationParser {
    
    private static final Pattern DURATION_PATTERN = Pattern.compile(
        "(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?"
    );
    
    /**
     * Parses a duration string into a Duration object.
     * Supported formats: "2h", "30m", "90s", "1h30m", "2h15m30s"
     * 
     * @param input the duration string to parse
     * @return Duration object representing the parsed duration
     * @throws IllegalArgumentException if the input is invalid
     */
    public static Duration parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Duration string cannot be null or empty");
        }
        
        input = input.trim().toLowerCase();
        Matcher matcher = DURATION_PATTERN.matcher(input);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid duration format: " + input);
        }
        
        String hoursStr = matcher.group(1);
        String minutesStr = matcher.group(2);
        String secondsStr = matcher.group(3);
        
        // Check if at least one group matched
        if (hoursStr == null && minutesStr == null && secondsStr == null) {
            throw new IllegalArgumentException("Invalid duration format: " + input);
        }
        
        long totalSeconds = 0;
        
        if (hoursStr != null) {
            totalSeconds += Long.parseLong(hoursStr) * 3600;
        }
        
        if (minutesStr != null) {
            totalSeconds += Long.parseLong(minutesStr) * 60;
        }
        
        if (secondsStr != null) {
            totalSeconds += Long.parseLong(secondsStr);
        }
        
        return Duration.ofSeconds(totalSeconds);
    }
}
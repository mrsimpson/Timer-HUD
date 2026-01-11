package net.bopbopstudios.timer_hud.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

@DisplayName("DurationParser Tests - Simplified")
public class SimpleDurationParserTest {
    
    @Test
    @DisplayName("Should parse simple hours format")
    public void testParseSimpleHours() {
        Duration result = DurationParser.parse("2h");
        assertEquals(Duration.ofHours(2), result);
    }
    
    @Test
    @DisplayName("Should parse simple minutes format") 
    public void testParseSimpleMinutes() {
        Duration result = DurationParser.parse("30m");
        assertEquals(Duration.ofMinutes(30), result);
    }
    
    @Test
    @DisplayName("Should parse combined hours and minutes")
    public void testParseCombinedHoursAndMinutes() {
        Duration result = DurationParser.parse("1h30m");
        assertEquals(Duration.ofHours(1).plusMinutes(30), result);
    }
    
    @Test
    @DisplayName("Should throw exception for invalid format")
    public void testInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DurationParser.parse("invalid");
        });
    }
}
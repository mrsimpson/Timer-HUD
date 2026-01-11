package net.bopbopstudios.timer_hud.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

@DisplayName("DurationParser Tests")
public class DurationParserTest {
    
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
    @DisplayName("Should parse seconds format")
    public void testParseSeconds() {
        Duration result = DurationParser.parse("90s");
        assertEquals(Duration.ofSeconds(90), result);
    }
    
    @Test
    @DisplayName("Should handle complex combined format")
    public void testComplexCombinedFormat() {
        Duration result = DurationParser.parse("2h15m30s");
        assertEquals(Duration.ofHours(2).plusMinutes(15).plusSeconds(30), result);
    }
    
    @Test
    @DisplayName("Should throw exception for invalid format")
    public void testInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DurationParser.parse("invalid");
        });
    }
    
    @Test
    @DisplayName("Should throw exception for null input")
    public void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            DurationParser.parse(null);
        });
    }
    
    @Test
    @DisplayName("Should throw exception for empty input")
    public void testEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            DurationParser.parse("");
        });
    }
}
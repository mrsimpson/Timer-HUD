package net.bopbopstudios.timer_hud.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Renders notification overlays on screen when timer alerts are triggered.
 * Provides visual feedback with fade-in/fade-out animations.
 */
public class AlarmOverlay {
    
    private final MinecraftClient minecraft;
    private String notificationText;
    private LocalDateTime showStartTime;
    private Duration displayDuration;
    private boolean isShowing;
    
    // Animation settings
    private static final Duration FADE_IN_DURATION = Duration.ofMillis(500);
    private static final Duration FADE_OUT_DURATION = Duration.ofMillis(1000);
    private static final Duration DEFAULT_DISPLAY_DURATION = Duration.ofSeconds(5);
    
    // Colors
    private static final int BACKGROUND_COLOR = 0x80000000; // Semi-transparent black
    private static final int TEXT_COLOR = 0xFFFF5555; // Red text
    private static final int BORDER_COLOR = 0xFFFF0000; // Red border
    
    public AlarmOverlay() {
        this.minecraft = MinecraftClient.getInstance();
        this.isShowing = false;
    }
    
    /**
     * Shows a notification overlay with the specified message.
     * 
     * @param message the notification message to display
     */
    public void showNotification(String message) {
        showNotification(message, DEFAULT_DISPLAY_DURATION);
    }
    
    /**
     * Shows a notification overlay with a custom display duration.
     * 
     * @param message the notification message to display
     * @param displayDuration how long to show the notification
     */
    public void showNotification(String message, Duration displayDuration) {
        this.notificationText = message;
        this.showStartTime = LocalDateTime.now();
        this.displayDuration = displayDuration;
        this.isShowing = true;
    }
    
    /**
     * Hides the current notification.
     */
    public void hideNotification() {
        this.isShowing = false;
        this.notificationText = null;
    }
    
    /**
     * Checks if a notification is currently being shown.
     * 
     * @return true if showing, false otherwise
     */
    public boolean isShowing() {
        return isShowing && notificationText != null;
    }
    
    /**
     * Renders the notification overlay if one is active.
     * Should be called from the main render loop.
     * 
     * @param context the draw context for rendering
     */
    public void render(DrawContext context) {
        if (!isShowing() || minecraft.textRenderer == null) {
            return;
        }
        
        LocalDateTime now = LocalDateTime.now();
        Duration timeSinceStart = Duration.between(showStartTime, now);
        
        // Check if notification should still be visible
        Duration totalDuration = displayDuration.plus(FADE_OUT_DURATION);
        if (timeSinceStart.compareTo(totalDuration) > 0) {
            hideNotification();
            return;
        }
        
        // Calculate alpha for fade animation
        float alpha = calculateAlpha(timeSinceStart);
        if (alpha <= 0) {
            return;
        }
        
        MatrixStack matrices = context.getMatrices();
        Window window = minecraft.getWindow();
        
        // Calculate position (center of screen)
        int textWidth = minecraft.textRenderer.getWidth(notificationText);
        int textHeight = minecraft.textRenderer.fontHeight;
        
        int centerX = window.getScaledWidth() / 2;
        int centerY = window.getScaledHeight() / 3; // Upper third of screen
        
        // Notification box dimensions
        int padding = 20;
        int boxWidth = textWidth + (padding * 2);
        int boxHeight = textHeight + (padding * 2);
        
        int boxX = centerX - (boxWidth / 2);
        int boxY = centerY - (boxHeight / 2);
        
        // Apply scaling for prominence
        matrices.push();
        float scale = 1.5f;
        matrices.scale(scale, scale, scale);
        
        // Adjust positions for scaling
        int scaledBoxX = Math.round(boxX / scale);
        int scaledBoxY = Math.round(boxY / scale);
        int scaledBoxWidth = Math.round(boxWidth / scale);
        int scaledBoxHeight = Math.round(boxHeight / scale);
        int scaledTextX = Math.round((centerX - textWidth / 2) / scale);
        int scaledTextY = Math.round((centerY - textHeight / 2) / scale);
        
        // Apply alpha to colors
        int backgroundColorWithAlpha = applyAlpha(BACKGROUND_COLOR, alpha);
        int textColorWithAlpha = applyAlpha(TEXT_COLOR, alpha);
        int borderColorWithAlpha = applyAlpha(BORDER_COLOR, alpha);
        
        // Draw background
        context.fill(scaledBoxX, scaledBoxY, 
                    scaledBoxX + scaledBoxWidth, scaledBoxY + scaledBoxHeight,
                    backgroundColorWithAlpha);
        
        // Draw border
        drawBorder(context, scaledBoxX, scaledBoxY, scaledBoxWidth, scaledBoxHeight, 
                  borderColorWithAlpha);
        
        // Draw text
        context.drawCenteredTextWithShadow(minecraft.textRenderer, 
                                         Text.literal(notificationText),
                                         scaledTextX + scaledBoxWidth / 2, 
                                         scaledTextY + scaledBoxHeight / 2 - minecraft.textRenderer.fontHeight / 2,
                                         textColorWithAlpha);
        
        matrices.pop();
    }
    
    /**
     * Calculates the alpha value for fade animation.
     * 
     * @param timeSinceStart time since notification started showing
     * @return alpha value between 0.0 and 1.0
     */
    private float calculateAlpha(Duration timeSinceStart) {
        long totalDisplayMillis = displayDuration.toMillis();
        long fadeInMillis = FADE_IN_DURATION.toMillis();
        long fadeOutMillis = FADE_OUT_DURATION.toMillis();
        long elapsedMillis = timeSinceStart.toMillis();
        
        if (elapsedMillis < fadeInMillis) {
            // Fade in phase
            return (float) elapsedMillis / fadeInMillis;
        } else if (elapsedMillis < totalDisplayMillis) {
            // Full visibility phase
            return 1.0f;
        } else if (elapsedMillis < totalDisplayMillis + fadeOutMillis) {
            // Fade out phase
            long fadeOutElapsed = elapsedMillis - totalDisplayMillis;
            return 1.0f - ((float) fadeOutElapsed / fadeOutMillis);
        } else {
            // Fully faded out
            return 0.0f;
        }
    }
    
    /**
     * Applies alpha transparency to a color.
     * 
     * @param color the original color (ARGB format)
     * @param alpha alpha value between 0.0 and 1.0
     * @return color with applied alpha
     */
    private int applyAlpha(int color, float alpha) {
        int originalAlpha = (color >> 24) & 0xFF;
        int newAlpha = Math.round(originalAlpha * alpha);
        return (color & 0x00FFFFFF) | (newAlpha << 24);
    }
    
    /**
     * Draws a border around a rectangle.
     */
    private void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        int borderWidth = 2;
        
        // Top border
        context.fill(x, y, x + width, y + borderWidth, color);
        // Bottom border  
        context.fill(x, y + height - borderWidth, x + width, y + height, color);
        // Left border
        context.fill(x, y, x + borderWidth, y + height, color);
        // Right border
        context.fill(x + width - borderWidth, y, x + width, y + height, color);
    }
}
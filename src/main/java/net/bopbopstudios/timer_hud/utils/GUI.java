package net.bopbopstudios.timer_hud.utils;

import com.mojang.authlib.GameProfile;
import net.bopbopstudios.timer_hud.client.TimerHUDClient;
import net.bopbopstudios.timer_hud.gui.AlarmOverlay;
import net.bopbopstudios.timer_hud.notification.NotificationManager;
import net.bopbopstudios.timer_hud.notification.NotificationStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class GUI extends Screen {

    MinecraftClient minecraft;
    private static final Logger LOGGER;
    private static GUI INSTANCE;
    boolean running = false;
    Duration startDuration = null;
    LocalDateTime startTime = null;
    boolean forceRefresh = false;
    boolean isPaused = false;
    LocalDateTime pauseStartTime = null;
    Duration pauseDuration;
    boolean isInit;
    String timerId;
    int counter;
    int initialCounter;
    boolean oldPauseScreenState;
    
    // Notification system components
    private NotificationManager notificationManager;
    private NotificationStorage notificationStorage;
    private AlarmOverlay alarmOverlay;
    
    protected GUI(Text title) {
        super(title);
        this.pauseDuration = Duration.ZERO;
        this.isInit = false;
        this.counter = 5000;
        this.initialCounter = this.counter;
        this.oldPauseScreenState = false;
        this.minecraft = MinecraftClient.getInstance();
        
        // Initialize notification system
        this.notificationManager = new NotificationManager();
        this.notificationStorage = new NotificationStorage(new File("."));
        this.alarmOverlay = new AlarmOverlay();
    }

    public static GUI getInstance() {
        if (INSTANCE == null) {
            LOGGER.debug("Creating instance!");
            INSTANCE = new GUI(Text.literal("Timer HUD"));
        }

        return INSTANCE;
    }
    public static GUI getMaybeInstance() {
        return INSTANCE;
    }
    public static void removeInstance() {
        if (INSTANCE != null) {
            LOGGER.debug("Cleaning up instance!...");
            INSTANCE.cleanUp();
        }

        INSTANCE = null;
    }
    public void checkStats() {
        if (this.minecraft.player != null) {
            if (!this.running || this.forceRefresh) {
                Duration savedDuration = Storage.getInstance().getDuration(this.timerId);
                if (savedDuration == null || this.forceRefresh) {
                    int numberOfTicks = this.minecraft.player.getStatHandler().getStat(Stats.CUSTOM,Stats.PLAY_TIME);
                    int seconds = numberOfTicks / 20;
                    savedDuration = Duration.ofSeconds((long)seconds);
                    Storage.getInstance().saveDuration(this.timerId, savedDuration);
                    this.forceRefresh = false;
                }

                this.startDuration = savedDuration;
                this.pauseDuration = Duration.ZERO;
                this.startTime = LocalDateTime.now();
                this.running = true;
            }

        }
    }
    public void requestStatsRefresh() {
        ClientStatusC2SPacket packet = new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.REQUEST_STATS);
        ClientPlayNetworkHandler network = this.minecraft.getNetworkHandler();
        if (network != null) {
            network.sendPacket(packet);
        }
    }

    public Duration getPlayTime() {
        if (!this.running) {
            return null;
        } else {
            Duration timePaused = Duration.ZERO;
            LocalDateTime now = LocalDateTime.now();
            if (this.minecraft.isPaused()) {
                if (!this.isPaused) {
                    this.pauseStartTime = LocalDateTime.now();
                    this.counter = -1;
                }

                timePaused = Duration.between(this.pauseStartTime, now);
                this.isPaused = true;
            } else {
                if (this.isPaused) {
                    this.pauseDuration = this.pauseDuration.plus(Duration.between(this.pauseStartTime, now));
                }

                this.isPaused = false;
            }

            return Duration.between(this.startTime, now).plus(this.startDuration).minus(timePaused).minus(this.pauseDuration);
        }
    }
    public void reset() {
        this.startTime = LocalDateTime.now();
        this.startDuration = Duration.ZERO;
        this.pauseDuration = Duration.ZERO;
        Storage.getInstance().writeFile();
    }

    public void syncWithServer() {
        this.requestStatsRefresh();
        this.forceRefresh = true;
    }
    public void init() {
        if (!this.isInit) {
            IntegratedServer iServer = this.minecraft.getServer();
            ServerInfo sInfo = this.minecraft.getCurrentServerEntry();
            String playmode = null;
            String worldName = null;
            String playerName = null;
            if (this.minecraft.player != null) {
                GameProfile profile = this.minecraft.player.getGameProfile();
                if (profile == null) {
                    playerName = this.minecraft.player.getEntityName();
                } else {
                    playerName = profile.getId().toString();
                }
            }

            if (playerName == null) {
                playerName = "unknown";
            }

            if (iServer != null) {
                worldName = iServer.getSaveProperties().getLevelName();
                playmode = "SP";
            } else if (sInfo != null) {
                String address = sInfo.address;
                String[] parts = address.split(":");
                if (parts.length == 1) {
                    worldName = parts[0] + ":25565";
                } else if (parts.length == 2) {
                    worldName = parts[0] + ":" + parts[1];
                }

                playmode = "MP";
            }

            if (playmode == null) {
                this.timerId = "undefined";
            } else {
                this.timerId = playmode + "(" + worldName + "," + playerName + ")";
            }

            this.requestStatsRefresh();
            this.isInit = true;
        }
    }
    public void updateDurationInStorage(Duration duration) {
        if (duration != null) {
            Storage.getInstance().saveDuration(this.timerId, duration);
            this.counter = this.initialCounter;
        }
    }
    private void cleanUp() {
        Duration duration = this.getPlayTime();
        this.updateDurationInStorage(duration);
    }
    
    // Notification system getters
    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
    
    public NotificationStorage getNotificationStorage() {
        return notificationStorage;
    }
    
    public AlarmOverlay getAlarmOverlay() {
        return alarmOverlay;
    }
    
    public String getTimerId() {
        return timerId;
    }
    
    /**
     * Initializes notification settings from storage for the current timer.
     */
    private void initNotifications() {
        if (timerId != null && notificationStorage != null) {
            Duration savedNotification = notificationStorage.getNotificationDuration(timerId);
            if (savedNotification != null) {
                notificationManager.setNotificationDuration(savedNotification);
            }
        }
    }
    
    /**
     * Checks if a notification should be triggered and shows it if needed.
     */
    private void checkAndTriggerNotification(Duration currentPlaytime) {
        if (notificationManager.shouldTriggerNotification(currentPlaytime)) {
            String message = "Timer Alert: You've been playing for " + formatDurationForNotification(currentPlaytime);
            alarmOverlay.showNotification(message);
            notificationManager.markNotificationShown();
        }
    }
    
    /**
     * Formats a duration for display in notifications.
     */
    private String formatDurationForNotification(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        
        if (hours > 0) {
            if (minutes > 0) {
                return String.format("%d hour%s %d minute%s", 
                    hours, hours == 1 ? "" : "s",
                    minutes, minutes == 1 ? "" : "s");
            } else {
                return String.format("%d hour%s", hours, hours == 1 ? "" : "s");
            }
        } else {
            return String.format("%d minute%s", minutes, minutes == 1 ? "" : "s");
        }
    }
    public void render(DrawContext context) {
        if (this.minecraft != null && this.minecraft.player != null && this.minecraft.world != null && this.minecraft.player.getWorld() != null) {
            this.init();
            MatrixStack stack = context.getMatrices();
            Duration duration = this.getPlayTime();
            if (duration != null) {
                if (!this.minecraft.isPaused()) {
                    --this.counter;
                }

                boolean refresh = false;
                boolean pauseScreenState = this.minecraft.currentScreen != null;
                if (this.oldPauseScreenState != pauseScreenState && pauseScreenState) {
                    refresh = true;
                }

                this.oldPauseScreenState = pauseScreenState;
                if (this.counter <= 0 || refresh) {
                    this.updateDurationInStorage(duration);
                }

                // Initialize notifications when timer ID is available
                if (this.isInit && this.timerId != null && !this.forceRefresh) {
                    initNotifications();
                }
                
                // Check for notification triggers
                checkAndTriggerNotification(duration);

                String hms = String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
                if (this.forceRefresh) {
                    hms = "??:??:??";
                }

                if (TimerHUDClient.timerVisible) {
                    Objects.requireNonNull(this.minecraft.textRenderer);
                    Window mainWindow = this.minecraft.getWindow();
                    float scale = 1.5f;
                    float xoffset = 2f;
                    float yoffset = 1.92f;
                    int yneed = 0,xneed = 0;
                    if(xoffset < 1) {
                        xneed = -this.minecraft.textRenderer.getWidth(hms);
                    } else if (xoffset == 1) {
                        xneed = 0;
                    } else if (xoffset > 1) {
                        xneed = this.minecraft.textRenderer.getWidth(hms);
                    }

                    if(xoffset < 1) {
                        yneed = -9;
                    } else if (xoffset == 1) {
                        yneed = 0;
                    } else if (xoffset > 1) {
                        yneed = 9;
                    }

                    int xpos = Math.round((((float)mainWindow.getScaledWidth() / 2) * xoffset) - xneed * scale);
                    int ypos = Math.round((((float)mainWindow.getScaledHeight() / 2) * yoffset) - yneed * scale);
                    int scaledxpos = Math.round(xpos / scale);
                    int scaledypos = Math.round(ypos / scale);
                    stack.push();
                    stack.scale(scale,scale,scale);
                    context.drawCenteredTextWithShadow(this.minecraft.textRenderer, Text.literal(hms), scaledxpos, scaledypos, 0xff5555);
                    stack.pop();
                }
                
                // Always render alarm overlay (it handles its own visibility)
                alarmOverlay.render(context);
            }
        }
    }

    static {
        LOGGER = TimerHUDClient.LOGGER;
        INSTANCE = null;
    }
}

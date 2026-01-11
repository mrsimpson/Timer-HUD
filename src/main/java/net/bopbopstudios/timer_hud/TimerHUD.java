package net.bopbopstudios.timer_hud;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.bopbopstudios.timer_hud.client.TimerHUDClient.MOD_ID;

/**
 * Main mod initializer for Minecraft Timer plugin.
 * Handles server-side initialization (currently minimal).
 */
public class TimerHUD implements ModInitializer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Server-side initialization
        LOGGER.info("Minecraft Timer plugin initialized on server");
        
        // Note: The main functionality is client-side in TimerHUDClient
        // This class is kept for mod structure compatibility
    }
}

package net.bopbopstudios.timer_hud.client;

import net.bopbopstudios.timer_hud.TimerHUD;
import net.bopbopstudios.timer_hud.utils.Storage;
import net.fabricmc.api.ClientModInitializer;

import static net.minecraft.server.command.CommandManager.literal;
import net.bopbopstudios.timer_hud.commands.NotifyCommand;
import net.bopbopstudios.timer_hud.commands.ResetCommand;
import net.bopbopstudios.timer_hud.commands.SyncCommand;
import net.bopbopstudios.timer_hud.commands.ToggleCommand;
import net.bopbopstudios.timer_hud.commands.HelpCommand;
import java.io.File;
import java.io.IOException;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerHUDClient implements ClientModInitializer {
    public static final String MOD_ID = "timer_hud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static File hiddenfile = new File(".timer_hud-hidden");
    public static boolean timerVisible = true;

    @Override
    public void onInitializeClient() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        Storage storage = Storage.getInstance();
        storage.readFile();

        LOGGER.info("Timer HUD Loaded");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("playtimer")
                .then(literal("help")
                        .executes(context -> {
                            new HelpCommand(context);
                            //context.getSource().sendFeedback(() -> Text.literal("Help"), false);
                            return 1;
                        })
                ).then(literal("reset")
                        .executes(context -> {
                            new ResetCommand(context);
                            //context.getSource().sendFeedback(() -> Text.literal("Reset"), false);
                            return 1;
                        })
                ).then(literal("sync")
                        .executes(context -> {
                            new SyncCommand(context);
                            //context.getSource().sendFeedback(() -> Text.literal("Sync"), false);
                            return 1;
                        })
                ).then(literal("toggle")
                        .executes(context -> {
                            new ToggleCommand(context);
                            //context.getSource().sendFeedback(() -> Text.literal("Toggle"), false);
                            return 1;
                        })
                ).then(literal("notify")
                        .then(CommandManager.argument("duration", com.mojang.brigadier.arguments.StringArgumentType.greedyString())
                                .executes(context -> {
                                    String argument = com.mojang.brigadier.arguments.StringArgumentType.getString(context, "duration");
                                    new NotifyCommand(context, argument);
                                    return 1;
                                })
                        )
                        .executes(context -> {
                            new NotifyCommand(context, "status");
                            return 1;
                        })
                )
        ));

        loadVisible();
    }
    public static boolean toggle() {
        timerVisible = !timerVisible;
        saveVisible();
        return timerVisible;
    }

    public static void loadVisible() {
        timerVisible = !hiddenfile.exists();
    }

    public static void saveVisible() {
        if (timerVisible) {
            if (hiddenfile.exists()) {
                hiddenfile.delete();
            }
        } else if (!hiddenfile.exists()) {
            try {
                hiddenfile.createNewFile();
            } catch (IOException var1) {
                LOGGER.error("Could not update timer visibility status...");
            }
        }

    }
}

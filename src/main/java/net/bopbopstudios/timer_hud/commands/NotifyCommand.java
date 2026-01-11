package net.bopbopstudios.timer_hud.commands;

import com.mojang.brigadier.context.CommandContext;
import net.bopbopstudios.timer_hud.commands.NotificationCommandLogic;
import net.bopbopstudios.timer_hud.utils.GUI;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 * Command handler for /playtimer notify subcommand.
 * Integrates with the GUI notification system.
 */
public class NotifyCommand {
    
    public NotifyCommand(CommandContext<ServerCommandSource> context, String argument) {
        GUI gui = GUI.getMaybeInstance();
        
        if (gui == null) {
            sendResponse(context, "Timer not active. Join a world first.");
            return;
        }
        
        // Get current timer ID (this should match the timer ID generation in GUI)
        String timerId = getCurrentTimerId(gui);
        if (timerId == null) {
            sendResponse(context, "Unable to determine current timer ID.");
            return;
        }
        
        // Use the notification command logic to process the command
        NotificationCommandLogic commandLogic = new NotificationCommandLogic(
            gui.getNotificationManager(),
            gui.getNotificationStorage()
        );
        
        String response = commandLogic.handleNotifyCommand(timerId, argument);
        sendResponse(context, response);
    }
    
    private String getCurrentTimerId(GUI gui) {
        String timerId = gui.getTimerId();
        if (timerId != null && !timerId.trim().isEmpty()) {
            return timerId;
        }
        
        // Fallback if timer ID is not yet initialized
        return "current-session";
    }
    
    private void sendResponse(CommandContext<ServerCommandSource> context, String message) {
        context.getSource().sendFeedback(() -> Text.literal(message), false);
    }
}
package net.bopbopstudios.timer_hud.commands;

import com.mojang.brigadier.context.CommandContext;
import net.bopbopstudios.timer_hud.client.TimerHUDClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ToggleCommand {
    public ToggleCommand(CommandContext<ServerCommandSource> context) {
        //FabricClientCommandSource source = (FabricClientCommandSource)context.getSource();
        boolean newStatus = TimerHUDClient.toggle();
        if (newStatus) {
            context.getSource().sendFeedback(() -> Text.literal("playtimer.show"), false);
            //source.sendFeedback(Text.of("playtimer.show"));
        } else {
            context.getSource().sendFeedback(() -> Text.literal("playtimer.hide"), false);
            //source.sendFeedback(Text.of("playtimer.hide"));
        }
    }
}

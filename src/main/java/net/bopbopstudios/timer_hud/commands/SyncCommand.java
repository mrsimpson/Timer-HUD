package net.bopbopstudios.timer_hud.commands;

import com.mojang.brigadier.context.CommandContext;
import net.bopbopstudios.timer_hud.utils.GUI;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SyncCommand {
    public SyncCommand(CommandContext<ServerCommandSource> context) {
        //FabricClientCommandSource source = (FabricClientCommandSource)context.getSource();
        GUI gui = GUI.getMaybeInstance();
        if (gui != null) {
            gui.syncWithServer();
        }
        context.getSource().sendFeedback(() -> Text.literal("Sync"), false);
        //source.sendFeedback(Text.of("playtimer.sync"));
    }
}

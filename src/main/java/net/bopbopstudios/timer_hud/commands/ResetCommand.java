package net.bopbopstudios.timer_hud.commands;

import com.mojang.brigadier.context.CommandContext;
import net.bopbopstudios.timer_hud.utils.GUI;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ResetCommand {
    public ResetCommand(CommandContext<ServerCommandSource> context) {
        GUI gui = GUI.getMaybeInstance();
        if (gui != null) {
            gui.reset();
        }

        context.getSource().sendFeedback(() -> Text.literal("Reset"), false);
    }
}

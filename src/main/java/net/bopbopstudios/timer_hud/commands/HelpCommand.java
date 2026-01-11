package net.bopbopstudios.timer_hud.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class HelpCommand {
    public HelpCommand(CommandContext<ServerCommandSource> context) {
        // Get the source. This will always work.
        final ServerCommandSource source = context.getSource();
        // Unchecked, may be null if the sender was the console or the command block.
        final @Nullable Entity sender = source.getEntity();
        assert sender != null;
        String helpText = "§6Minecraft Timer Commands:\n" +
                         "§f/playtimer help §7- Show this help message\n" +
                         "§f/playtimer reset §7- Reset the current timer\n" +
                         "§f/playtimer sync §7- Sync timer with server stats\n" +
                         "§f/playtimer toggle §7- Toggle timer display on/off\n" +
                         "§f/playtimer notify <duration> §7- Set timer notification\n" +
                         "§f/playtimer notify off §7- Disable notifications\n" +
                         "§f/playtimer notify status §7- Show notification status\n" +
                         "§7Duration examples: 30m, 1h30m, 2h";
        
        sender.sendMessage(Text.literal(helpText));
        //source.sendFeedback(() -> Text.literal("Help"), false);
    }
}

package net.bopbopstudios.timer_hud;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static net.bopbopstudios.timer_hud.client.TimerHUDClient.MOD_ID;

public class TimerHUD implements ModInitializer {
    public static final Identifier DIRT_BROKEN = new Identifier(MOD_ID, "dirt_broken");

    private Integer totalDirtBlocksBroken = 0;

    @Override
    public void onInitialize() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) {
                // Increment the amount of dirt blocks that have been broken
                totalDirtBlocksBroken += 1;

                // Send a packet to the client
                MinecraftServer server = world.getServer();

                PacketByteBuf data = PacketByteBufs.create();
                data.writeInt(totalDirtBlocksBroken);

                ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
                server.execute(() -> {
                    ServerPlayNetworking.send(playerEntity, DIRT_BROKEN, data);
                });
            }
        });
    }
}

package net.bopbopstudios.timer_hud.mixin;

import net.bopbopstudios.timer_hud.utils.GUI;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.StatisticsS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayNetworkHandler.class})
public abstract class NetworkMixin {
    @Inject(
            method = {"onStatistics"},
            at = {@At("TAIL")}
    )
    public void afterStatisticsUpdate(StatisticsS2CPacket packet, CallbackInfo cb) {
        GUI.getInstance().checkStats();
    }
}

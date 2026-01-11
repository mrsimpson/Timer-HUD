package net.bopbopstudios.timer_hud.mixin;

import net.bopbopstudios.timer_hud.utils.GUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftClient.class})
public abstract class DisconnectMixin {
    @Inject(
            method = {"disconnect(Lnet/minecraft/client/gui/screen/Screen;)V"},
            at = {@At("TAIL")}
    )
    private void onDisconnect(Screen screen, CallbackInfo cb) {
        GUI.removeInstance();
    }
}

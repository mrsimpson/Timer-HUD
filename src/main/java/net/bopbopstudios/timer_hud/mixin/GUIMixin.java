package net.bopbopstudios.timer_hud.mixin;

import net.bopbopstudios.timer_hud.utils.GUI;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameHud.class})
public abstract class GUIMixin {
    @Inject(
            method = {"render"},
            at = {@At("HEAD")}
    )
    private void beforeRenderDebugScreen(DrawContext context, float tickDelta, CallbackInfo ci) {
        GUI.getInstance().render(context);
    }
}

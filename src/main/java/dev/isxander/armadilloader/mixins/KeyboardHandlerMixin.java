package dev.isxander.armadilloader.mixins;

import dev.isxander.armadilloader.OverlayInputConsumer;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "keyPress", at = @At("HEAD"))
    private void passKeyPressToOverlay(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (windowPointer != minecraft.getWindow().getWindow()) return;

        if (minecraft.getOverlay() instanceof OverlayInputConsumer inputConsumer) {
            inputConsumer.armadilloader$onKeyPress(key, scanCode, action, modifiers);
        }
    }
}

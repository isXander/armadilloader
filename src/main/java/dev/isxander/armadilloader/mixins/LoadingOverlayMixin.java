package dev.isxander.armadilloader.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.armadilloader.Armadilloader;
import dev.isxander.armadilloader.LoadingOverlayCallback;
import dev.isxander.armadilloader.OverlayInputConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin implements OverlayInputConsumer, LoadingOverlayCallback {
    @Unique
    private final Armadilloader armadilloader = new Armadilloader(this);

    @Unique
    private boolean preventFinish = false;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;setColor(FFFF)V", ordinal = 1))
    private void doArmadilloRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        RenderSystem.defaultBlendFunc();
        armadilloader.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @WrapWithCondition(method = "render", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V"),

    })
    private boolean shouldRenderDefault(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        return false;
    }

    @WrapWithCondition(method = "render", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lnet/minecraft/client/renderer/RenderType;IIIII)V"),

    })
    private boolean shouldRenderDefault2(GuiGraphics instance, RenderType renderType, int minX, int minY, int maxX, int maxY, int color) {
        return false;
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ReloadInstance;isDone()Z"))
    private boolean preventFadeOutBeginning(boolean original) {
        return original && !preventFinish;
    }

    @Override
    public void armadilloader$setPreventFinish(boolean preventFinish) {
        this.preventFinish = preventFinish;
    }

    @Override
    public void armadilloader$onKeyPress(int key, int scanCode, int action, int modifiers) {
        armadilloader.onKeyPress(key, scanCode, action, modifiers);
    }
}

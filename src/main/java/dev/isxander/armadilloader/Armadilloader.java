package dev.isxander.armadilloader;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class Armadilloader {
    private static final SizedTex FOREGROUND = new SizedTex(new ResourceLocation("armadilloader", "textures/fg.png"), 12800, 720);
    private static final SizedTex BACKGROUND = new SizedTex(new ResourceLocation("armadilloader", "textures/sunset.png"), 2000, 720);
    private static final SizedTex PARALLAX = new SizedTex(new ResourceLocation("armadilloader", "textures/paralax.png"), 1280, 720);
    private static final SpriteSheet ARMADILLO = new SpriteSheet(new ResourceLocation("armadilloader", "textures/armadillo.png"), 39, 27, 10);

    private static final int FG_FOOT_Y = 423;

    private final LoadingOverlayCallback callback;

    private float time;

    private float armadilloY = 0;
    private float armadilloVelocity = 0;
    private boolean isJumping = false;
    private boolean startedGame;

    public Armadilloader(LoadingOverlayCallback callback) {
        this.callback = callback;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int armadilloFootY = graphics.guiHeight() / 10 * 6;

        renderDistantBackground(graphics);

        renderScrollingTexture(graphics, PARALLAX, FG_FOOT_Y, armadilloFootY - 25, 1f);
        renderScrollingTexture(graphics, FOREGROUND, FG_FOOT_Y, armadilloFootY, 3f);

        armadilloVelocity -= 3f * partialTick; // gravity
        armadilloY += armadilloVelocity * partialTick;

        if (armadilloY <= 0) {
            armadilloY = 0;
            armadilloVelocity = 0;

            if (isJumping) {
                if (!startedGame)
                    startGame();

                armadilloVelocity = 20;
            }
        }

        renderArmadillo(graphics);

        time += partialTick;
    }

    private void startGame() {
        startedGame = true;
        callback.armadilloader$setPreventFinish(true);
    }

    private void renderDistantBackground(GuiGraphics graphics) {
        float width = graphics.guiHeight() * BACKGROUND.whAspect;
        int xOffset = (int) ((width - graphics.guiWidth()) / 2);

        graphics.blit(BACKGROUND.rl, -xOffset, 0, (int) width, graphics.guiHeight(), 0, 0, BACKGROUND.w, BACKGROUND.h, BACKGROUND.w, BACKGROUND.h);
    }

    private void renderScrollingTexture(GuiGraphics graphics, SizedTex tex, int texAlign, int guiAlign, float speedPxPerSec) {
        int guiAlignHeight = graphics.guiHeight() - guiAlign;
        int texAlignHeight = tex.h - texAlign;

        float sFactor = (float) guiAlignHeight / texAlignHeight;
        float sTextureHeight = tex.h * sFactor;

        float width = sTextureHeight * tex.whAspect;

        float sSpeed = speedPxPerSec / sFactor;

        graphics.blit(tex.rl, 0, (int) Math.ceil(graphics.guiHeight() - sTextureHeight), (int) width, (int)Math.ceil(sTextureHeight), (time * sSpeed) % tex.w, 0, tex.w, tex.h, tex.w, tex.h);
    }

    private void renderArmadillo(GuiGraphics graphics) {
        int frame = (int) (time / 1.5f % ARMADILLO.frameCount);

        int x = (int) (graphics.guiWidth() / 2 - ARMADILLO.w / 2);
        int y = (int) Math.ceil(graphics.guiHeight() / 10f * 6 - ARMADILLO.h - armadilloY) + 1;

        graphics.blit(ARMADILLO.rl, x, y, (int) (ARMADILLO.w), (int) (ARMADILLO.h), ARMADILLO.getUOffset(frame), 0, ARMADILLO.w, ARMADILLO.h, ARMADILLO.atlasWidth, ARMADILLO.h);
    }

    public void onKeyPress(int key, int scanCode, int action, int modifiers) {
        if (key == InputConstants.KEY_SPACE) {
            if (action == GLFW.GLFW_PRESS)
                isJumping = true;
            else if (action == GLFW.GLFW_RELEASE)
                isJumping = false;
        }
    }

    private record SizedTex(ResourceLocation rl, int w, int h, float whAspect) {
        SizedTex(ResourceLocation rl, int w, int h) {
            this(rl, w, h, (float) w / h);
        }
    }

    private record SpriteSheet(ResourceLocation rl, int w, int h, int frameCount, int atlasWidth) {
        SpriteSheet(ResourceLocation rl, int w, int h, int frameCount) {
            this(rl, w, h, frameCount, w * frameCount);
        }

        public int getUOffset(int frame) {
            return frame * w;
        }
    }
}

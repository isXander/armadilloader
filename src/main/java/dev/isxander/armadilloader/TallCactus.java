package dev.isxander.armadilloader;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.resources.ResourceLocation;

public class TallCactus extends Obstacle {
    private static final SizedTex TEX = new SizedTex(new ResourceLocation("armadilloader", "textures/cactus.png"), 24, 44);

    public TallCactus(float x, float floorY) {
        this.x = x;
        this.y = floorY - TEX.h();
    }

    @Override
    public boolean testIntersection(ScreenRectangle armadillo) {
        return armadillo.overlaps(new ScreenRectangle((int) x, (int) y, TEX.w(), TEX.h()));
    }

    @Override
    public void render(GuiGraphics graphics) {
        graphics.blit(TEX.rl(), (int) x, (int) y, 0, 0, TEX.w(), TEX.h(), TEX.w(), TEX.h());
    }

    @Override
    public boolean isOffScreen() {
        return x < -TEX.w();
    }
}

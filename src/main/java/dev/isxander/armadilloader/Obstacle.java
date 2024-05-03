package dev.isxander.armadilloader;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public abstract class Obstacle {
    protected float x, y;

    public void move(float speed, float tickDelta) {
        x -= speed * tickDelta;
    }

    public boolean isOffScreen() {
        return x < -1;
    }

    public abstract boolean testIntersection(ScreenRectangle armadillo);

    public abstract void render(GuiGraphics graphics);
}

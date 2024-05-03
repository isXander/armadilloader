package dev.isxander.armadilloader;

import net.minecraft.resources.ResourceLocation;

record SizedTex(ResourceLocation rl, int w, int h, float whAspect) {
    SizedTex(ResourceLocation rl, int w, int h) {
        this(rl, w, h, (float) w / h);
    }
}

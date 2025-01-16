package net.igneo.icv.client.indicators;

import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import static net.igneo.icv.client.EnchantmentHudOverlay.animTime;

public abstract class EnchantIndicator {
    public int totalFrames;
    public int chargeFrames;
    /*
        the slot variable is backwards, 0 is helmet, and 3 is boots.
     */
    public int slot;
    public ResourceLocation image;
    private int frame = 0;
    private int loopFrame;
    public ArmorEnchantManager manager;
    EnchantIndicator(int totalFrames, int chargeFrames, int loopFrame, int slot, ResourceLocation image, ArmorEnchantManager manager) {
        this.totalFrames = totalFrames;
        this.chargeFrames = chargeFrames;
        this.slot = slot;
        this.image = image;
        this.loopFrame = loopFrame;
        this.manager = manager;
    }

    public int getHeight() {
        return this.totalFrames * 16;
    }

    public int getFrame() {
        if (frame < chargeFrames || this.manager.getCoolDown() > 0) {
            double framePercent = (double) this.manager.getCoolDown() / this.manager.maxCoolDown;
            frame = (int) (chargeFrames * Math.abs((framePercent - 1)));
        } else {
            if (System.currentTimeMillis() >= animTime + 125 && this.manager.canUse()) {
                ++frame;
                if (frame >= totalFrames) {
                    frame = loopFrame;
                }
            }
        }
        if (!this.manager.canUse()) {
            frame = 0;
        }
        return frame;
    }

    public boolean shouldRender() {
        return !this.manager.canUse() || frame <= totalFrames;
    }
}

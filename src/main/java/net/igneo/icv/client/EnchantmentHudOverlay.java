package net.igneo.icv.client;

import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class EnchantmentHudOverlay {
    public static long animTime = 0;

    public static final IGuiOverlay HUD_ENCHANTMENTS = ((gui, poseStack, partialTick, width, height) -> {

        int x = width / 2;
        int y = height;

        Minecraft.getInstance().player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
           for (int slot = 0; slot < 4; ++slot) {
               EnchantIndicator indicator = enchVar.indicators[slot];
               if (indicator != null) {
                   int offset = 16 * indicator.slot;
                   offset = offset > 0 ? ++offset : 0;
                   int pX = x - 94 + offset;
                   int pY = y - 65;
                   if (indicator.shouldRender()) {
                       poseStack.blit(
                               indicator.image,
                               pX,
                               pY,
                               0,
                               16 * indicator.getFrame(),
                               16,
                               16,
                               16,
                               indicator.getHeight());
                   }
               }
           }
        });

        if (animTime == 0) {
            animTime = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() >= animTime + 125) {
            animTime = System.currentTimeMillis();
        }
    });
}

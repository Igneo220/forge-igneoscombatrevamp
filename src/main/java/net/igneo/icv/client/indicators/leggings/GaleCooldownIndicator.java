package net.igneo.icv.client.indicators.leggings;

import net.igneo.icv.ICV;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class GaleCooldownIndicator extends EnchantIndicator {
    private static final ResourceLocation STASIS = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/gale.png");

    public GaleCooldownIndicator(ArmorEnchantManager manager) {
        super(26, 17, 23, 2, STASIS, manager);
    }
}

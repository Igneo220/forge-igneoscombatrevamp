package net.igneo.icv.client.indicators;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class StasisCooldownIndicator extends EnchantIndicator{
    private static final ResourceLocation STASIS = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/stasis.png");
    public StasisCooldownIndicator(ArmorEnchantManager manager) {
        super(41,14,18,3, STASIS, manager);
    }
}

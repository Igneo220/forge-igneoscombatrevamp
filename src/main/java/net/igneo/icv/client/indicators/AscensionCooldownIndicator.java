package net.igneo.icv.client.indicators;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class AscensionCooldownIndicator extends EnchantIndicator {
    private static final ResourceLocation STASIS = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/ascension.png");

    public AscensionCooldownIndicator(ArmorEnchantManager manager) {
        super(23, 17, 19, 3, STASIS, manager);
    }
}

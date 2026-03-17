package net.igneo.icv.client.indicators.leggings;

import net.igneo.icv.ICV;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class JudgementCooldownIndicator extends EnchantIndicator {
    private static final ResourceLocation STASIS = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/judgement.png");

    public JudgementCooldownIndicator(ArmorEnchantManager manager) {
        super(25, 16, 21, 2, STASIS, manager);
    }
}

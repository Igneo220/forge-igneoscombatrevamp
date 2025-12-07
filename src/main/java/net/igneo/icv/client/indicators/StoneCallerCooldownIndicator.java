package net.igneo.icv.client.indicators;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class StoneCallerCooldownIndicator extends EnchantIndicator {
    private static final ResourceLocation STONE_CALLER = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/stonecaller.png");

    public StoneCallerCooldownIndicator(ArmorEnchantManager manager) {
        super(24, 16, 21, 3, STONE_CALLER, manager);
    }
}

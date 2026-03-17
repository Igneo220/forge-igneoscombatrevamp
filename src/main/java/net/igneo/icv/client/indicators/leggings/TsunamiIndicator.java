package net.igneo.icv.client.indicators.leggings;

import net.igneo.icv.ICV;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class TsunamiIndicator extends EnchantIndicator {
    private static final ResourceLocation TSUNAMI = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/tsunami.png");
    public TsunamiIndicator(ArmorEnchantManager manager) {
        super(28,15,22,2, TSUNAMI, manager);
    }
}

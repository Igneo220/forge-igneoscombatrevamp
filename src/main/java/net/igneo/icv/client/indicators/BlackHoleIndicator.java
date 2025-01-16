package net.igneo.icv.client.indicators;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.minecraft.resources.ResourceLocation;

public class BlackHoleIndicator extends EnchantIndicator{
    private static final ResourceLocation BLACK_HOLE = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/black_hole.png");
    public BlackHoleIndicator(ArmorEnchantManager manager) {
        super(27,13,17,0, BLACK_HOLE, manager);
    }
}

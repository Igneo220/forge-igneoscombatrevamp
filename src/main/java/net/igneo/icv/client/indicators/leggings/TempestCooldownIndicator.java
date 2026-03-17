package net.igneo.icv.client.indicators.leggings;

import net.igneo.icv.ICV;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings.TempestManager;
import net.minecraft.resources.ResourceLocation;

public class TempestCooldownIndicator extends EnchantIndicator {
    private static final ResourceLocation STASIS = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/tempest.png");
    private static final ResourceLocation OVERLAY = new ResourceLocation(ICV.MOD_ID,
            "textures/gui/enchantments/tempest_overlay.png");

    @Override
    public ResourceLocation overlay() {
        return OVERLAY;
    }

    @Override
    public int charges() {
        TempestManager tempestManager = (TempestManager) manager;
        return tempestManager.charges;
    }

    @Override
    public int maxCharges() {
        return 3;
    }

    public TempestCooldownIndicator(ArmorEnchantManager manager) {
        super(27, 15, 18, 2, STASIS, manager);
    }
}

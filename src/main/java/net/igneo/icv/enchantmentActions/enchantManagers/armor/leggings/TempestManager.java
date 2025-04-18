package net.igneo.icv.enchantmentActions.enchantManagers.armor.leggings;

import net.igneo.icv.client.indicators.BlackHoleIndicator;
import net.igneo.icv.client.indicators.EnchantIndicator;
import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.entity.player.Player;

public class TempestManager extends ArmorEnchantManager {
    public TempestManager(Player player) {
        super(EnchantType.LEGGINGS, 300, -10, false, player);
    }
    
    @Override
    public void activate() {
        if (player.level().isClientSide) {
            player.setDeltaMovement(ICVUtils.getFlatInputDirection(player.getYRot(), enchVar.input, 1.5F, 0.5));
        }
    }
    
    @Override
    public void onOffCoolDown(Player player) {
    
    }
    
    @Override
    public EnchantIndicator getIndicator() {
        return new BlackHoleIndicator(this);
    }
    
    @Override
    public void resetCoolDown() {
        addCoolDown((maxCoolDown / 3));
    }
    
    @Override
    public boolean isOffCoolDown() {
        return getCoolDown() <= maxCoolDown - (maxCoolDown / 3);
    }
    
    @Override
    public boolean canUse() {
        return true;
    }
}

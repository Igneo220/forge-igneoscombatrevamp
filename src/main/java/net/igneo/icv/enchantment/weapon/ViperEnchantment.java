package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.GustManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.ViperManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ViperEnchantment extends WeaponEnchantment {
    public ViperEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public EnchantmentManager getManager(Player player) {
        return new ViperManager(player);
    }

    @Override
    public boolean performBonusCheck() {
        return true;
    }
}

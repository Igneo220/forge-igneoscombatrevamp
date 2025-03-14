package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.BurstManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.TungstenCoreManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class TungstenCoreEnchantment extends WeaponEnchantment {
    public TungstenCoreEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    @Override
    public EnchantmentManager getManager(Player player) {
        return new TungstenCoreManager(player);
    }
}

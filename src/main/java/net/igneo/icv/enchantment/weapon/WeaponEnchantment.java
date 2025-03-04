package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.BlackHoleManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.WeaponEnchantManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class WeaponEnchantment extends ICVEnchantment {
    protected WeaponEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if (pAttacker instanceof Player player) {
            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (enchVar.getManager(4).getClass() == this.getManager(player).getClass()) {
                    ((WeaponEnchantManager) enchVar.getManager(4)).onAttack(pTarget);
                } else if (enchVar.getManager(5).getClass() == this.getManager(player).getClass()) {
                    ((WeaponEnchantManager) enchVar.getManager(5)).onAttack(pTarget);
                }
            });
        }
    }
}

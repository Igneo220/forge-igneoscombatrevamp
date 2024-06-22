package net.igneo.icv.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import static java.lang.Math.abs;

public class SkeweringEnchantment extends Enchantment {
    public SkeweringEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        if(!pAttacker.onGround() && !pAttacker.isInFluidType() && !pAttacker.isPassenger()){
            pAttacker.hurt(pAttacker.damageSources().playerAttack((Player) pTarget),pTarget.getMainHandItem().getDamageValue() + 4);
        }
        super.doPostAttack(pTarget, pAttacker, pLevel);
    }
}

package net.igneo.icv.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class KineticEnchantment extends Enchantment{
    public static float speedDamage;
    public KineticEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    /*
    public void doPostAttack(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        if(!pAttacker.level().isClientSide) {
            LocalPlayer player = Minecraft.getInstance().player;
            speedDamage = (float) (((Math.abs(player.getDeltaMovement().x) +Math.abs(player.getDeltaMovement().y) + Math.abs(player.getDeltaMovement().z))) * 30);
            if (speedDamage >= 20) {
                speedDamage = 20;
            }
            System.out.println(speedDamage);
            pAttacker.hurt(pTarget.damageSources().playerAttack((Player) pTarget), speedDamage);
        }
        super.doPostHurt(pTarget, pAttacker, pLevel);
    }*/
}

package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BackpedalS2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;

public class BackPedalEnchantment extends Enchantment {
    public static boolean initialHit = false;
    public static boolean hit = false;
    public BackPedalEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if (pAttacker.level() instanceof ServerLevel) {
            ModMessages.sendToPlayer(new BackpedalS2CPacket(),(ServerPlayer) pAttacker);
            pAttacker.addDeltaMovement(new Vec3(pAttacker.getLookAngle().reverse().x,0.2,pAttacker.getLookAngle().reverse().z));
        }
        super.doPostHurt(pAttacker, pTarget, pLevel);
    }
}

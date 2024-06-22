package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MomentumC2SPacket;
import net.igneo.icv.networking.packet.TempoTheftC2SPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class TempoTheftEnchantment extends Enchantment {
    public static int theftCount;
    public static long loseTheft;
    public static LivingEntity thief;
    public static LivingEntity tempoVictim;
    public TempoTheftEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        thief = pAttacker;
        tempoVictim = (LivingEntity) pTarget;
        loseTheft = System.currentTimeMillis();
        ++theftCount;
        ModMessages.sendToServer(new TempoTheftC2SPacket());
        super.doPostAttack(pAttacker, pTarget, pLevel);
    }
/*
    public static void onClientTick() {
        if (thief != null) {
            if (System.currentTimeMillis() >= loseTheft + 3000){
                theftCount = 0;
                ModMessages.sendToServer(new TempoTheftC2SPacket());
                thief = null;
                tempoVictim = null;
            }
        }
    }*/
}

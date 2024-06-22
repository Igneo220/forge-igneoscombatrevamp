package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.GustC2SPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class GustEnchantment extends Enchantment {
    public static LivingEntity gusted;
    public static long gustDelay = 0;
    public GustEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    @Override
    public void doPostAttack(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        if (Minecraft.getInstance().player.fallDistance > 0 && System.currentTimeMillis() >= gustDelay + 1000){
            gusted = (LivingEntity) pAttacker;
            ModMessages.sendToServer(new GustC2SPacket());
            gustDelay = System.currentTimeMillis();
        }
        super.doPostHurt(pTarget, pAttacker, pLevel);
    }*/
}

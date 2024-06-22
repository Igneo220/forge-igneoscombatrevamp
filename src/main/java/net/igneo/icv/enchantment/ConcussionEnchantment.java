package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.ConcussC2SPacket;
import net.igneo.icv.networking.packet.JudgementC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class ConcussionEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public static long concussTime = -5000;
    public static boolean searchTarget;
    public static boolean targetFound;
    public static Vec3 lookDirection;
    public ConcussionEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null){
            pPlayer = Minecraft.getInstance().player;
            //System.out.println(EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(2)).containsKey(ModEnchantments.CONCUSSION.get()));
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(2)).containsKey(ModEnchantments.CONCUSSION.get())) {
                if (Keybindings.INSTANCE.concussion.isDown() && System.currentTimeMillis() >= concussTime + 5000) {
                    lookDirection = pPlayer.getLookAngle();
                    searchTarget = true;
                    concussTime = System.currentTimeMillis();
                    ModMessages.sendToServer(new ConcussC2SPacket());
                }
                if (searchTarget && System.currentTimeMillis() <= concussTime + 1000) {
                    if (pPlayer.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(),null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), pPlayer.getBoundingBox()) != null && pPlayer.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(),null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), pPlayer.getBoundingBox()) != pPlayer) {
                        concussTime = System.currentTimeMillis();
                        targetFound = true;
                        searchTarget = false;
                        ModMessages.sendToServer(new ConcussC2SPacket());
                    }
                }
            }
        }
    }*/

}

package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.JudgementC2SPacket;
import net.igneo.icv.networking.packet.TrainDashC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;

public class JudgementEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public static long judgeTime;
    public static boolean searchTarget;
    public static boolean targetFound;
    public static Vec3 lookDirection;
    public JudgementEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null){
            pPlayer = Minecraft.getInstance().player;
            if (ModEnchantments.checkLegEnchantments().contains("Judgement")) {
                if (Keybindings.INSTANCE.judgement.isDown() && System.currentTimeMillis() >= judgeTime + 5000) {
                    lookDirection = pPlayer.getLookAngle();
                    searchTarget = true;
                    judgeTime = System.currentTimeMillis();
                    ModMessages.sendToServer(new JudgementC2SPacket());
                }
                if (searchTarget && System.currentTimeMillis() <= judgeTime + 1000) {
                    if (pPlayer.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(),null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), pPlayer.getBoundingBox()) != null && pPlayer.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(),null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), pPlayer.getBoundingBox()) != pPlayer) {
                        judgeTime = System.currentTimeMillis();
                        targetFound = true;
                        searchTarget = false;
                        ModMessages.sendToServer(new JudgementC2SPacket());
                    }
                }
            }
        }
    }*/
}
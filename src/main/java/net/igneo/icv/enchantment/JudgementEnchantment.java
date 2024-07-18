package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.JudgementC2SPacket;
import net.igneo.icv.networking.packet.JudgementHitC2SPacket;
import net.igneo.icv.networking.packet.TrainDashC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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

    public static void onClientTick() {
        if (Minecraft.getInstance().player != null){
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(1)).containsKey(ModEnchantments.JUDGEMENT.get())) {
                if (Keybindings.INSTANCE.judgement.isDown() && System.currentTimeMillis() >= judgeTime + 5000) {
                    lookDirection = pPlayer.getLookAngle();
                    searchTarget = true;
                    judgeTime = System.currentTimeMillis();
                    pPlayer.setDeltaMovement(pPlayer.getLookAngle().scale(1.5).x,pPlayer.getLookAngle().scale(0.5).y,pPlayer.getLookAngle().scale(1.5).z);
                    ModMessages.sendToServer(new JudgementC2SPacket());
                }
                if (searchTarget && System.currentTimeMillis() <= judgeTime + 1000) {
                    for (LivingEntity entity : pPlayer.level().getEntitiesOfClass(LivingEntity.class, pPlayer.getBoundingBox())) {
                        if (entity != pPlayer) {
                            judgeTime = System.currentTimeMillis();
                            targetFound = true;
                            searchTarget = false;
                            ModMessages.sendToServer(new JudgementHitC2SPacket());
                            break;
                        }
                    }
                }
            }
        }
    }
}

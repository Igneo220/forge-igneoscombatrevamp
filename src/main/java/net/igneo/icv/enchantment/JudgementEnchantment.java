package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.JudgementC2SPacket;
import net.igneo.icv.networking.packet.JudgementHitC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class JudgementEnchantment extends Enchantment {
    //public static LocalPlayer uniPlayer = Minecraft.getInstance().uniPlayer;
    public static long judgeTime;
    public static boolean searchTarget;
    public static boolean targetFound;
    public static Vec3 lookDirection;
    public JudgementEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Keybindings.judgement.isDown() && System.currentTimeMillis() >= judgeTime + 5000) {
            EnchantmentHudOverlay.judgeFrames = 0;
            lookDirection = uniPlayer.getLookAngle();
            searchTarget = true;
            judgeTime = System.currentTimeMillis();
            uniPlayer.setDeltaMovement(uniPlayer.getLookAngle().scale(1.5).x,uniPlayer.getLookAngle().scale(0.5).y,uniPlayer.getLookAngle().scale(1.5).z);
            ModMessages.sendToServer(new JudgementC2SPacket());
        }
        if (searchTarget && System.currentTimeMillis() <= judgeTime + 1000) {
            for (LivingEntity entity : uniPlayer.level().getEntitiesOfClass(LivingEntity.class, uniPlayer.getBoundingBox())) {
                if (entity != uniPlayer) {
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

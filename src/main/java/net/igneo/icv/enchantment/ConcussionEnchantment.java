package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.ConcussC2SPacket;
import net.igneo.icv.networking.packet.ConcussHurtC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class ConcussionEnchantment extends Enchantment {
    public static long concussTime = -5000;
    private static boolean searchTarget;
    private static int targetID;
    public ConcussionEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Keybindings.concussion.isDown() && System.currentTimeMillis() >= concussTime + 5000) {
            EnchantmentHudOverlay.concussFrames = 0;
            searchTarget = true;
            concussTime = System.currentTimeMillis();
            uniPlayer.setDeltaMovement(uniPlayer.getLookAngle().scale(1.5).x,uniPlayer.getLookAngle().scale(0.5).y,uniPlayer.getLookAngle().scale(1.5).z);
            ModMessages.sendToServer(new ConcussC2SPacket());
        }
        if (searchTarget && System.currentTimeMillis() <= concussTime + 1000) {
            if (targetID == 0) {
                for (LivingEntity entity : uniPlayer.level().getEntitiesOfClass(LivingEntity.class, uniPlayer.getBoundingBox())) {
                    if (entity != uniPlayer) {
                        targetID = entity.getId();
                        break;
                    }
                }
            } else {
                concussTime = System.currentTimeMillis();
                searchTarget = false;
                ModMessages.sendToServer(new ConcussHurtC2SPacket(targetID));
            }
        } else {
            targetID = 0;
        }
    }

}

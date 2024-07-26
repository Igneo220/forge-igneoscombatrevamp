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

public class ConcussionEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public static long concussTime = -5000;
    private static boolean searchTarget;
    private static int targetID;
    public ConcussionEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Minecraft.getInstance().player != null){
            LocalPlayer pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(2)).containsKey(ModEnchantments.CONCUSSION.get())) {
                if (Keybindings.concussion.isDown() && System.currentTimeMillis() >= concussTime + 5000) {
                    //lookDirection = pPlayer.getLookAngle();
                    EnchantmentHudOverlay.concussFrames = 0;
                    searchTarget = true;
                    concussTime = System.currentTimeMillis();
                    pPlayer.setDeltaMovement(pPlayer.getLookAngle().scale(1.5).x,pPlayer.getLookAngle().scale(0.5).y,pPlayer.getLookAngle().scale(1.5).z);
                    ModMessages.sendToServer(new ConcussC2SPacket());
                }
                if (searchTarget && System.currentTimeMillis() <= concussTime + 1000) {
                    if (targetID == 0) {
                        for (LivingEntity entity : pPlayer.level().getEntitiesOfClass(LivingEntity.class, pPlayer.getBoundingBox())) {
                            if (entity != pPlayer) {
                                targetID = entity.getId();
                                break;
                            }
                        }
                    } else {
                        concussTime = System.currentTimeMillis();

                        //targetFound = true;
                        searchTarget = false;
                        ModMessages.sendToServer(new ConcussHurtC2SPacket(targetID));
                    }
                } else {
                    targetID = 0;
                }
            } else {
                concussTime = System.currentTimeMillis();
                EnchantmentHudOverlay.concussFrames = 0;
            }
        }
    }

}

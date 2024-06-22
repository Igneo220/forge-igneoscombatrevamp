package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MomentumC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MomentumEnchantment extends Enchantment {
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public static long delay;
    public static int loopCount = 0;
    public static boolean shouldCheck = true;
    public static boolean spedUp;
    public MomentumEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

/*
    public static void onClientTick() {
        pPlayer = Minecraft.getInstance().player;
        if (pPlayer != null) {
            if (ModEnchantments.checkBootEnchantments().contains("net.igneo.icv.enchantment.MomentumEnchantment")) {
                if (pPlayer.isSprinting()) {
                    if (shouldCheck) {
                        delay = System.currentTimeMillis();
                        System.out.println("increasing speed!!!");
                        ++loopCount;
                        shouldCheck = false;
                    }
                    if (System.currentTimeMillis() >= delay + 3000 && loopCount <= 3) {
                        spedUp = true;
                        ModMessages.sendToServer(new MomentumC2SPacket());
                    }
                } else if (spedUp){
                    loopCount = 0;
                    spedUp = false;
                    ModMessages.sendToServer(new MomentumC2SPacket());
                }
                if (spedUp) {
                    double d0 = pPlayer.getDeltaMovement().x;
                    double d1 = pPlayer.getDeltaMovement().y;
                    double d2 = pPlayer.getDeltaMovement().z;

                    if ((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)) <= 0.15) {
                        System.out.println((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)));
                        spedUp = false;
                        ModMessages.sendToServer(new MomentumC2SPacket());
                        loopCount = 0;
                    }
                }

            }
        }
    }*/
}

package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.FlareC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FlareEnchantment extends Enchantment {
    public static boolean charging;
    public static long chargeTime;
    public FlareEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            if (ModEnchantments.checkChestEnchantments().contains("Flare") && Keybindings.INSTANCE.flare.isDown() && !charging) {
                charging = true;
                chargeTime = System.currentTimeMillis();
            }
            if (charging) {
                Minecraft.getInstance().player.setDeltaMovement(0,0,0);
            }
            if (charging && System.currentTimeMillis() >= chargeTime + 2500) {
                ModMessages.sendToServer(new FlareC2SPacket());
                charging = false;
                chargeTime = System.currentTimeMillis();
            }
        }
    }*/
}

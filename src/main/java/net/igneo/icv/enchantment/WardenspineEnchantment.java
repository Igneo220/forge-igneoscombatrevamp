package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.WardenspineC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class WardenspineEnchantment extends Enchantment {
    public static long wardenCooldown;
    public static boolean blinding = false;
    public static boolean blind = false;
    public WardenspineEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            if (ModEnchantments.checkChestEnchantments().contains("Warden") && Keybindings.INSTANCE.wardenspine.isDown() && System.currentTimeMillis() >= wardenCooldown + 1000 && !blind && !blinding) {
                blinding = true;
                wardenCooldown = System.currentTimeMillis();
            }
            if (blinding && !blind && System.currentTimeMillis() >= wardenCooldown + 500) {
                blind = true;
                blinding = false;
                wardenCooldown = System.currentTimeMillis();
                ModMessages.sendToServer(new WardenspineC2SPacket());
            }
            if (ModEnchantments.checkChestEnchantments().contains("Warden") && Keybindings.INSTANCE.wardenspine.isDown() && System.currentTimeMillis() >= wardenCooldown + 1000 && blind && !blinding) {
                blinding = true;
                blind = true;
                wardenCooldown = System.currentTimeMillis();
            }
            if (blinding && blind && System.currentTimeMillis() >= wardenCooldown + 1000) {
                blind = false;
                blinding = false;
                wardenCooldown = System.currentTimeMillis();
                ModMessages.sendToServer(new WardenspineC2SPacket());
            }
        }
    }*/
}

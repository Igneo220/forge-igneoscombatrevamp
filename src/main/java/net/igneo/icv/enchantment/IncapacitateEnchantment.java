package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.IncaC2SPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class IncapacitateEnchantment extends Enchantment {
    public static long incaCool;
    public IncapacitateEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Keybindings.INSTANCE.incapacitate.isDown() && System.currentTimeMillis() >= incaCool + 10000 && ModEnchantments.checkLegEnchantments().contains("incapacitate")) {
            incaCool = System.currentTimeMillis();
            ModMessages.sendToServer(new IncaC2SPacket());
        }
    }*/
}

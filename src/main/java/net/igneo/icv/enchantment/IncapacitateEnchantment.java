package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.config.ICVCommonConfigs;
import net.igneo.icv.event.ModEventBusClientEvents;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.IncaC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class IncapacitateEnchantment extends Enchantment {
    public static long incaCool;
    public IncapacitateEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(1)).containsKey(ModEnchantments.INCAPACITATE.get())) {
            if (Keybindings.incapacitate.isDown() && System.currentTimeMillis() >= incaCool + 10000) {
                EnchantmentHudOverlay.incaFrames = 0;
                incaCool = System.currentTimeMillis();
                Minecraft.getInstance().player.setDeltaMovement(0, 0, 0);
                ModMessages.sendToServer(new IncaC2SPacket());
            }
        } else {
            incaCool = System.currentTimeMillis();
            EnchantmentHudOverlay.incaFrames = 0;
        }
    }
}

package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.ParryC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ParryEnchantment extends Enchantment {
    public static long parryCooldown;
    public static boolean parrying;
    public ParryEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Keybindings.parry.isDown() && System.currentTimeMillis() >= parryCooldown + 3000) {
            EnchantmentHudOverlay.parryFrames = 0;
            parryCooldown = System.currentTimeMillis();
            parrying = true;
            ModMessages.sendToServer(new ParryC2SPacket());
        }
    }
}

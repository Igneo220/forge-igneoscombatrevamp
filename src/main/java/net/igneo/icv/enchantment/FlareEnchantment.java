package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.FlareC2SPacket;
import net.igneo.icv.networking.packet.FlareParticleC2SPacket;
import net.igneo.icv.networking.packet.FlareSoundC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class FlareEnchantment extends Enchantment {
    public static boolean charging;
    public static long chargeTime;
    private static int flareDelay = 0;
    public FlareEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onClientTick() {
        if (Keybindings.flare.isDown() && !charging && System.currentTimeMillis() > chargeTime + 7000) {
            charging = true;
            chargeTime = System.currentTimeMillis();
            ModMessages.sendToServer(new FlareSoundC2SPacket());
        }
        if (charging) {
            if (System.currentTimeMillis() >= chargeTime + flareDelay) {
                ModMessages.sendToServer(new FlareParticleC2SPacket());
                flareDelay += 100;
            }
            Minecraft.getInstance().player.setDeltaMovement(0, Minecraft.getInstance().player.getDeltaMovement().y, 0);
        }
        if (charging && System.currentTimeMillis() >= chargeTime + 2500) {
            EnchantmentHudOverlay.flareFrames = 0;
            ModMessages.sendToServer(new FlareC2SPacket());
            flareDelay = 0;
            charging = false;
            chargeTime = System.currentTimeMillis();
        }
    }
}

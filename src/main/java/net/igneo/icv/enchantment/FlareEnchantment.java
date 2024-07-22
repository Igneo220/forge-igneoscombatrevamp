package net.igneo.icv.enchantment;

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
    public FlareEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            if (EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getInventory().getArmor(2)).containsKey(ModEnchantments.FLARE.get()) && Keybindings.flare.isDown() && !charging && System.currentTimeMillis() > chargeTime + 7000) {
                charging = true;
                chargeTime = System.currentTimeMillis();
                ModMessages.sendToServer(new FlareSoundC2SPacket());
            }
            if (charging) {
                ModMessages.sendToServer(new FlareParticleC2SPacket());
                Minecraft.getInstance().player.setDeltaMovement(0,Minecraft.getInstance().player.getDeltaMovement().y,0);
            }
            if (charging && System.currentTimeMillis() >= chargeTime + 2500) {
                ModMessages.sendToServer(new FlareC2SPacket());
                charging = false;
                chargeTime = System.currentTimeMillis();
            }
        }
    }
}

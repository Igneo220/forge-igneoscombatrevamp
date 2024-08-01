package net.igneo.icv.enchantment;

import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlizzardC2SPacket;
import net.igneo.icv.networking.packet.BlizzardSoundC2SPacket;
import net.igneo.icv.networking.packet.FlameC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class BlizzardEnchantment extends Enchantment {
    public static long iceTime;
    public static boolean doBeIcin;
    private static int iceDelay;
    //private final EquipmentSlot[] slots;
    public BlizzardEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
        //slots = pApplicableSlots;
    }
    public static void onClientTick() {
        if (Keybindings.blizzard.isDown() && System.currentTimeMillis() >= iceTime + 17000 && !doBeIcin) {
            iceDelay = 0;
            doBeIcin = true;
            iceTime = System.currentTimeMillis();
            ModMessages.sendToServer(new BlizzardSoundC2SPacket());
        } else if (doBeIcin && System.currentTimeMillis() <= iceTime + 3000) {
            if (System.currentTimeMillis() >= iceTime + iceDelay) {
                ModMessages.sendToServer(new BlizzardC2SPacket());
                iceDelay += 75;
            }
        } else if (doBeIcin) {
            EnchantmentHudOverlay.blizzardFrames = 0;
            iceTime = System.currentTimeMillis();
            doBeIcin = false;
        }
    }
}

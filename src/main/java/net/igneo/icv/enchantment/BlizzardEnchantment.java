package net.igneo.icv.enchantment;

import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlizzardC2SPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;

public class BlizzardEnchantment extends Enchantment {
    public static long iceTime;
    public static boolean doBeIcin;
    public static Vec3 look;
    public BlizzardEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            if (ModEnchantments.checkHelmEnchantments().contains("Blizzard")) {
                if (Keybindings.INSTANCE.blizzard.isDown() && System.currentTimeMillis() >= iceTime + 15000 && !doBeIcin) {
                    look = Minecraft.getInstance().player.getLookAngle();
                    doBeIcin = true;
                    iceTime = System.currentTimeMillis();
                } else if (doBeIcin && System.currentTimeMillis() <= iceTime + 3000) {
                    look = Minecraft.getInstance().player.getLookAngle();
                    ModMessages.sendToServer(new BlizzardC2SPacket());
                } else if (doBeIcin) {
                    iceTime = System.currentTimeMillis();
                    doBeIcin = false;
                }
            }
        }
    }*/
}

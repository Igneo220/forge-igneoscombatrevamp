package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.RendC2SPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class RendEnchantment extends Enchantment {
    public static Entity rendEntity = null;
    public static int rendCount;
    public RendEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void rendHit(Entity hitEntity) {
        if (rendEntity == null) {
            rendEntity = hitEntity;
            rendCount = 0;
        } else if (rendEntity !=  hitEntity) {
            rendEntity = hitEntity;
            rendCount = 0;
        }
        ++rendCount;
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            if (EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getMainHandItem()).containsKey(ModEnchantments.REND.get()) &&
                    Minecraft.getInstance().mouseHandler.isLeftPressed() &&
                    rendEntity != null &&
                    rendCount > 0) {
                ModMessages.sendToServer(new RendC2SPacket());
                rendEntity.hurt(Minecraft.getInstance().player.damageSources().magic(),rendCount);
            }
        }
    }*/
}
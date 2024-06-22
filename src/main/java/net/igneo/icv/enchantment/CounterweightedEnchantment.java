package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.CounterweightedC2SPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CounterweightedEnchantment extends Enchantment {
    public static boolean initialHit = false;
    public CounterweightedEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
/*
    public static void onKeyInputEvent() {
        if (Minecraft.getInstance().player != null) {
            if (EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getMainHandItem()).toString().contains("Counterweighted") && !initialHit && Minecraft.getInstance().options.keyAttack.isDown()) {
                initialHit = true;
                ModMessages.sendToServer(new CounterweightedC2SPacket());
            } else if (!Minecraft.getInstance().options.keyAttack.isDown() && initialHit) {
                initialHit = false;
                ModMessages.sendToServer(new CounterweightedC2SPacket());
            }
        }
    }*/


}

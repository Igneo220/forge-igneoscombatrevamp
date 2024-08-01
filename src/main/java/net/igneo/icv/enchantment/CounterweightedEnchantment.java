package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.CounterweightedC2SPacket;
import net.igneo.icv.networking.packet.WeightedC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class CounterweightedEnchantment extends Enchantment {
    public static boolean initialHit = false;
    public static boolean hit = false;
    public CounterweightedEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (EnchantmentHelper.getEnchantments(uniPlayer.getMainHandItem()).containsKey(ModEnchantments.COUNTERWEIGHTED.get()) && !initialHit && Minecraft.getInstance().options.keyAttack.isDown()) {
            initialHit = true;
            for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
                if (entity.getBoundingBox().intersects(uniPlayer.getEyePosition(), uniPlayer.position().add(uniPlayer.getLookAngle().scale(7))) && entity != uniPlayer && entity instanceof LivingEntity) {
                    hit = true;
                }
            }
            if (!hit) {
                ModMessages.sendToServer(new CounterweightedC2SPacket());
            }
        } else if (!hit) {
            for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
                if (entity.getBoundingBox().intersects(uniPlayer.getEyePosition(), uniPlayer.position().add(uniPlayer.getLookAngle().scale(7))) && entity != uniPlayer && entity instanceof LivingEntity) {
                    hit = true;
                }
            }
        }
    }


}

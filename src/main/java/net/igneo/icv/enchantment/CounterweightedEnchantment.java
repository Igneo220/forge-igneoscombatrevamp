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

public class CounterweightedEnchantment extends Enchantment {
    public static boolean initialHit = false;
    private static boolean hit = false;
    public CounterweightedEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    public static void onKeyInputEvent() {
        if (Minecraft.getInstance().player != null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(Minecraft.getInstance().player.getMainHandItem()).containsKey(ModEnchantments.COUNTERWEIGHTED.get()) && !initialHit && Minecraft.getInstance().options.keyAttack.isDown()) {
                initialHit = true;
                for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
                    if (entity.getBoundingBox().intersects(player.getEyePosition(), player.position().add(player.getLookAngle().scale(7))) && entity != player && entity instanceof LivingEntity) {
                        hit = true;
                    }
                }
                System.out.println(hit);
                if (!hit) {
                    ModMessages.sendToServer(new CounterweightedC2SPacket());
                }
            } else if ((!Minecraft.getInstance().options.keyAttack.isDown() && initialHit)) {
                hit = false;
                initialHit = false;
                ModMessages.sendToServer(new WeightedC2SPacket());
            } else if (Minecraft.getInstance().options.keyAttack.isDown() && !hit) {
                for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
                    if (entity.getBoundingBox().intersects(player.getEyePosition(), player.position().add(player.getLookAngle().scale(7))) && entity != player && entity instanceof LivingEntity) {
                        hit = true;
                    }
                }
                if (hit) {
                    ModMessages.sendToServer(new WeightedC2SPacket());
                }
            }
        }
    }


}

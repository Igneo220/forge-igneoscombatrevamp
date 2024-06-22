package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.AcrobaticC2SPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class AcrobaticEnchantment extends Enchantment {
    public static long flipTime;
    public static boolean flipping = false;
    //public static LocalPlayer pPlayer = Minecraft.getInstance().player;
    public AcrobaticEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        super.doPostAttack(pAttacker, pTarget, pLevel);
        if (flipping) {
            pTarget.hurt(pTarget.damageSources().playerAttack((Player) pAttacker),6);
        }
    }
/*
    public static void onClientTick() {
        if (Minecraft.getInstance().player != null) {
            pPlayer = Minecraft.getInstance().player;
            if (EnchantmentHelper.getEnchantments(pPlayer.getInventory().getArmor(1)).containsKey(ModEnchantments.ACROBATIC.get())) {
                if (Minecraft.getInstance().options.keyLeft.isDown() && Minecraft.getInstance().options.keyRight.isDown() && pPlayer.onGround() && !flipping) {
                    flipping = true;
                    flipTime = System.currentTimeMillis();
                    ModMessages.sendToServer(new AcrobaticC2SPacket());
                }
                if (flipping && pPlayer.onGround() && System.currentTimeMillis() >= flipTime + 250) {
                    flipping = false;
                }
            }
        }
    }*/
}

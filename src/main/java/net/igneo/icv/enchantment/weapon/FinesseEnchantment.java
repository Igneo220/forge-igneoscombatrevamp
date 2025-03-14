package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantment.EnchantType;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.FinesseManager;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BackpedalS2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;

public class FinesseEnchantment extends WeaponEnchantment {
    public static boolean initialHit = false;
    public static boolean hit = false;
    public FinesseEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public EnchantmentManager getManager(Player player) {
        return new FinesseManager(player);
    }
}

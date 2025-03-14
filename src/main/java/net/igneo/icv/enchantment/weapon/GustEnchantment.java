package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.weapon.GustManager;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.GustC2SPacket;
import net.igneo.icv.networking.packet.GustS2CPacket;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class GustEnchantment extends WeaponEnchantment {
    public GustEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public EnchantmentManager getManager(Player player) {
        return new GustManager(player);
    }

    @Override
    public boolean performBonusCheck() {
        return true;
    }
}

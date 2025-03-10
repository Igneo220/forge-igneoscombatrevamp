package net.igneo.icv.enchantment.weapon;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlitzNBTUpdateS2CPacket;
import net.igneo.icv.particle.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.UUID;

public class BlitzEnchantment extends Enchantment {
    public static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("3e176df4-23ac-4811-8dea-d461bb401352");
    public BlitzEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if (pTarget.level() instanceof ServerLevel) {
            pAttacker.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                ServerPlayer player = (ServerPlayer) pAttacker;
                ServerLevel level = player.serverLevel();
                if (player.fallDistance <= 0) {
                    enchVar.addBlitzBoostCount();
                } else {
                    enchVar.addBlitzBoostCount();
                    enchVar.addBlitzBoostCount();
                    enchVar.addBlitzBoostCount();
                }
                enchVar.setBlitzTime(System.currentTimeMillis());
                if (enchVar.getBlitzBoostCount() > 0) {
                    level.sendParticles(ModParticles.ATTACK_SPEED_PARTICLE.get(), player.getX(), player.getY() + 1.5, player.getZ(), 5, Math.random(), Math.random(), Math.random(), 0.5);
                    level.playSound(null, player.blockPosition(), SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.5F, (float) 0.3 + ((float) enchVar.getBlitzBoostCount() / 10));
                    player.getAttributes().getInstance(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER_UUID);
                    player.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Attack speed boost blitz", enchVar.getBlitzBoostCount()/10F, AttributeModifier.Operation.ADDITION));
                }
                ModMessages.sendToPlayer(new BlitzNBTUpdateS2CPacket(enchVar.getBlitzBoostCount(), enchVar.getBlitzTime()), player);
            });
        }
        super.doPostAttack(pAttacker, pTarget, pLevel);
    }
}

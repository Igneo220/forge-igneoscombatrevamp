package net.igneo.icv.enchantment;

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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class GustEnchantment extends Enchantment {
    private static long gustDelay = 0;
    public GustEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pTarget, Entity pAttacker, int pLevel) {
        if (pTarget.level() instanceof ServerLevel) {
            if (pTarget.fallDistance > 0 && System.currentTimeMillis() >= gustDelay + 1000) {
                if (pAttacker instanceof ServerPlayer) {
                    ModMessages.sendToPlayer(new GustS2CPacket(),(ServerPlayer) pAttacker);
                } else {
                    pAttacker.setDeltaMovement(pAttacker.getDeltaMovement().x, 0.8, pAttacker.getDeltaMovement().z);
                }
                //ModMessages.sendToServer(new GustC2SPacket());
                gustDelay = System.currentTimeMillis();
                System.out.println(pTarget.level() instanceof ServerLevel);
                if (pTarget.level() instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel) pTarget.level();
                    level.playSound(null, pTarget.blockPosition(), ModSounds.GUST.get(), SoundSource.PLAYERS);
                    level.sendParticles(ParticleTypes.EXPLOSION, pAttacker.getX(), pAttacker.getY(), pAttacker.getZ(), 5, 0, 0, 0, 1);
                }
            }
        }
        super.doPostHurt(pTarget, pAttacker, pLevel);
    }
}

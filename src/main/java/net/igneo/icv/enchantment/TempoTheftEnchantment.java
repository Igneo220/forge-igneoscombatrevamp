package net.igneo.icv.enchantment;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.MomentumC2SPacket;
import net.igneo.icv.networking.packet.TempoTheftC2SPacket;
import net.igneo.icv.networking.packet.TempoTheftS2CPacket;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.UUID;

public class TempoTheftEnchantment extends Enchantment {
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID = UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90");
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID2 = UUID.fromString("271e4444-d4ee-4fc1-824c-478eb07dac0c");
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID3 = UUID.fromString("cfe8d6a4-c198-4444-85b2-910ea4afda8b");
    public static int theftCount;
    public static long loseTheft = 0;
    public static LivingEntity thief;
    public static LivingEntity tempoVictim;
    public TempoTheftEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        if(pTarget instanceof LivingEntity) {
            if (pAttacker instanceof ServerPlayer player) {
                ServerLevel level = player.serverLevel();
                LivingEntity entity = (LivingEntity) pTarget;
                level.sendParticles(ModParticles.MOMENTUM_PARTICLE.get(), player.getX(),player.getEyeY(),player.getZ(),10,0,0,0,1);
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1), player);
                ModMessages.sendToPlayer(new TempoTheftS2CPacket(),player);
                if (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID) == null) {
                    System.out.println("it worked??");
                    level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM.get(), SoundSource.PLAYERS,1,0.5F);
                    player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID, "Tempo theft speed boost", 0.015, AttributeModifier.Operation.ADDITION));
                } else if (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID2) == null) {
                    level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM.get(), SoundSource.PLAYERS,1,1F);
                    player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID2, "Tempo theft speed boost2", 0.010, AttributeModifier.Operation.ADDITION));
                } else if (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID3) == null) {
                    level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM.get(), SoundSource.PLAYERS,1,1.5F);
                    player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID3, "Tempo theft speed boost3", 0.005, AttributeModifier.Operation.ADDITION));
                }
                System.out.println("speed 1:" + (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID) != null));
                System.out.println("speed 2:" + (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID2) != null));
                System.out.println("speed 3:" + (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID3) != null));

            }
            //ModMessages.sendToServer(new TempoTheftC2SPacket());
        }
        super.doPostAttack(pAttacker, pTarget, pLevel);
    }

    public static void onClientTick() {
        if (loseTheft != 0) {
            if (System.currentTimeMillis() >= loseTheft + 3000){
                System.out.println("attempting to remove...");
                ModMessages.sendToServer(new TempoTheftC2SPacket());
                loseTheft = 0;
            }
        }
    }
}

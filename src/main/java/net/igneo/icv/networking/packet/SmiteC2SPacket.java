package net.igneo.icv.networking.packet;

import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.particle.ModParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SmiteC2SPacket {
    private static int boltsShot;
    public SmiteC2SPacket(int bolts){
        boltsShot = bolts;
    }
    public SmiteC2SPacket(FriendlyByteBuf buf) {
        boltsShot = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(boltsShot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            if (boltsShot == 0) {
                player.setDeltaMovement(0,1,0);
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,75,99));
                level.sendParticles(ModParticles.SMITE_PARTICLE.get(),player.getX(),player.getY() + 0.5,player.getZ(),5,0,0,0,1);
                level.playSound(null,player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS,1,2);
            } else {
                level.sendParticles(ModParticles.SMITE_PARTICLE.get(),player.getX(),player.getEyeY(),player.getZ(),5,0,0,0,1);
                player.addDeltaMovement(new Vec3(player.getLookAngle().x/10,-0.2,player.getLookAngle().z/10).reverse());
                level.playSound(null,player.blockPosition(),SoundEvents.PLAYER_ATTACK_SWEEP,SoundSource.PLAYERS,1F,0.5F);
                ModEntities.BOLT.get().spawn(level,player.blockPosition(), MobSpawnType.TRIGGERED).setDeltaMovement(player.getLookAngle().scale(2));
            }

        });
        return true;
    }
}

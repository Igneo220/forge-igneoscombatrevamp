package net.igneo.icv.networking.packet;

import net.igneo.icv.particle.ModParticles;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class ConcussHurtC2SPacket {

    public ConcussHurtC2SPacket(){

    }
    public ConcussHurtC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = context.getSender().serverLevel();


            LivingEntity target = level.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(),null, player.getX(), player.getY(), player.getZ(),player.getBoundingBox().inflate(1.5));
            level.sendParticles(player, ModParticles.CONCUSS_HIT_PARTICLE.get(),true,target.getX(),target.getY() + 1.5,target.getZ(),10,Math.random(),Math.random(),Math.random(),0.5);
            level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1, 0.1F);
            target.hurt(player.damageSources().playerAttack(player),5);
            target.setDeltaMovement(new Vec3(0,0.8,0));
            target.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 30, 50), player);
            System.out.println(target);
        });
        return true;
    }
}

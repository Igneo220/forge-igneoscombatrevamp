package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class ConcussC2SPacket {

    public ConcussC2SPacket(){

    }
    public ConcussC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            LivingEntity player = context.getSender();
            ServerLevel level = context.getSender().serverLevel();

/*
            if (targetFound) {
                LivingEntity target = level.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(),null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),player.getBoundingBox());
                target.hurt(player.damageSources().playerAttack((Player) player),5);
                target.setDeltaMovement(new Vec3(0,0.8,0));
                target.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 30, 50), player);
                System.out.println(target);
                targetFound = false;
            } else {
                pPlayer.setDeltaMovement(lookDirection.scale(2.5));
            }*/
        });
        return true;
    }
}

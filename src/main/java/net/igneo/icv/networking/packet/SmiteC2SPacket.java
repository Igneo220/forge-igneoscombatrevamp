package net.igneo.icv.networking.packet;

import net.igneo.icv.entity.ModEntities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SmiteC2SPacket {
    public SmiteC2SPacket(){

    }
    public SmiteC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            LivingEntity entity = context.getSender();
            ServerLevel level = player.serverLevel();
/*
            if (boltsShot == 0) {
                System.out.println(entity);
                pPlayer.setDeltaMovement(0,1,0);
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,75,99));
            } else {
                pPlayer.setDeltaMovement(player.getLookAngle().reverse().scale(0.1));
                ModEntities.BOLT.get().spawn(level,player.blockPosition(), MobSpawnType.TRIGGERED).setDeltaMovement(player.getLookAngle());
            }
*/
        });
        return true;
    }
}

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
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CrushC2SPacket {
    public CrushC2SPacket(){

    }
    public CrushC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            Thread DetectEntity = new Thread(() -> {
                for (Entity entity : level.getAllEntities()) {
                    if (entity.distanceTo(player) <= 10 && entity != player) {
                        if (entity instanceof LivingEntity) {
                            LivingEntity newEntity = (LivingEntity) entity;
                            newEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 10, 15), player);
                        }
                    }
                }
            });
            DetectEntity.start();
            level.explode(player,player.getX(),player.getY(),player.getZ(),3.5F, Level.ExplosionInteraction.NONE);
            });
        return true;
    }
}

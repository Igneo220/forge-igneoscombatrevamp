package net.igneo.icv.networking.packet;

import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class IncaC2SPacket {
    public IncaC2SPacket(){

    }
    public IncaC2SPacket(FriendlyByteBuf buf) {

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
                        System.out.println("running!!!");
                        if (entity instanceof LivingEntity) {
                            LivingEntity newEntity = (LivingEntity) entity;
                            newEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 5), player);
                        }
                    }
                }
            });
            DetectEntity.start();
            level.playSound(null,player.blockPosition(), ModSounds.INCAPACITATE.get(), SoundSource.PLAYERS);
            //particle engine
            for (int i = -3; i < 4;) {
                double j;
                if (i > 0) {
                    j = 1;
                } else {
                    j = -1;
                }
                if (i !=0) {
                    //X
                    level.sendParticles(ModParticles.INCAPACITATE_PARTICLE.get(), player.getX() + i, player.getY(), player.getZ(), 1, 0, 0, 0, 1);
                    //Z
                    level.sendParticles(ModParticles.INCAPACITATE_PARTICLE.get(), player.getX(), player.getY(), player.getZ() + i, 1, 0, 0, 0, 1);
                    //XZ
                    level.sendParticles(ModParticles.INCAPACITATE_PARTICLE.get(), player.getX() + i + j, player.getY(), player.getZ() + i + j, 1, 0, 0, 0, 1);
                    //-XZ
                    level.sendParticles(ModParticles.INCAPACITATE_PARTICLE.get(), player.getX() - i - j, player.getY(), player.getZ() + i + j, 1, 0, 0, 0, 1);
                }
                ++i;
            }



            });
        return true;
    }
}

package net.igneo.icv.networking.packet;

import net.igneo.icv.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;


public class FlameC2SPacket {
    public FlameC2SPacket(){

    }
    public FlameC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            Vec3 look = player.getLookAngle();
            double x;
            double y;
            double z;
            double i = 0.4;
            if(Math.random() > 0.5) {
                x = i;
            } else {
                x = -i;
            }
            if(Math.random() > 0.5) {
                y = i;
            } else {
                y = -i;
            }
            if(Math.random() > 0.5) {
                z = i;
            } else {
                z = -i;
            }
            Vec3 newlook = new Vec3(look.x + (Math.random() * x), look.y,look.z + (Math.random() * z));
            ModEntities.FIRE.get().spawn(level,player.blockPosition().atY((int) player.getEyeY()), MobSpawnType.MOB_SUMMONED).setTrajectory(newlook);
            level.sendParticles(ParticleTypes.FLAME, player.getX(),player.getEyeY(),player.getZ(),2,0,0,0,0.1);
            level.playSound(null,player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS);
        });
        return true;
    }
}

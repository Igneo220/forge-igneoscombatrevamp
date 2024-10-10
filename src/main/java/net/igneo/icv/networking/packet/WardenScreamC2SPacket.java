package net.igneo.icv.networking.packet;

import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class WardenScreamC2SPacket {
    private boolean shoot;
    private final double x;
    private final double y;
    private final double z;
    public WardenScreamC2SPacket(boolean doshoot, double lx, double ly, double lz){
        shoot = doshoot;
        x = lx;
        y = ly;
        z = lz;
    }
    public WardenScreamC2SPacket(FriendlyByteBuf buf) {
        shoot = buf.readBoolean();
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(shoot);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            LivingEntity pPlayer = context.getSender();
            ServerLevel level = player.serverLevel();
            Vec3 look = new Vec3(x,y,z);

            if (shoot) {
                level.playSound(null,player.blockPosition(),SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS);
                Thread HurtEntities = new Thread(() -> {
                    for (Entity entity : level.getAllEntities()) {
                        if (entity.getBoundingBox().intersects(player.getEyePosition(),player.getEyePosition().add(look.scale(20))) && entity != pPlayer && entity instanceof LivingEntity) {
                            entity.hurt(player.damageSources().sonicBoom(player),15);
                        }
                    }
                });
                HurtEntities.start();
            } else {
                level.playSound(null,player.blockPosition(), ModSounds.WS_CHARGE.get(), SoundSource.PLAYERS);
                Vec3 vec3 = player.getEyePosition();
                Vec3 vec31 = look;//p_217704_.getEyePosition().subtract(vec3);
                Vec3 vec32 = vec31.normalize();
                for(int i = 1; i < Mth.floor(vec31.length()) + 20; ++i) {
                    Vec3 vec33 = player.getEyePosition().add(vec32.scale((double)i));
                    level.sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
            });
        return true;
    }
}

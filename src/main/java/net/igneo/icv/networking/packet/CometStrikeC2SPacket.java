package net.igneo.icv.networking.packet;

import java.util.function.Supplier;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;

public class CometStrikeC2SPacket {
    public CometStrikeC2SPacket() {
    }

    public CometStrikeC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = (NetworkEvent.Context)supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            System.out.println(player.getLookAngle().x);
            System.out.println(player.getLookAngle().z);
            int x = 0;
            int z = 0;
            double i = 0.4;
            if (player.getLookAngle().x > i) {
                x = 1;
            } else if (player.getLookAngle().x < -i) {
                x = -1;
            }
            if (player.getLookAngle().z > i) {
                z = 1;
            } else if (player.getLookAngle().z < -i) {
                z = -1;
            }
            BlockPos cometSpawn = new BlockPos(player.getBlockX() + x,player.getBlockY(),player.getBlockZ() + z);
            ModEntities.COMET.get().spawn(level, cometSpawn, MobSpawnType.COMMAND);
            level.sendParticles(ParticleTypes.END_ROD, cometSpawn.getX() + 0.5, cometSpawn.getY() + 0.5, cometSpawn.getZ() + 0.5, 15, 0, 0, 0, 0.1);
            level.playSound(null, cometSpawn, ModSounds.COMET_SPAWN.get(), SoundSource.PLAYERS, 0.5F,1);
        });
        return true;
    }
}
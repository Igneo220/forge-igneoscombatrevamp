package net.igneo.icv.networking.packet;

import net.igneo.icv.particle.ModParticles;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class DoubleJumpC2SPacket {

    public DoubleJumpC2SPacket(){

    }
    public DoubleJumpC2SPacket(FriendlyByteBuf buf) {

    }
    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = context.getSender().serverLevel();

            level.sendParticles(ParticleTypes.POOF,player.getX(),player.getY(),player.getZ(),10,Math.random(),Math.random(),Math.random(),0.5);
            level.playSound(null, player.blockPosition(), SoundEvents.SAND_BREAK, SoundSource.PLAYERS, 1, 0.1F);
            player.setDeltaMovement(player.getDeltaMovement().x,0.6,player.getDeltaMovement().z);
        });
        return true;
    }
}

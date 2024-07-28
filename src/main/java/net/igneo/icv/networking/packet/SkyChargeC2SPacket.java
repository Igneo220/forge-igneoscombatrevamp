package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class SkyChargeC2SPacket {
    private static double chargeAmount;
    public SkyChargeC2SPacket(double amount){
        chargeAmount = amount;
    }
    public SkyChargeC2SPacket(FriendlyByteBuf buf) {
        chargeAmount = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(chargeAmount);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            if (chargeAmount > 0) {
                player.setDeltaMovement(new Vec3(0, chargeAmount, 0));
                level.sendParticles(ParticleTypes.POOF, player.getX(), player.getY(), player.getZ(), 5, Math.random() / 5, Math.random() / 5, Math.random() / 5, 0.1);
                level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 2, 0.1F);
            } else {
                level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM.get(), SoundSource.PLAYERS,1F,0.1F);
                level.sendParticles(ParticleTypes.END_ROD,player.getX(),player.getY(),player.getZ(),5,0,0,0,1);
            }
        });
        return true;
    }
}

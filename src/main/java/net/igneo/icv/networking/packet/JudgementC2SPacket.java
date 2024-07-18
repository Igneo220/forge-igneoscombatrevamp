package net.igneo.icv.networking.packet;

import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class JudgementC2SPacket {
    public JudgementC2SPacket(){

    }
    public JudgementC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            LivingEntity player = context.getSender();
            ServerLevel level = context.getSender().serverLevel();

            //level.explode(player,player.getX(),player.getY(),player.getZ(),2, Level.ExplosionInteraction.NONE);
            //else {
            level.playSound(null,player.blockPosition(), ModSounds.JUDGEMENT.get(), SoundSource.PLAYERS);
            player.setDeltaMovement(player.getLookAngle().scale(1.5).x,player.getLookAngle().scale(0.5).y,player.getLookAngle().scale(1.5).z);
            //}
            //if (dashing && System.currentTimeMillis() >= hitCooldown + 500) {
            //    hitCooldown = System.currentTimeMillis();
            //    System.out.println("KILL THE " + unluckyGirlfriend);
            //    level.explode(player,player.getX(),player.getY(),player.getZ(),1, Level.ExplosionInteraction.NONE);
            //} else {
            //    level.explode(null,player.getX(),player.getY(),player.getZ(),1, Level.ExplosionInteraction.NONE);
            //}

        });
        return true;
    }
}

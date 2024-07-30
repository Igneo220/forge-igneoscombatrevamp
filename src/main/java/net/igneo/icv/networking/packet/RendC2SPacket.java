package net.igneo.icv.networking.packet;

import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class RendC2SPacket {
    int rendID;
    int rendCount;
    public RendC2SPacket(int ID,int count){
        rendID = ID;
        rendCount = count;
    }
    public RendC2SPacket(FriendlyByteBuf buf) {
        this.rendID = buf.readInt();
        this.rendCount = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(rendID);
        buf.writeInt(rendCount);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            LivingEntity entity = (LivingEntity) level.getEntity(rendID);
            entity.hurt(level.damageSources().magic(),rendCount);
            level.sendParticles(ModParticles.REND_HIT_PARTICLE.get(), entity.getX(),entity.getEyeY(),entity.getZ(),5,0,0,0,1);
            level.playSound(null, entity.blockPosition(), ModSounds.REND_HURT.get(), SoundSource.PLAYERS);
            level.sendParticles(ModParticles.REND_USE_PARTICLE.get(), player.getX(),player.getEyeY(),player.getZ(),5,0,0,0,1);
            level.playSound(null, player.blockPosition(), ModSounds.REND_USE.get(), SoundSource.PLAYERS);
        });
        return true;
    }
}

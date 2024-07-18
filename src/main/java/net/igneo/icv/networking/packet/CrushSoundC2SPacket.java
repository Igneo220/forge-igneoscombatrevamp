package net.igneo.icv.networking.packet;

import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CrushSoundC2SPacket {
    public CrushSoundC2SPacket(){

    }
    public CrushSoundC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            level.playSound(null, player.blockPosition(), ModSounds.CRUSH.get(), SoundSource.PLAYERS,1F,1);
            });
        return true;
    }
}

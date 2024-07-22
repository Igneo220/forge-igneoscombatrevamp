package net.igneo.icv.networking.packet;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public class TrainDashC2SPacket {
    private static int j;


    public TrainDashC2SPacket(int value){
        j = value;
    }
    public TrainDashC2SPacket(FriendlyByteBuf buf) {
        j = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(j);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            if (j == 1) {
                level.explode(player,player.getX(),player.getY(),player.getZ(),2, Level.ExplosionInteraction.NONE);
            } else  if (j == 0){
                level.explode(null,player.getX(),player.getY(),player.getZ(),2, Level.ExplosionInteraction.NONE);
            } else if (j == 3) {
                level.playSound(null,player.blockPosition(), ModSounds.TRAINDASH.get(), SoundSource.PLAYERS);
            }

        });
        return true;
    }
}

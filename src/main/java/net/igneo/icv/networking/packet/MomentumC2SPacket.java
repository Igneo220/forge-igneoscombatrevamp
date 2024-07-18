package net.igneo.icv.networking.packet;

import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public class MomentumC2SPacket {
    private static int loopCount;
    private static final UUID SPEED_MODIFIER_MOMENTUM_UUID = UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90");
    private static final UUID SPEED_MODIFIER_MOMENTUM_UUID2 = UUID.fromString("271e4444-d4ee-4fc1-824c-478eb07dac0c");
    private static final UUID SPEED_MODIFIER_MOMENTUM_UUID3 = UUID.fromString("cfe8d6a4-c198-4444-85b2-910ea4afda8b");

    public MomentumC2SPacket(int count){
        loopCount = count;
    }
    public MomentumC2SPacket(FriendlyByteBuf buf) {
        loopCount = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(loopCount);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            if (loopCount == 0) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_MOMENTUM_UUID);
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_MOMENTUM_UUID2);
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_MOMENTUM_UUID3);
                level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM_LOSE.get(), SoundSource.PLAYERS,1,1F);
            } else {
                level.sendParticles(ModParticles.MOMENTUM_PARTICLE.get(), player.getX(),player.getEyeY(),player.getZ(),10,0,0,0,1);
            }
            if (loopCount == 1) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_MOMENTUM_UUID, "Momentum speed boost", 0.015, AttributeModifier.Operation.ADDITION));
                level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM.get(), SoundSource.PLAYERS,1,0.5F);
            }
            if (loopCount == 2) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_MOMENTUM_UUID2, "Momentum speed boost2", 0.010, AttributeModifier.Operation.ADDITION));
                level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM.get(), SoundSource.PLAYERS,1,1F);
            }
            if (loopCount == 3) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_MOMENTUM_UUID3, "Momentum speed boost3", 0.008, AttributeModifier.Operation.ADDITION));
                level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM.get(), SoundSource.PLAYERS,1,1.5F);
            }

        });
        return true;
    }
}

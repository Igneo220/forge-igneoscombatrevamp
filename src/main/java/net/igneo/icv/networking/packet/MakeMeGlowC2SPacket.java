package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MakeMeGlowC2SPacket {
    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90");
    public MakeMeGlowC2SPacket(){

    }
    public MakeMeGlowC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            LivingEntity player = context.getSender();
            ServerLevel level = context.getSender().serverLevel();

            level.playSound(null,player.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.PLAYERS);
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING,40,1));

                //uniPlayer.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Attack speed boost blitz", 3, AttributeModifier.Operation.ADDITION));
        });
        return true;
    }
}

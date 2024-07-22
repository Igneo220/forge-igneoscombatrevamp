package net.igneo.icv.networking.packet;

import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public class TempoTheftC2SPacket {
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID = UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90");
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID2 = UUID.fromString("271e4444-d4ee-4fc1-824c-478eb07dac0c");
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID3 = UUID.fromString("cfe8d6a4-c198-4444-85b2-910ea4afda8b");
    private static int tempoID;
    private static int tempoCount;

    public TempoTheftC2SPacket(){

    }
    public TempoTheftC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
                //player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID, "Tempo theft speed boost", 0.015, AttributeModifier.Operation.ADDITION));
                //player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID2, "Tempo theft speed boost2", 0.010, AttributeModifier.Operation.ADDITION));
                //player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID3, "Tempo theft speed boost3", 0.005, AttributeModifier.Operation.ADDITION));
            System.out.println("removing!");
            //player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID2);
            //player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID3);
            level.playSound(null,player.blockPosition(), ModSounds.MOMENTUM_LOSE.get(), SoundSource.PLAYERS,1,1F);
            if (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID) != null) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID);
            }
            if (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID2) != null) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID2);
            }
            if (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID3) != null) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID3);
            }
            System.out.println("speed 1:" + (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID) != null));
            System.out.println("speed 2:" + (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID2) != null));
            System.out.println("speed 3:" + (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_TEMPO_THEFT_UUID3) != null));

        });
        return true;
    }
}

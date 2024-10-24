package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public class WardenspineC2SPacket {
    private static int blind = 0;
    private static final UUID SIGHT_MODIFIER_WARDENSPINE_UUID = UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90");
    public WardenspineC2SPacket(int value){
        blind = value;
    }
    public WardenspineC2SPacket(FriendlyByteBuf buf) {
        blind = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(blind);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            LivingEntity player = context.getSender();
            ServerLevel level = context.getSender().serverLevel();


            if (blind == 2) {
                //player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                level.playSound(null,player.blockPosition(), SoundEvents.WARDEN_ANGRY, SoundSource.PLAYERS);
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,99999999,99));
                //player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100,2));
                player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(SIGHT_MODIFIER_WARDENSPINE_UUID, "Warden sight", 6, AttributeModifier.Operation.ADDITION));
            } else if (blind == 1) {
                level.playSound(null,player.blockPosition(),SoundEvents.WARDEN_DEATH,SoundSource.PLAYERS);
                player.removeEffect(MobEffects.BLINDNESS);
                //player.removeEffect(MobEffects.MOVEMENT_SPEED);
                player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).removeModifier(UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90"));
            } else if (blind == 0) {
                player.removeEffect(MobEffects.BLINDNESS);
                //player.removeEffect(MobEffects.MOVEMENT_SPEED);
                player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).removeModifier(UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90"));
            } else if (blind == -1) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,99999999,99));
            }
        });
        return true;
    }
}

package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
    private static final UUID SPEED_MODIFIER_MOMENTUM_UUID = UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90");
    private static final UUID SPEED_MODIFIER_MOMENTUM_UUID2 = UUID.fromString("271e4444-d4ee-4fc1-824c-478eb07dac0c");
    private static final UUID SPEED_MODIFIER_MOMENTUM_UUID3 = UUID.fromString("cfe8d6a4-c198-4444-85b2-910ea4afda8b");

    public MomentumC2SPacket(){

    }
    public MomentumC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            //System.out.println("RAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            /*
            if (spedUp) {
                if (MomentumEnchantment.loopCount == 1) {
                    pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_MOMENTUM_UUID, "Momentum speed boost", 0.015, AttributeModifier.Operation.ADDITION));
                }
                if (MomentumEnchantment.loopCount == 2) {
                    pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_MOMENTUM_UUID2, "Momentum speed boost2", 0.010, AttributeModifier.Operation.ADDITION));
                }
                if (MomentumEnchantment.loopCount == 3) {
                    pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SPEED_MODIFIER_MOMENTUM_UUID3, "Momentum speed boost3", 0.005, AttributeModifier.Operation.ADDITION));
                }
            }
            if (!spedUp) {
                pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_MOMENTUM_UUID);
                pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_MOMENTUM_UUID2);
                pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_MOMENTUM_UUID3);
            }
            //System.out.println("speed 1:" + (pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_MOMENTUM_UUID) != null));
            //System.out.println("speed 2:" + (pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_MOMENTUM_UUID2) != null));
            //System.out.println("speed 3:" + (pPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_MOMENTUM_UUID3) != null));

            MomentumEnchantment.shouldCheck = true;*/
        });
        return true;
    }
}

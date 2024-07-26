package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantment.TempoTheftEnchantment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public class TempoTheftS2CPacket {
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID = UUID.fromString("9b3c6774-e4f3-4f36-b7c5-6ee971580f90");
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID2 = UUID.fromString("271e4444-d4ee-4fc1-824c-478eb07dac0c");
    private static final UUID SPEED_MODIFIER_TEMPO_THEFT_UUID3 = UUID.fromString("cfe8d6a4-c198-4444-85b2-910ea4afda8b");
    private static int tempoID;
    private static int tempoCount;

    public TempoTheftS2CPacket(){

    }
    public TempoTheftS2CPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            TempoTheftEnchantment.loseTheft = System.currentTimeMillis();

        });
        return true;
    }
}

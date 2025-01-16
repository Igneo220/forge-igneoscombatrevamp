package net.igneo.icv.networking.packet;

import net.igneo.icv.event.ModEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.event.ModEvents.clientCooldownDamageBonuses;
import static net.igneo.icv.event.ModEvents.useEnchant;


public class EnchantHitS2CPacket {
    public EnchantHitS2CPacket(){

    }
    public EnchantHitS2CPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(ModEvents::clientCooldownDamageBonuses);
        return true;
    }
}

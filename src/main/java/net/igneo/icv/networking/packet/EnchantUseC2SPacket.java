package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.event.ModEvents.useEnchant;


public class EnchantUseC2SPacket {
    private int slot;
    public EnchantUseC2SPacket(int slot){
        this.slot = slot;
    }
    public EnchantUseC2SPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerLevel level = context.getSender().serverLevel();
            ServerPlayer player = context.getSender();
            useEnchant(player, slot);
        });
        return true;
    }
}

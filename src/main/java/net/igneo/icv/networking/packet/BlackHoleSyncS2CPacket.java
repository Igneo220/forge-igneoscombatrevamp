package net.igneo.icv.networking.packet;

import net.igneo.icv.event.ModEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class BlackHoleSyncS2CPacket {
    private final int ID;
    public BlackHoleSyncS2CPacket(int id){
        ID = id;
    }
    public BlackHoleSyncS2CPacket(FriendlyByteBuf buf) {
        this.ID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ModEvents.tryBlackHoleUpdate(ID);
        });
        return true;
    }
}

package net.igneo.icv.networking.packet;

import net.igneo.icv.event.ModEvents;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class EntitySyncS2CPacket {
    private final int entityID;
    private final int slot;
    public EntitySyncS2CPacket(int entityID,int slot){
        this.entityID = entityID;
        this.slot = slot;
    }
    public EntitySyncS2CPacket(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.slot = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeInt(slot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            System.out.println("syncing");
            ICVUtils.syncClientEntity(entityID, slot);
        });
        return true;
    }
}

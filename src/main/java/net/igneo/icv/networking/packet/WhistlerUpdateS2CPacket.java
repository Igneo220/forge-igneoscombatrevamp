package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WhistlerUpdateS2CPacket {
    private final int arrowID;
    public WhistlerUpdateS2CPacket(int ID){
        this.arrowID = ID;
    }
    public WhistlerUpdateS2CPacket(FriendlyByteBuf buf) {
        this.arrowID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(arrowID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().level.getEntity(arrowID).getTags().add("whistle");
        });
        return true;
    }
}

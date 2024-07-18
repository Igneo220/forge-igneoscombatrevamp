package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PhaseUpdateS2CPacket {
    private final int ArrowID;
    public PhaseUpdateS2CPacket(int ID){
        this.ArrowID = ID;
    }
    public PhaseUpdateS2CPacket(FriendlyByteBuf buf) {
        this.ArrowID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ArrowID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().level.getEntity(ArrowID).getTags().add("EXTRACT");
        });
        return true;
    }
}

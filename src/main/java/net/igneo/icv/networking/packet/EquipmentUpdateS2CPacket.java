package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.init.ICVUtils.directUpdate;

public class EquipmentUpdateS2CPacket {
    private final int slot;
    
    public EquipmentUpdateS2CPacket(int slot) {
        this.slot = slot;
    }
    
    public EquipmentUpdateS2CPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
    }
    
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(slot);
    }
    
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            directUpdate(slot);
        });
        return true;
    }
}

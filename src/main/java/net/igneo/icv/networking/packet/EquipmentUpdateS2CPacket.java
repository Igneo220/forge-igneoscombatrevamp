package net.igneo.icv.networking.packet;

import net.igneo.icv.event.ModEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.event.ModEvents.*;

public class EquipmentUpdateS2CPacket {
    private final int slot;
    public EquipmentUpdateS2CPacket(int slot){
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

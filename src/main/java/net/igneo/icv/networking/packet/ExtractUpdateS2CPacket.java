package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExtractUpdateS2CPacket {
    private final int tridentID;
    public ExtractUpdateS2CPacket(int ID){
        this.tridentID = ID;
    }
    public ExtractUpdateS2CPacket(FriendlyByteBuf buf) {
        this.tridentID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(tridentID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().level.getEntity(tridentID).getTags().add("EXTRACT");
        });
        return true;
    }
}

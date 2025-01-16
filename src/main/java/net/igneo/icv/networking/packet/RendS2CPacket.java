package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.enchantment.ranged.RendEnchantment.rendHit;


public class RendS2CPacket {
    int rendID;
    public RendS2CPacket(int ID){
        rendID = ID;
    }
    public RendS2CPacket(FriendlyByteBuf buf) {
        this.rendID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(rendID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            rendHit(Minecraft.getInstance().level.getEntity(rendID));
        });
        return true;
    }
}

package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.enchantment.RendEnchantment.rendHit;


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

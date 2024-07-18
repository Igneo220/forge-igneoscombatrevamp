package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GustS2CPacket {
    public GustS2CPacket(){

    }
    public GustS2CPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft.getInstance().player.setDeltaMovement(Minecraft.getInstance().player.getDeltaMovement().x,0.8,Minecraft.getInstance().player.getDeltaMovement().z);
        });
        return true;
    }
}

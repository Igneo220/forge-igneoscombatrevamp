package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PushPlayerS2CPacket {

    private final double x;
    private final double y;
    private final double z;
    public PushPlayerS2CPacket(Vec3 addDirection){
        this.x = addDirection.x;
        this.y = addDirection.y;
        this.z = addDirection.z;
    }
    public PushPlayerS2CPacket(FriendlyByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Vec3 pushVec = new Vec3(x,y,z);;
            Minecraft.getInstance().player.setDeltaMovement(pushVec);
        });
        return true;
    }
}

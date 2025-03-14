package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.event.ModEvents.uniPlayer;

public class MovePlayerS2CPacket {

    private final double x;
    private final double y;
    private final double z;
    public MovePlayerS2CPacket(Vec3 setDirection){
        this.x = setDirection.x;
        this.y = setDirection.y;
        this.z = setDirection.z;
    }
    public MovePlayerS2CPacket(FriendlyByteBuf buf) {
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
            Vec3 setVec = new Vec3(x,y,z);;
            uniPlayer.setDeltaMovement(setVec);
        });
        return true;
    }
}

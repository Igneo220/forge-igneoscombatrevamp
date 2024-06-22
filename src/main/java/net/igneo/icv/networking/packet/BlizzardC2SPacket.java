package net.igneo.icv.networking.packet;

import net.igneo.icv.entity.ModEntities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class BlizzardC2SPacket {
    public BlizzardC2SPacket(){

    }
    public BlizzardC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            double x;
            double y;
            double z;
            if(Math.random() > 0.5) {
                x = 0.1;
            } else {
                x = -0.1;
            }
            if(Math.random() > 0.5) {
                y = 0.1;
            } else {
                y = -0.1;
            }
            if(Math.random() > 0.5) {
                z = 0.1;
            } else {
                z = -0.1;
            }
            //ModEntities.ICICLE.get().spawn(level,player.blockPosition().atY((int) player.getEyeY() + 2), MobSpawnType.MOB_SUMMONED).setTrajectory(new Vec3(look.x + (Math.random() * x), look.y + (Math.random() * y),look.z + (Math.random() * z)));
        });
        return true;
    }
}

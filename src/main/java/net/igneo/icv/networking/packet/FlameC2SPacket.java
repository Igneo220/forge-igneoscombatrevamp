package net.igneo.icv.networking.packet;

import net.igneo.icv.entity.ModEntities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;


public class FlameC2SPacket {
    public FlameC2SPacket(){

    }
    public FlameC2SPacket(FriendlyByteBuf buf) {

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
                x = 0.2;
            } else {
                x = -0.2;
            }
            if(Math.random() > 0.5) {
                y = 0.2;
            } else {
                y = -0.2;
            }
            if(Math.random() > 0.5) {
                z = 0.2;
            } else {
                z = -0.2;
            }
            //Vec3 newlook = new Vec3(look.x + (Math.random() * x), look.y + (Math.random() * y),look.z + (Math.random() * z));
            //ModEntities.FIRE.get().spawn(level,player.blockPosition().atY((int) player.getEyeY()), MobSpawnType.MOB_SUMMONED).setTrajectory(newlook);
        });
        return true;
    }
}

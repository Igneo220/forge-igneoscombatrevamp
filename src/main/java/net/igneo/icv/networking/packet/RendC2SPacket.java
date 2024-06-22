package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class RendC2SPacket {
    public RendC2SPacket(){

    }
    public RendC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            //LivingEntity entity = (LivingEntity) level.getEntity(rendEntity.getId());;
            //entity.hurt(level.damageSources().magic(),(int)(rendCount * (rendCount/2)));
            //rendCount = 0;
            //rendEntity = null;
        });
        return true;
    }
}

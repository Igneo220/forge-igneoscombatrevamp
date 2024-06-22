package net.igneo.icv.networking.packet;

import java.util.function.Supplier;
import net.igneo.icv.entity.ModEntities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;

public class CometStrikeC2SPacket {
    public CometStrikeC2SPacket() {
    }

    public CometStrikeC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = (NetworkEvent.Context)supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            ((EntityType)ModEntities.COMET.get()).spawn(level, player.blockPosition(), MobSpawnType.COMMAND);
        });
        return true;
    }
}
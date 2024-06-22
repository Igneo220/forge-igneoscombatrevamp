package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.enchantment.StoneCallerEnchantment.*;

public class StoneCallC2SPacket {
    public StoneCallC2SPacket(){

    }
    public StoneCallC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            level.setBlock(new BlockPos(lastX,initialY + loopCount,lastZ), Blocks.STONE.defaultBlockState(), 0);
            Minecraft.getInstance().level.setBlock(new BlockPos(lastX,initialY + loopCount,lastZ), Blocks.STONE.defaultBlockState(), 0);
        });
        return true;
    }
}

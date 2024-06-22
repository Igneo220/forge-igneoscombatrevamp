package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.igneo.icv.enchantment.StoneCallerEnchantment.*;

public class StoneBreakC2SPacket {
    public StoneBreakC2SPacket(){

    }
    public StoneBreakC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            level.setBlock(new BlockPos(lastX,initialY + 1,lastZ), Blocks.AIR.defaultBlockState(), 0);
            level.setBlock(new BlockPos(lastX,initialY + 2,lastZ), Blocks.AIR.defaultBlockState(), 0);
            level.setBlock(new BlockPos(lastX,initialY + 3,lastZ), Blocks.AIR.defaultBlockState(), 0);
            level.setBlock(new BlockPos(lastX,initialY + 4,lastZ), Blocks.AIR.defaultBlockState(), 0);
            Minecraft.getInstance().level.setBlock(new BlockPos(lastX,initialY + 1,lastZ), Blocks.AIR.defaultBlockState(), 0);
            Minecraft.getInstance().level.setBlock(new BlockPos(lastX,initialY + 2,lastZ), Blocks.AIR.defaultBlockState(), 0);
            Minecraft.getInstance().level.setBlock(new BlockPos(lastX,initialY + 3,lastZ), Blocks.AIR.defaultBlockState(), 0);
            Minecraft.getInstance().level.setBlock(new BlockPos(lastX,initialY + 4,lastZ), Blocks.AIR.defaultBlockState(), 0);
            double d0 = level.random.nextGaussian() * 0.02D;
            double d1 = level.random.nextGaussian() * 0.02D;
            double d2 = level.random.nextGaussian() * 0.02D;

            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    lastX + (double) (level.random.nextFloat()),
                    initialY + 1.5,
                    lastZ + (double) (level.random.nextFloat()),
                    10, d0, d1, d2, 0.0F);
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    lastX + (double) (level.random.nextFloat()),
                    initialY + 2.5,
                    lastZ + (double) (level.random.nextFloat()),
                    10, d0, d1, d2, 0.0F);
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    lastX + (double) (level.random.nextFloat()),
                    initialY + 3.5,
                    lastZ + (double) (level.random.nextFloat()),
                    10, d0, d1, d2, 0.0F);
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    lastX + (double) (level.random.nextFloat()),
                    initialY + 4.5,
                    lastZ + (double) (level.random.nextFloat()),
                    10, d0, d1, d2, 0.0F);


        });
        return true;
    }
}

package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class StoneCallerC2SPacket {
    private final int next;
    private final int x;
    private final int y;
    private final int z;
    public StoneCallerC2SPacket(int adding,int x1,int y1,int z1){
        this.next = adding;
        this.x = x1;
        this.y = y1;
        this.z = z1;
    }
    public StoneCallerC2SPacket(FriendlyByteBuf buf) {
        this.next = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(next);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if(enchVar.getStoneTime() == 0) {
                    enchVar.setStoneTime(System.currentTimeMillis());
                    enchVar.setStoneX(x);
                    enchVar.setStoneY(y);
                    enchVar.setStoneZ(z);
                    player.setDeltaMovement(0, 1, 0);
                }
                level.playSound(null, new BlockPos(enchVar.getStoneX(),enchVar.getStoneY() + next,enchVar.getStoneZ()), SoundType.DEEPSLATE_BRICKS.getBreakSound(), SoundSource.PLAYERS, 10F, 0.1F);
                level.setBlock(new BlockPos(enchVar.getStoneX(),enchVar.getStoneY() + next,enchVar.getStoneZ()), Blocks.STONE.defaultBlockState(), 2);
            });
        });
        return true;
    }
}

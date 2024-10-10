package net.igneo.icv.networking.packet;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.custom.BlackHoleEntity;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class BlockHoleMoveC2SPacket {
    private final int ID;
    private final double x;
    private final double y;
    private final double z;
    public BlockHoleMoveC2SPacket(int id, double nx,double ny, double nz){
        ID = id;
        x = nx;
        y = ny;
        z = nz;
    }
    public BlockHoleMoveC2SPacket(FriendlyByteBuf buf) {
        this.ID = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ID);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            Entity bh = level.getEntity(ID);
            Vec3 push = new Vec3(x,y,z);

            bh.setDeltaMovement(push.scale(0.06));
        });
        return true;
    }
}

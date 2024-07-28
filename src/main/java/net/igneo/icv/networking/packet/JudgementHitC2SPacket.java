package net.igneo.icv.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class JudgementHitC2SPacket {

    public JudgementHitC2SPacket(){

    }
    public JudgementHitC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            LivingEntity player = context.getSender();
            ServerLevel level = context.getSender().serverLevel();

            level.explode(player,player.getX(),player.getY(),player.getZ(),3, Level.ExplosionInteraction.NONE);
            //else {
            //player.setDeltaMovement(player.getLookAngle().scale(2.5));
            //}
            //if (dashing && System.currentTimeMillis() >= hitCooldown + 500) {
            //    hitCooldown = System.currentTimeMillis();
            //    level.explode(player,player.getX(),player.getY(),player.getZ(),1, Level.ExplosionInteraction.NONE);
            //} else {
            //    level.explode(null,player.getX(),player.getY(),player.getZ(),1, Level.ExplosionInteraction.NONE);
            //}

        });
        return true;
    }
}

package net.igneo.icv.networking.packet;

import net.igneo.icv.networking.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public class TrainDashC2SPacket {

    public static long hitCooldown;
    public TrainDashC2SPacket(){

    }
    public TrainDashC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
/*
            if (dashing && System.currentTimeMillis() >= hitCooldown + 500) {
                hitCooldown = System.currentTimeMillis();
                System.out.println("KILL THE " + unluckyGirlfriend);
                level.explode(player,player.getX(),player.getY(),player.getZ(),1, Level.ExplosionInteraction.NONE);
            } else {
                level.explode(null,player.getX(),player.getY(),player.getZ(),1, Level.ExplosionInteraction.NONE);
            }
*/
        });
        return true;
    }
}

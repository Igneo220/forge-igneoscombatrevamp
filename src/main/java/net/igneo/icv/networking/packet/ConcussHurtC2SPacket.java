package net.igneo.icv.networking.packet;

import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.particle.ModParticles;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class ConcussHurtC2SPacket {
    private static int targetID;

    public ConcussHurtC2SPacket(int ID){
        targetID = ID;
    }
    public ConcussHurtC2SPacket(FriendlyByteBuf buf) {
        targetID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(targetID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = context.getSender().serverLevel();


            LivingEntity target = (LivingEntity) level.getEntity(targetID);
            level.sendParticles(ModParticles.CONCUSS_HIT_PARTICLE.get(),target.getX(),target.getY() + 1.5,target.getZ(),10,Math.random(),Math.random(),Math.random(),0.5);
            level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1, 0.1F);
            target.hurt(player.damageSources().playerAttack(player),5);
            if (target instanceof ServerPlayer) {
                ServerPlayer playerTarget = (ServerPlayer) target;
                ModMessages.sendToPlayer(new PushPlayerS2CPacket(new Vec3(0,1,0)),playerTarget);
                playerTarget.setDeltaMovement(new Vec3(0, 1, 0));
                //playerTarget.setPos(playerTarget.getX(),playerTarget.getY() + 0.1 ,playerTarget.getZ());
                //playerTarget.getAttributes().getInstance(ForgeMod.ENTITY_GRAVITY.get()).addTransientModifier(new AttributeModifier(CONCUSSION_GRAVITY_MODIFIER_UUID, "Concussion gravity decrease", (double) -0.03, AttributeModifier.Operation.ADDITION));
            } else {
                target.setDeltaMovement(new Vec3(0, 1, 0));
            }
            target.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 30, 50), player);
        });
        return true;
    }
}

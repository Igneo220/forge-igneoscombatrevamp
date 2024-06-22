package net.igneo.icv.networking.packet;

import net.igneo.icv.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class WardenScreamC2SPacket {
    public WardenScreamC2SPacket(){

    }
    public WardenScreamC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            LivingEntity pPlayer = context.getSender();
            ServerLevel level = player.serverLevel();
            System.out.println("boom!!");

/*
                    Vec3 vec3 = playerpos;
                    Vec3 vec31 = WardenScreamEnchantment.look;//p_217704_.getEyePosition().subtract(vec3);
                    Vec3 vec32 = vec31.normalize();
                    for(int i = 1; i < Mth.floor(vec31.length()) + 7; ++i) {
                        Vec3 vec33 = playerpos.add(vec32.scale((double)i));
                        level.sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                //System.out.println(WardenScreamEnchantment.wardenhit);

            Thread HurtEntities = new Thread(() -> {
                for (Entity entity : level.getAllEntities()) {
                    if (entity.getBoundingBox().intersects(playerpos,playerpos.add(look.scale(7))) && entity != pPlayer && entity instanceof LivingEntity) {
                        System.out.println("killing the " + entity);
                        entity.hurt(player.damageSources().sonicBoom(player),10);
                    }
                }
            });
            HurtEntities.start();
                    //level.playSound();

 */
            });
        return true;
    }
}

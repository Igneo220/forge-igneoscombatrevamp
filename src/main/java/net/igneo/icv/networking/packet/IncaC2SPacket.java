package net.igneo.icv.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class IncaC2SPacket {
    public IncaC2SPacket(){

    }
    public IncaC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //SERVER WORK
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            Thread DetectEntity = new Thread(() -> {
                for (Entity entity : level.getAllEntities()) {
                    if (entity.distanceTo(player) <= 10 && entity != player) {
                        System.out.println("running!!!");
                        if (entity instanceof LivingEntity) {
                            LivingEntity newEntity = (LivingEntity) entity;
                            newEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 5), player);
                        }
                    }
                }
            });
            DetectEntity.start();


            //List e = player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ, (player.posX + 5),(player.posY + 5),(player.posZ + 5)));
            //level.getEntities().get(player.getBoundingBox().expandTowards((player.getX() + 5),(player.getY() + 5),(player.getZ() + 5)), (Consumer<Entity>) player);


            //System.out.println(level.getEntities().get(player.getBoundingBox().expandTowards((player.getX() + 5),(player.getY() + 5),(player.getZ() + 5)), (Consumer<Entity>) player));
                    //getBoundingBox(player.getX(), player.getY(), player.getZ(), (player.getX() + 5),(player.getY() + 5),(player.getZ() + 5)));)
        });
        return true;
    }
}

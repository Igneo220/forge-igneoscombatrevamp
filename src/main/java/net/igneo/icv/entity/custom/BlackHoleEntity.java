package net.igneo.icv.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlackHoleEntity extends Fireball {
    public Vec3 trajectory;
    private static long blackHoleTime = 0;
    public BlackHoleEntity(EntityType<? extends Fireball> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        blackHoleTime = 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void tick() {
        if (level().isClientSide) {
            if (this.getOwner() == null) {
                System.out.println(level().getNearestPlayer(this,4));
                this.setOwner(level().getNearestPlayer(this,4));
            }
        }
        if (trajectory == null && !level().isClientSide && this.getOwner() != null) {
            trajectory = this.getOwner().getLookAngle().scale(0.15);
            this.setDeltaMovement(trajectory);
        }
        if (blackHoleTime == 0) {
            trajectory = null;
            blackHoleTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() >= blackHoleTime + 14000) {
            this.discard();
        }
        if (!level().isClientSide) {
            ServerLevel level = (ServerLevel) level();
            for (Entity entity : level.getAllEntities()) {
                //System.out.println(this.getOwner());
                Vec3 pushVec = (((this.position().subtract(entity.position())).scale((10.1 - entity.distanceTo(this)) * 0.1)).scale(0.04));
                if (entity.distanceTo(this) < 1.5 && entity != this && entity != this.getOwner()) {
                    if (entity instanceof LivingEntity || entity instanceof Player) {
                        LivingEntity entity1 = (LivingEntity) entity;
                        entity1.hurt(this.damageSources().cramming(), 3);
                    }
                    entity.addDeltaMovement((this.position().subtract(entity.position())).scale(0.14));
                } else if (entity.distanceTo(this) < 10 && entity != this && entity != this.getOwner()) {
                    entity.addDeltaMovement(pushVec);
                }
            }
        } else if (Minecraft.getInstance().player != null){
            Entity entity = Minecraft.getInstance().player;
            //System.out.println((this.getOwner() == Minecraft.getInstance().player));
            Vec3 pushVec = (((this.position().subtract(entity.position())).scale((10.1 - entity.distanceTo(this)) * 0.1)).scale(0.04));
            if (entity.distanceTo(this) < 1 && entity != this && Minecraft.getInstance().player != this.getOwner()) {
                entity.addDeltaMovement((this.position().subtract(entity.position())).scale(0.14));
            } else if (entity.distanceTo(this) < 10 && Minecraft.getInstance().player != this.getOwner()) {
                entity.addDeltaMovement(pushVec);
            }
            //&& entity != this.getOwner()
        }
        super.tick();
    }
}

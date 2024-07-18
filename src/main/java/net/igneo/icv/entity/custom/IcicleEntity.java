package net.igneo.icv.entity.custom;

import net.igneo.icv.enchantment.BlizzardEnchantment;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Objects;

public class IcicleEntity extends Fireball {
    private long iceTime = 0;
    private Vec3 finaltrajectory;
    private BlockState lastState;

    public IcicleEntity(EntityType<? extends Fireball> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {

    }
    public void setTrajectory(Vec3 trajectory) {
        finaltrajectory = trajectory;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (System.currentTimeMillis() >= iceTime + 50) {
            pResult.getEntity().hurt(damageSources().magic(), 5);
            if (pResult.getEntity() instanceof LivingEntity) {
                if (this.level() instanceof ServerLevel) {
                    ServerLevel level = (ServerLevel) this.level();
                    level.playSound(null,this.blockPosition(), ModSounds.ICE_HIT.get(), SoundSource.PLAYERS,0.2F,1);
                    level.sendParticles(ModParticles.ICE_HIT_PARTICLE.get(), this.getX(), this.getY(), this.getZ(), 3, 0, 0, 0, 0.1);
                }
                LivingEntity living = (LivingEntity) pResult.getEntity();
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 100));
            }
        }
    }

    @Override
    public void tick() {
        if (iceTime == 0) {
            iceTime = System.currentTimeMillis();
        }
        //setDeltaMovement(0.1,0.1,0.1);
        if (finaltrajectory != null) {
            this.setDeltaMovement(finaltrajectory);
        }
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            //super.tick();
            //if (this.shouldBurn()) {
            //    this.setSecondsOnFire(1);
            //}

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            Vec3 moving = this.getDeltaMovement();
            if (moving.lengthSqr() != 0.0D) {
                double dd0 = moving.horizontalDistance();
                this.setYRot((float)(Mth.atan2(moving.z, moving.x) * (double)(180F / (float)Math.PI)) + 90.0F);
                this.setXRot((float)(Mth.atan2(dd0, moving.y) * (double)(180F / (float)Math.PI)) - 90.0F);

                while(this.getXRot() - this.xRotO < -180.0F) {
                    this.xRotO -= 360.0F;
                }

                while(this.getXRot() - this.xRotO >= 180.0F) {
                    this.xRotO += 360.0F;
                }

                while(this.getYRot() - this.yRotO < -180.0F) {
                    this.yRotO -= 360.0F;
                }

                while(this.getYRot() - this.yRotO >= 180.0F) {
                    this.yRotO += 360.0F;
                }

                this.setXRot(this.xRotO);
                this.setYRot(this.yRotO);
            }
            float f = this.getInertia();
            //if (this.isInWater()) {
            //    for(int i = 0; i < 4; ++i) {
            //        float f1 = 0.25F;
            //        this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
            //    }

            //    f = 0.8F;
            //}

            //this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
            //this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }


    @Override
    protected void onHit(HitResult pResult) {
        if (System.currentTimeMillis() >= iceTime + 50) {
            this.discard();
            super.onHit(pResult);
        }
    }
}

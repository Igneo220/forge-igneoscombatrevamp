package net.igneo.icv.entity.custom;

import net.igneo.icv.enchantment.BlizzardEnchantment;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
        pResult.getEntity().hurt(damageSources().magic(),6);
        if (pResult.getEntity() instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) pResult.getEntity();
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,50,100));
        }
    }

    @Override
    public void tick() {
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
        this.discard();
        super.onHit(pResult);
    }
}

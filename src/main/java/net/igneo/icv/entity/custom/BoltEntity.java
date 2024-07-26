package net.igneo.icv.entity.custom;

import net.igneo.icv.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BoltEntity extends Fireball {
    private static long boltTime;
    public BoltEntity(EntityType<? extends Fireball> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        boltTime = System.currentTimeMillis();
    }

    //@Override
    //public boolean hurt(DamageSource pSource, float pAmount) {
    //    return false;
    //}

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (System.currentTimeMillis() >= boltTime + 100 && pResult.getEntity() instanceof LivingEntity) {
            pResult.getEntity().hurt(level().damageSources().lightningBolt(), 10);
            if (!level().isClientSide) {
                ServerLevel level = (ServerLevel) level();
                EntityType.LIGHTNING_BOLT.spawn(level, new BlockPos((int) pResult.getLocation().x, (int) pResult.getLocation().y, (int) pResult.getLocation().z), MobSpawnType.MOB_SUMMONED);
            }
            this.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (System.currentTimeMillis() >= boltTime + 100) {
            if (!level().isClientSide) {
                ServerLevel level = (ServerLevel) level();
                EntityType.LIGHTNING_BOLT.spawn(level, new BlockPos((int) pResult.getLocation().x, (int) pResult.getLocation().y, (int) pResult.getLocation().z), MobSpawnType.MOB_SUMMONED);
            }
            this.discard();
        }
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            ServerLevel level = (ServerLevel) this.level();
            level.sendParticles(ModParticles.SMITE_PARTICLE.get(),this.getX(),this.getY(),this.getZ(),18,0,0,0,1);
        }
        if (System.currentTimeMillis() >= boltTime + 10000) {
            this.discard();
        }
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            //super.tick();


            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
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
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }

                f = 0.8F;
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
            //this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }
}

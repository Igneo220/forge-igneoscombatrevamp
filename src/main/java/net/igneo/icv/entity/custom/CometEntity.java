package net.igneo.icv.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.event.ForgeEventFactory;

public class CometEntity extends AbstractHurtingProjectile {
    private double explosionPower = 0.0;
    public double xPower;
    public double yPower;
    public double zPower;
    public boolean hurt = false;
    public final AnimationState idleAnimationState = new AnimationState();
    private int cometAnimationTimeout = 0;

    private void setupAnimationStates() {
        if (this.cometAnimationTimeout <= 0) {
            System.out.println("bruh");
            this.cometAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(0);
        } else {
            --this.cometAnimationTimeout;
        }

    }

    public CometEntity(EntityType<? extends AbstractHurtingProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
        this.setDeltaMovement(0.0, 0.25, 0.0);
    }
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        hurt = true;
        explosionPower += pAmount;
        System.out.println(this.explosionPower);
        this.markHurt();
        Entity entity = pSource.getEntity();
        if (entity != null) {
            if (!this.level().isClientSide) {
                Vec3 vec3 = entity.getLookAngle();
                this.xPower = vec3.x * (this.explosionPower * 0.005 + 0.1);
                this.yPower = vec3.y * (this.explosionPower * 0.005 + 0.1);
                this.zPower = vec3.z * (this.explosionPower * 0.005 + 0.1);
                this.setOwner(entity);
            }

            return true;
        } else {
            return false;
        }
    }
    @Override
    public void tick() {
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }

        if (!this.hurt && this.getDeltaMovement().y > 0.0) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.01, this.getDeltaMovement().z);
        } else if (this.hurt) {
            this.yPower -= 0.002;
        }

        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this,this::canHitEntity);
            if (hitresult.getType() != Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }

            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25, d1 - vec3.y * 0.25, d2 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
                }

                f = 0.8F;
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale((double)f));
            this.level().addParticle(ParticleTypes.END_ROD, d0, d1 + 0.5, d2, 0.0, 0.0, 0.0);
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower / 2.0F, ExplosionInteraction.NONE);
        //this.level().m_7106_(ParticleTypes.f_123813_, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower / 2.0F, ExplosionInteraction.NONE);
        //this.level().m_7106_(ParticleTypes.f_123813_, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
        this.discard();
    }
}
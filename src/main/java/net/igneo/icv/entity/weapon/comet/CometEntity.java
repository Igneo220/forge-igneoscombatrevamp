package net.igneo.icv.entity.weapon.comet;

import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CometEntity extends ICVEntity {
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation SHOOT_ANIM = RawAnimation.begin().thenLoop("shoot");
    public double hurt = 0;
    public int hurtTicks;
    public Vec3 hurtPos;
    public boolean peaked = false;
    
    public CometEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.addDeltaMovement(new Vec3(0.0, 0.23, 0.0));
        if (this.level() instanceof ServerLevel) {
        }
    }
    
    @Override
    protected <E extends GeoEntity> PlayState animController(AnimationState<E> event) {
        if (hurt > 0 && hurtTicks < 5) {
            return PlayState.STOP;
        } else {
            return event.setAndContinue(IDLE_ANIM);
        }
    }
    
    @Override
    public boolean hasFriction() {
        return false;
    }
    
    @Override
    public double getGravity() {
        double gravity = -0.0000;
        if (!peaked) {
            gravity = -0.02;
            if (this.getDeltaMovement().y < 0) {
                peaked = true;
            }
        }
        return hurt == 0 ? gravity : -0.02;
    }
    
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.level() instanceof ServerLevel) {
        }
        hurtTicks = 0;
        hurt += pAmount;
        hurtPos = this.position();
        if (this.level() instanceof ServerLevel level) {
            level.playSound(null, this.blockPosition(), ModSounds.PARRY.get(), SoundSource.PLAYERS);
        }
        if (pSource.getEntity() != null) {
            this.setDeltaMovement(pSource.getEntity().getLookAngle().normalize().scale(1));
        } else if (pSource.getSourcePosition() != null) {
            Vec3 pushVec = pSource.getSourcePosition().subtract(this.position()).normalize().reverse().scale(1);
            this.setDeltaMovement(new Vec3(
                    pushVec.x,
                    pushVec.y,
                    pushVec.z
            ));
        }
        return true;
    }
    
    @Override
    public boolean isPickable() {
        return true;
    }
    
    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        for (Entity entity : this.level().getEntities(null, this.getBoundingBox().inflate(4))) {
            if (entity != this) {
                entity.hurt(this.damageSources().explosion(this, null), (float) (hurt * 1.2F));
            }
        }
        this.discard();
    }
    
    @Override
    public void tick() {
        ProjectileUtil.rotateTowardsMovement(this, 1);
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult) || verticalCollision) {
            this.onHit(hitresult);
        }
        if (hurt > 0 && hurtTicks < 5) {
            this.setPos(hurtPos);
            if (hurtTicks == 4 && this.level() instanceof ServerLevel level) {
                level.playSound(null, this.blockPosition(), ModSounds.COMET_HIT.get(), SoundSource.PLAYERS);
            }
            ++hurtTicks;
        }
    }
}

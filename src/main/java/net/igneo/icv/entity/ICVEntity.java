package net.igneo.icv.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class ICVEntity extends Projectile implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);


    protected ICVEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "icv", 6,this::animController));
    }

    protected <E extends GeoEntity> PlayState animController(final AnimationState<E> event) {
        return  PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith();
    }

    @Override
    protected void defineSynchedData() {

    }

    public double getGroundFriction() {
        return 0.8;
    }
    public double getAirFriction() {
        return 0.001;
    }

    @Override
    public void tick() {
        super.tick();
        runPhysics();
    }

    public void runPhysics() {
        if (!this.onGround() && !this.isNoGravity()) {
            this.addDeltaMovement(new Vec3(0, -0.05, 0));
        }
        Vec3 currentVelocity = this.getDeltaMovement();
        double friction = this.onGround() ? getGroundFriction() : getAirFriction();

        friction = this.level().getBlockState(this.blockPosition()).isAir() ? friction : 0.01;
        currentVelocity = new Vec3(currentVelocity.scale(friction).x, 0, currentVelocity.scale(friction).z);
        currentVelocity = this.getDeltaMovement().subtract(currentVelocity);

        // Stop movement if velocity is very small
        if (currentVelocity.lengthSqr() < 0.0001) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            this.setDeltaMovement(currentVelocity);
        }

        this.move(MoverType.SELF, this.getDeltaMovement().scale(1.5));
    }
}
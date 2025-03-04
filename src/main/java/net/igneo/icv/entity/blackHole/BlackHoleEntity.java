package net.igneo.icv.entity.blackHole;

import com.google.common.collect.ImmutableList;
import com.lowdragmc.photon.client.fx.BlockEffect;
import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import net.igneo.icv.ICV;
import net.igneo.icv.entity.ICVEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.primitives.Doubles.max;

public class BlackHoleEntity extends ICVEntity {

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    public BlackHoleEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        FX fx = FXHelper.getFX(new ResourceLocation(ICV.MOD_ID,"blackhole"));
        EntityEffect effect = new EntityEffect(fx,this.level(),this);
        effect.setAllowMulti(true);
        effect.start();
    }

    @Override
    protected <E extends GeoEntity> PlayState animController(AnimationState<E> event) {
        return event.setAndContinue(IDLE_ANIM);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 400 || this.getOwner() == null) {
            FX fx = FXHelper.getFX(new ResourceLocation(ICV.MOD_ID,"blackholedissapate"));
            BlockEffect effect = new BlockEffect(fx,this.level(),this.blockPosition());
            effect.setAllowMulti(true);
            effect.start();
            this.discard();
        }

        pull();
    }

    private void pull() {
        for (Entity entity : this.level().getEntities(null,this.getBoundingBox().inflate(6))) {
            if (entity != this) {
                double scale = 0.05;
                Vec3 pushVec = entity.position().subtract(this.position()).reverse();
                pushVec = pushVec.normalize().scale(scale);
                entity.addDeltaMovement(pushVec);
                if (entity.distanceTo(this) < 2) {
                    entity.addDeltaMovement(pushVec);
                    entity.hurt(this.damageSources().cramming(), 3);
                }
            }
        }
    }

    @Override
    public double getAirFriction() {
        return 0.1;
    }



    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() == null) {
            Vec3 pushVec = pSource.getSourcePosition().subtract(this.position()).normalize().reverse().scale(1);
            this.addDeltaMovement(new Vec3(
                    pushVec.x,
                    0,
                    pushVec.z
            ));
        } else if (pSource.getSourcePosition() != null) {
            this.addDeltaMovement(pSource.getEntity().getLookAngle().normalize().scale(0.2));
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {

    }

}

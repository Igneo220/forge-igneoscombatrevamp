package net.igneo.icv.entity.helmet.blackHole;

import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.boots.StasisManager;
import net.igneo.icv.entity.ICVEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlackHoleEntity extends ICVEntity {
    
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    
    public BlackHoleEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
            this.discard();
        }
        
        pull();
    }
    
    private void pull() {
        for (Entity entity : this.level().getEntities(null, this.getBoundingBox().inflate(6))) {
            if (entity != this && entity != this.getOwner()) {
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
        AtomicBoolean cancel = new AtomicBoolean(false);
        if (pSource.getEntity() == null) {
            if (!(pSource.getEntity() instanceof LivingEntity)) {
                pSource.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                    if (enchVar.getManager(0) instanceof StasisManager manager) {
                        if (manager.entityData.containsKey(pSource.getEntity())) {
                            cancel.set(true);
                        }
                    }
                });
            }
            if (!cancel.get()) {
                Vec3 pushVec = pSource.getSourcePosition().subtract(this.position()).normalize().reverse().scale(1);
                this.addDeltaMovement(new Vec3(
                        pushVec.x,
                        0,
                        pushVec.z
                ));
            }
        } else if (pSource.getSourcePosition() != null) {
            this.addDeltaMovement(pSource.getEntity().getLookAngle().normalize().scale(0.2));
        }
        return super.hurt(pSource, pAmount);
    }
    
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
    
    }
    
}

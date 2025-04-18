package net.igneo.icv.entity.weapon.FireRing;

import net.igneo.icv.entity.ICVEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class FireRingEntity extends ICVEntity {
    int lifeTime = 0;
    
    public FireRingEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    public double getGravity() {
        return 0;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (lifeTime > 10) {
            this.discard();
        } else {
            ++lifeTime;
        }
    }
    
    @Override
    public boolean isPushable() {
        return true;
    }
    
    @Override
    public boolean isPickable() {
        return true;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.discard();
    }
    
    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        System.out.println("beuh");
    }
    
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (pResult.getEntity() instanceof LivingEntity entity && entity != this.getOwner()) {
            entity.setSecondsOnFire(3);
            entity.hurt(this.damageSources().onFire(), (float) 4);
        }
    }
}

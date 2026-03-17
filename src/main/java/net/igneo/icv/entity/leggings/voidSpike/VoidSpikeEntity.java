package net.igneo.icv.entity.leggings.voidSpike;

import net.igneo.icv.entity.ICVEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class VoidSpikeEntity extends ICVEntity {
    public VoidSpikeEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    private int lifetime = 0;
    public boolean launched = false;
    public Vec3 facing = new Vec3(0,1,0);
    
    @Override
    public void tick() {
        super.tick();
        if (lifetime < 400) {
            ++lifetime;
        } else {
            this.discard();
        }
        if (this.onGround() && launched) {
            this.discard();
        }
    }
    
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!pResult.getEntity().equals(this.getOwner()) && pResult.getEntity() instanceof LivingEntity entity && !this.level().isClientSide) {
            entity.hurt(this.damageSources().magic(), 6);
            System.out.println("on hit");
            discard();
        }
        super.onHitEntity(pResult);
    }
}

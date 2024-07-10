package net.igneo.icv.entity.custom;

import net.igneo.icv.entity.ModEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireEntity extends SmallFireball {
    private Vec3 finaltrajectory;
    private long despawnTimer;
    public FireEntity(EntityType<FireEntity> pEntityType, Level pLevel) {

        super(pEntityType, pLevel);
        despawnTimer = System.currentTimeMillis();
    }

    @Override
    public void setItem(ItemStack pStack) {
        super.setItem(pStack);
    }
    public void setTrajectory(Vec3 trajectory) {
        finaltrajectory = trajectory;
    }
    @Override
    public void tick() {
        if (System.currentTimeMillis() >= despawnTimer + 5000) {
            this.discard();
        }
        if (finaltrajectory != null) {
            this.setDeltaMovement(finaltrajectory);
        }
        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!this.level().isClientSide()) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            entity.hurt(this.damageSources().fireball(this, entity1), 10.0F);
        }
        super.onHitEntity(pResult);
    }
}

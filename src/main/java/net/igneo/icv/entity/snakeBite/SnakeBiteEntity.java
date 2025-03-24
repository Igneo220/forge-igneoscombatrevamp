package net.igneo.icv.entity.snakeBite;

import net.igneo.icv.enchantmentActions.enchantManagers.weapon.ViperManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SnakeBiteEntity extends ICVEntity {
    int lifeTime = 0;
    public SnakeBiteEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public double getGravity() {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (lifeTime > 5) {
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
        if (pResult.getEntity() instanceof LivingEntity entity) {
            double potionBonus = entity.getActiveEffects().isEmpty() ? 0 : 4;
            entity.addEffect(new MobEffectInstance(MobEffects.POISON,100,2));
            entity.hurt(this.damageSources().magic(), (float) (4 + potionBonus));
        }
    }

    @Override
    public void remove(RemovalReason pReason) {
        ViperManager manager = ((ViperManager) ICVUtils.getManagerForType((Player) this.getOwner(), ViperManager.class));
        if (manager != null) manager.setChild(null);
        super.remove(pReason);
    }
}

package net.igneo.icv.entity.soulSpider;

import net.igneo.icv.enchantmentActions.enchantManagers.weapon.ViperManager;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.soulOrb.SoulOrbEntity;
import net.igneo.icv.init.ICVUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SoulSpiderEntity extends ICVEntity {
    private SoulOrbEntity orb;
    boolean orbObtained = false;
    public SoulSpiderEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (orb == null) {
            if (orbObtained) this.discard();
        } else if (inRange()) {
            Vec3 pushVec = new Vec3(orb.position().x - this.position().x, 0,orb.position().z - this.position().z);
            this.addDeltaMovement(pushVec.normalize().scale(0.2));
        }
    }

    private boolean inRange() {
        if (orb == null) {
            if (orbObtained) {
                this.discard();
                return true;
            } else {
                return true;
            }
        }
        return (Math.abs(this.position().x - orb.position().x) + Math.abs(this.position().z - orb.position().z)) > 4;
    }

    @Override
    public double getGroundFriction() {
        return inRange() ? 0 : 0.8;
    }

    public void setOrb(SoulOrbEntity entity) {
        orb = entity;
        orbObtained = true;
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
    protected void onHitEntity(EntityHitResult pResult) {
        if (pResult.getEntity() instanceof LivingEntity entity) {
            entity.hurt(this.damageSources().magic(), 8F);
        }
    }
}

package net.igneo.icv.entity.boots.stonePillar;

import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class StonePillarEntity extends ICVEntity {
    public StonePillarEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    private final int maxLife = 320;
    private int lifetime = 0;
    float health = 40;
    boolean spawned = false;
    public Vec2 root = null;
    public int hurtTick = 0;
    
    @Override
    public float getStepHeight() {
        return 1.5F;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (root == null) {
            root = new Vec2((float) position().x, (float) position().z);
        } else {
            this.setPos(root.x,position().y,root.y);
        }
        this.setDeltaMovement(0,-1,0);
        if (this.health <= 0) {
            for (int i = 0; i < 5; ++i) {
                LodestoneParticles.smokeParticles(level(),position().add(0,i,0),null);
                LodestoneParticles.rockParticles(level(),position().add(0,i,0),0.2F);
            }

            level().playSound(null, this.position().x, this.position().y, this.position().z,
                    ModSounds.STONE_CALLER_BREAK.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 1f, 1.0f);
            this.discard();
        }
        if (lifetime == maxLife - 12) {
            for (int i = 0; i < 5; ++i) {
                LodestoneParticles.smokeParticles(level(),position().add(0,i,0),null);
            }
            for (Vec3 pos : ParticleShapes.renderRingList(level(),this.position(),4,0.5F)) {
                LodestoneParticles.rockParticles(level(),pos,0.2F);
            }
            level().playSound(null, this.position().x, this.position().y, this.position().z,
                    ModSounds.STONE_CALLER_DESPAWN.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 1.0f);
        }
        if (lifetime < maxLife) {
            ++lifetime;
        } else {
            this.discard();
        }
    }
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    protected static final RawAnimation SPAWN_ANIM = RawAnimation.begin().thenPlay("spawn");
    protected static final RawAnimation DESPAWN_ANIM = RawAnimation.begin().thenPlay("despawn");

    @Override
    protected <E extends GeoEntity> PlayState animController(AnimationState<E> event) {
        if (lifetime > maxLife - 12) {
            return event.setAndContinue(DESPAWN_ANIM);
        }
        if (event.getController().hasAnimationFinished()) {
            spawned = true;
        }
        if (!spawned) {
            return event.setAndContinue(SPAWN_ANIM);
        }
        return event.setAndContinue(IDLE_ANIM);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "icv", 0, this::animController));
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.tickCount > hurtTick + 2) {
            for (int i = 0; i < 5; ++i) {
                LodestoneParticles.smokeParticles(level(),position().add(0,i,0),null);
                LodestoneParticles.rockParticles(level(),position().add(0,i,0),0.2F);
            }
            level().playSound(null, this.position().x, this.position().y, this.position().z,
                    ModSounds.STONE_CALLER_HIT.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 1f, 1.0f);
            health = health - pAmount;
            hurtTick = tickCount;
            this.setPos(this.getX(), this.getY() + 0.1, this.getZ());
            return true;
        }
        return false;
    }
}

package net.igneo.icv.entity.boots.surfWave;

import net.igneo.icv.enchantmentActions.Input;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.leggings.wave.WaveEntity;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;

public class SurfWaveEntity extends ICVEntity {
    public SurfWaveEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private int lifetime = 0;
    private Vec3 storedMotion = Vec3.ZERO;
    private ArrayList<Entity> entities = new ArrayList<>();

    @Override
    public float getStepHeight() {
        return 1.5F;
    }

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    @Override
    protected <E extends GeoEntity> PlayState animController(AnimationState<E> event) {


        return event.setAndContinue(IDLE_ANIM);
    }

        @Override
        public void tick() {
            super.tick();
            if (!this.getPassengers().isEmpty()) {
                storedMotion = ICVUtils.getFlatDirection(this.getFirstPassenger().getYRot(), 0.4F, 0);
                this.getFirstPassenger().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                    storedMotion = storedMotion.add(ICVUtils.getFlatInputDirection(this.getFirstPassenger().getYRot(), enchVar.input, 0.3F, 0));
                    if (enchVar.input.equals(Input.FORWARD)) {
                        storedMotion = storedMotion.scale(1.3F);
                    }
                });
            }
            for (Entity entity : this.level().getEntities(null,this.getBoundingBox().inflate(3.5))){
                if (entity != this.getOwner() && !(entity instanceof WaveEntity) && !(entity instanceof SurfWaveEntity)) {
                    LodestoneParticles.waveParticlesBright(this.level(),entity.position(),Vec3.ZERO);
                    entity.addDeltaMovement(storedMotion.scale(1.2));
                }
                if (!entities.contains(entity)) {
                    level().playSound(null, this.position().x, this.position().y, this.position().z,
                            ModSounds.SURF_PICKUP.get(),
                            net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 1.0f);
                    entities.add(entity);
                }
            }
        setDeltaMovement(storedMotion);
        if (lifetime < 200) {
            ++lifetime;
        } else {
            this.discard();
        }
        faceFlatDirection(this.getDeltaMovement());


        float scale = 3F;
        float mscale = 3F;
        float yaw = (float)(Math.toDegrees(Math.atan2(-this.getLookAngle().x, this.getLookAngle().z)));
        Vec3 start = ICVUtils.getFlatDirection(yaw + 130,scale,0)
                .add(this.position())
                .add(this.getLookAngle().scale(mscale));
        Vec3 stop = ICVUtils.getFlatDirection(yaw - 130,scale,0)
                .add(this.position())
                .add(this.getLookAngle().scale(mscale));

        for (Vec3 pos : ParticleShapes.renderLineList(level(),this.position().add(this.getLookAngle().scale(mscale)),stop,2)) {
            Vec3 realPos = pos.add(Math.random(),0,Math.random());
            LodestoneParticles.waveParticles(level(),realPos,this.getLookAngle().scale(0.3));
        }
        for (Vec3 pos : ParticleShapes.renderLineList(level(),this.position().add(this.getLookAngle().scale(mscale)),start,2)) {
            Vec3 realPos = pos.add(Math.random(),0,Math.random());
            LodestoneParticles.waveParticles(level(),realPos,this.getLookAngle().scale(0.3));
        }
    }
    
    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return pPassenger == this.getOwner();
    }
}

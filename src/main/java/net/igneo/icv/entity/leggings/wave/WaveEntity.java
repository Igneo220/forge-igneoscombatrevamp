package net.igneo.icv.entity.leggings.wave;

import net.igneo.icv.entity.ICVEntity;
import net.igneo.icv.entity.boots.surfWave.SurfWaveEntity;
import net.igneo.icv.init.ICVUtils;
import net.igneo.icv.init.LodestoneParticles;
import net.igneo.icv.init.ParticleShapes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class WaveEntity extends ICVEntity {
    public WaveEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    private int lifetime = 0;
    private Vec3 storedMotion = Vec3.ZERO;

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    private static final EntityDataAccessor<Vector3f> TRAJECTORY =
            SynchedEntityData.defineId(WaveEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Integer> OWNER_ID =
            SynchedEntityData.defineId(WaveEntity.class, EntityDataSerializers.INT);

    public Vector3f getTrajectory() {
        return this.entityData.get(TRAJECTORY);
    }

    public void setTrajectory(Vec3 vec) {
        this.entityData.set(TRAJECTORY,vec.toVector3f());
    }

    public void setOwnerID(int id) {
        this.entityData.set(OWNER_ID,id);
    }

    @Override
    protected <E extends GeoEntity> PlayState animController(AnimationState<E> event) {


        return event.setAndContinue(IDLE_ANIM);
    }
    
    @Override
    public float getStepHeight() {
        return 1.5F;
    }
    
    @Override
    public void tick() {
        super.tick();

        if (this.entityData.get(OWNER_ID) != 0 && this.getOwner() == null) {
            this.setOwner(this.level().getEntity(this.entityData.get(OWNER_ID)));
        }

        System.out.println(this.position());
        storedMotion = new Vec3(getTrajectory());
        for (Entity entity : this.level().getEntities(null, this.getBoundingBox().inflate(3.5))) {
            if (!(entity instanceof WaveEntity) && !(entity instanceof SurfWaveEntity) && entity != this.getOwner()) {
                entity.addDeltaMovement(storedMotion);
            }
        }
        setDeltaMovement(storedMotion);
        faceFlatDirection(this.getDeltaMovement());
        if (lifetime < 10) {
            ++lifetime;
        } else {
            this.discard();
        }

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
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TRAJECTORY, Vec3.ZERO.toVector3f());
        this.entityData.define(OWNER_ID, 0);
    }


}

package net.igneo.icv.particle.custom;

import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.custom.BlackHoleEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class BlackHoleParticles extends TextureSheetParticle {
    private long orbitTime;
    private int orbitStage = 0;
    protected BlackHoleParticles(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, SpriteSet spriteSet, double pYSpeed, double pZSpeed) {


        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.friction = 0F;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *= 0.85F;
        this.lifetime = 100;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        if (System.currentTimeMillis() >= orbitTime + 10) {
            if (orbitStage == 0) {
                this.setPos(this.x + 0.3, this.y + 1, this.z);
                orbitStage = 1;
            }
            double d0 = 0.075;
            double d1 = 0.1875;
            //double d2 = 0.125;
            if (orbitStage == 1) {
                this.move(-d0, -d1, d1);
                orbitStage += 1;
            } else if (orbitStage == 2) {
                this.move(-d0, -d1, d1);
                orbitStage += 1;
            } else if (orbitStage == 3) {
                this.move(-d0, -d1, d1);
                orbitStage += 1;
            } else if (orbitStage == 4) {
                this.move(-d0, -d1, d1);
                orbitStage += 1;
            } else if (orbitStage == 5) {
                this.move(-d0, -d1, -d1);
                orbitStage += 1;
            } else if (orbitStage == 6) {
                this.move(-d0, -d1, -d1);
                orbitStage += 1;
            } else if (orbitStage == 7) {
                this.move(-d0, -d1, -d1);
                orbitStage += 1;
            } else if (orbitStage == 8) {
                this.move(-d0, -d1, -d1);
                orbitStage += 1;
            } else if (orbitStage == 9) {
                this.move(d0, d1, -d1);
                orbitStage += 1;
            } else if (orbitStage == 10) {
                this.move(d0, d1, -d1);
                orbitStage += 1;
            } else if (orbitStage == 11) {
                this.move(d0, d1, -d1);
                orbitStage += 1;
            } else if (orbitStage == 12) {
                this.move(d0, d1, -d1);
                orbitStage += 1;
            } else if (orbitStage == 13) {
                this.move(d0, d1, d1);
                orbitStage += 1;
            } else if (orbitStage == 14) {
                this.move(d0, d1, d1);
                orbitStage += 1;
            } else if (orbitStage == 15) {
                this.move(d0, d1, d1);
                orbitStage += 1;
            } else if (orbitStage == 16) {
                this.move(d0, d1, d1);
                orbitStage = 1;
            }
            orbitTime = System.currentTimeMillis();
        }
        super.tick();
        //fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new BlackHoleParticles(level, x, y, z, dx, this.sprites, dy, dz);
        }
    }
}

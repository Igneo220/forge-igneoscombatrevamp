package net.igneo.icv.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IncaParticles extends TextureSheetParticle {
    protected IncaParticles(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, SpriteSet spriteSet, double pYSpeed, double pZSpeed) {


        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);

        this.friction = 0.8F;
        this.xd = 0;
        this.yd = 0.5;
        this.zd = 0;
        this.quadSize *= 5F;
        this.lifetime = 30;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        //move(0,0.01,0);
        super.tick();
        fadeOut();
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
            return new IncaParticles(level, x, y, z, dx, this.sprites, dy, dz);
        }
    }
}

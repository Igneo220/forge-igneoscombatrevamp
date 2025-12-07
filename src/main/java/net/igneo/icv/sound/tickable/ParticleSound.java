package net.igneo.icv.sound.tickable;

import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class ParticleSound extends AbstractTickableSoundInstance {
    TextureSheetParticle particle;
    public ParticleSound(SoundEvent sound, TextureSheetParticle particle, float volume) {
        super(sound, SoundSource.PLAYERS, RandomSource.create());
        this.volume = volume;
        this.particle = particle;
        this.delay = 0;
        this.x = particle.getPos().x;
        this.y = particle.getPos().y;
        this.z = particle.getPos().z;
    }

    @Override
    public void tick() {
        this.x = particle.getPos().x;
        this.y = particle.getPos().y;
        this.z = particle.getPos().z;

        if (!this.particle.isAlive()) {
            this.stop();
        }
    }
}

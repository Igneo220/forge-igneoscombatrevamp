package net.igneo.icv.sound.tickable;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class FollowingSound extends AbstractTickableSoundInstance {
    Entity entity;
    public FollowingSound(SoundEvent sound, Entity entity) {
        super(sound, SoundSource.PLAYERS, RandomSource.create());
        this.entity = entity;
        this.delay = 0;
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }

    @Override
    public void tick() {
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }
}

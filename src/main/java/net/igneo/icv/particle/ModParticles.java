package net.igneo.icv.particle;

import net.igneo.icv.ICV;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ICV.MOD_ID);

    public static final RegistryObject<SimpleParticleType> STASIS_RUNE =
            PARTICLE_TYPES.register("stasis_rune_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ASCENSION_RUNE =
            PARTICLE_TYPES.register("ascension_rune_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> GALE_RUNE =
            PARTICLE_TYPES.register("gale_rune_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLAST_WAVE =
            PARTICLE_TYPES.register("blast_wave_particle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLAST_FIRE_BALL =
            PARTICLE_TYPES.register("blast_fire_ball_particle", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}

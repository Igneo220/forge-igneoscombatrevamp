package net.igneo.icv.particle;

import net.igneo.icv.ICV;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ICV.MOD_ID);

    public static final RegistryObject<SimpleParticleType> ATTACK_SPEED_PARTICLE =
            PARTICLE_TYPES.register("attack_speed_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SKEWERING_PARTICLE =
            PARTICLE_TYPES.register("skewering_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PHANTOM_HEAL_PARTICLE =
            PARTICLE_TYPES.register("phantom_heal_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PHANTOM_HURT_PARTICLE =
            PARTICLE_TYPES.register("phantom_hurt_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ACRO_HIT_PARTICLE =
            PARTICLE_TYPES.register("acro_hit_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventbus) {
        PARTICLE_TYPES.register(eventbus);
    }
}

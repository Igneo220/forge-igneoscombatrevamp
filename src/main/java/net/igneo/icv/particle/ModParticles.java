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
    public static final RegistryObject<SimpleParticleType> BLACK_HOLE_PARTICLE =
            PARTICLE_TYPES.register("black_hole_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> CONCUSS_HIT_PARTICLE =
            PARTICLE_TYPES.register("concuss_hit_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> CONCUSS_USE_PARTICLE =
            PARTICLE_TYPES.register("concuss_use_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ICE_HIT_PARTICLE =
            PARTICLE_TYPES.register("ice_hit_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ICE_SPAWN_PARTICLE =
            PARTICLE_TYPES.register("ice_spawn_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> KINETIC_HIT_PARTICLE =
            PARTICLE_TYPES.register("kinetic_hit_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> INCAPACITATE_PARTICLE =
            PARTICLE_TYPES.register("inca_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> REND_HIT_PARTICLE =
            PARTICLE_TYPES.register("rend_hit_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> REND_USE_PARTICLE =
            PARTICLE_TYPES.register("rend_use_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> MOMENTUM_PARTICLE =
            PARTICLE_TYPES.register("momentum_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PARRY_PARTICLE =
            PARTICLE_TYPES.register("parry_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PHASE_PARTICLE =
            PARTICLE_TYPES.register("phase_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SIPHON_PARTICLE =
            PARTICLE_TYPES.register("siphon_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SMITE_PARTICLE =
            PARTICLE_TYPES.register("smite_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventbus) {
        PARTICLE_TYPES.register(eventbus);
    }
}

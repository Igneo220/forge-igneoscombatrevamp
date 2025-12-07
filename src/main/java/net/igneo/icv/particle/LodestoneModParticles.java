package net.igneo.icv.particle;

import net.igneo.icv.ICV;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class LodestoneModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ICV.MOD_ID);
    
    private static RegistryObject<LodestoneWorldParticleType> registerParticle(String name) {
        return PARTICLE_TYPES.register(name, LodestoneWorldParticleType::new);
    }

    public static final RegistryObject<LodestoneWorldParticleType> BLINK_PARTICLE =
            PARTICLE_TYPES.register("blink_particle", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> WAVE_PARTICLE =
            PARTICLE_TYPES.register("wave_particle", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> ROCK_PARTICLE =
            PARTICLE_TYPES.register("rock_particle", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> STASIS_PARTICLE =
            PARTICLE_TYPES.register("stasis_particle", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> STASIS_DIR_PARTICLE =
            PARTICLE_TYPES.register("stasis_dir_particle", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> STASIS_BREAK_PARTICLE =
            PARTICLE_TYPES.register("stasis_break_particle", LodestoneWorldParticleType::new);
    public static final RegistryObject<LodestoneWorldParticleType> UP_PARTICLE =
            PARTICLE_TYPES.register("up_particle", LodestoneWorldParticleType::new);



    public static void register(IEventBus eventbus) {
        PARTICLE_TYPES.register(eventbus);
    }
}

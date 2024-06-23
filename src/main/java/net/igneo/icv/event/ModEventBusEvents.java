package net.igneo.icv.event;

import net.igneo.icv.ICV;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.particle.custom.AttackSpeedParticle;
import net.igneo.icv.particle.custom.SkeweringParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ATTACK_SPEED_PARTICLE.get(),
                AttackSpeedParticle.Provider::new);
        event.registerSpriteSet(ModParticles.SKEWERING_PARTICLE.get(),
                SkeweringParticle.Provider::new);
    }
}

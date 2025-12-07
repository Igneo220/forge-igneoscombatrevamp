package net.igneo.icv.event;

import net.igneo.icv.ICV;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.particle.LodestoneModParticles;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.particle.custom.AscensionRune;
import net.igneo.icv.particle.custom.StasisRune;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

@Mod.EventBusSubscriber (modid = ICV.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.helmet);
        event.register(Keybindings.chestplate);
        event.register(Keybindings.leggings);
        event.register(Keybindings.boots);
    }
    
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(LodestoneModParticles.BLINK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(LodestoneModParticles.WAVE_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(LodestoneModParticles.ROCK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(LodestoneModParticles.STASIS_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(LodestoneModParticles.STASIS_BREAK_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(LodestoneModParticles.STASIS_DIR_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);
        Minecraft.getInstance().particleEngine.register(LodestoneModParticles.UP_PARTICLE.get(), LodestoneWorldParticleType.Factory::new);

        Minecraft.getInstance().particleEngine.register(ModParticles.STASIS_RUNE.get(),
                StasisRune.Provider::new);
        Minecraft.getInstance().particleEngine.register(ModParticles.ASCENSION_RUNE.get(),
                AscensionRune.Provider::new);
    }
}

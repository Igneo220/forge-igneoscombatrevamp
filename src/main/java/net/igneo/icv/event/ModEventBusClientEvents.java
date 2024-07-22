package net.igneo.icv.event;

import com.mojang.authlib.yggdrasil.response.HasJoinedMinecraftServerResponse;
import net.igneo.icv.ICV;
import net.igneo.icv.entity.client.*;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.particle.custom.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.black_hole);
        event.register(Keybindings.blizzard);
        event.register(Keybindings.concussion);
        event.register(Keybindings.flamethrower);
        event.register(Keybindings.flare);
        event.register(Keybindings.incapacitate);
        event.register(Keybindings.judgement);
        event.register(Keybindings.parry);
        event.register(Keybindings.siphon);
        event.register(Keybindings.smite);
        event.register(Keybindings.train_dash);
        event.register(Keybindings.wardenscream);
        event.register(Keybindings.wardenspine);
    }
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ATTACK_SPEED_PARTICLE.get(),
                AttackSpeedParticle.Provider::new);
        event.registerSpriteSet(ModParticles.SKEWERING_PARTICLE.get(),
                SkeweringParticle.Provider::new);
        event.registerSpriteSet(ModParticles.PHANTOM_HEAL_PARTICLE.get(),
                PhantomHealParticle.Provider::new);
        event.registerSpriteSet(ModParticles.PHANTOM_HURT_PARTICLE.get(),
                PhantomHurtParticle.Provider::new);
        event.registerSpriteSet(ModParticles.ACRO_HIT_PARTICLE.get(),
                AcroHitParticles.Provider::new);
        event.registerSpriteSet(ModParticles.BLACK_HOLE_PARTICLE.get(),
                BlackHoleParticles.Provider::new);
        event.registerSpriteSet(ModParticles.CONCUSS_HIT_PARTICLE.get(),
                AcroHitParticles.Provider::new);
        event.registerSpriteSet(ModParticles.CONCUSS_USE_PARTICLE.get(),
                AcroHitParticles.Provider::new);
        event.registerSpriteSet(ModParticles.ICE_HIT_PARTICLE.get(),
                IceHitParticles.Provider::new);
        event.registerSpriteSet(ModParticles.ICE_SPAWN_PARTICLE.get(),
                IceSpawnParticles.Provider::new);
        event.registerSpriteSet(ModParticles.KINETIC_HIT_PARTICLE.get(),
                KineticHitParticles.Provider::new);
        event.registerSpriteSet(ModParticles.INCAPACITATE_PARTICLE.get(),
                IncaParticles.Provider::new);
        event.registerSpriteSet(ModParticles.REND_HIT_PARTICLE.get(),
                RendHitParticles.Provider::new);
        event.registerSpriteSet(ModParticles.REND_USE_PARTICLE.get(),
                RendUseParticles.Provider::new);
        event.registerSpriteSet(ModParticles.MOMENTUM_PARTICLE.get(),
                MomentumParticles.Provider::new);
        event.registerSpriteSet(ModParticles.PARRY_PARTICLE.get(),
                ParryParticles.Provider::new);
        event.registerSpriteSet(ModParticles.PHASE_PARTICLE.get(),
                PhaseParticles.Provider::new);
        event.registerSpriteSet(ModParticles.SIPHON_PARTICLE.get(),
                SiphonParticles.Provider::new);
        event.registerSpriteSet(ModParticles.SMITE_PARTICLE.get(),
                SmiteParticles.Provider::new);
    }
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.COMET_LAYER, CometModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BLACK_HOLE_LAYER, BlackHoleModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ICICLE_LAYER, IcicleModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BOLT_LAYER, BoltModel::createBodyLayer);
    }
}

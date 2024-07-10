package net.igneo.icv.event;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.AcrobaticEnchantment;
import net.igneo.icv.entity.client.BlackHoleModel;
import net.igneo.icv.entity.client.CometModel;
import net.igneo.icv.entity.client.IcicleModel;
import net.igneo.icv.entity.client.ModModelLayers;
import net.igneo.icv.entity.custom.IcicleEntity;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.particle.custom.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
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
    }
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.COMET_LAYER, CometModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BLACK_HOLE_LAYER, BlackHoleModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ICICLE_LAYER, IcicleModel::createBodyLayer);
    }
}

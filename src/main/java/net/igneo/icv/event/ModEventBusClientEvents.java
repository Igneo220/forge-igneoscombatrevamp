package net.igneo.icv.event;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.AcrobaticEnchantment;
import net.igneo.icv.entity.client.CometModel;
import net.igneo.icv.entity.client.ModModelLayers;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.particle.custom.AttackSpeedParticle;
import net.igneo.icv.particle.custom.PhantomHealParticle;
import net.igneo.icv.particle.custom.PhantomHurtParticle;
import net.igneo.icv.particle.custom.SkeweringParticle;
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
                PhantomHurtParticle.Provider::new);
    }
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.COMET_LAYER, CometModel::createBodyLayer);
    }
}

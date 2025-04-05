package net.igneo.icv;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.config.ICVClientConfigs;
import net.igneo.icv.config.ICVCommonConfigs;
import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.chestplate.abyssStone.AbyssStoneRenderer;
import net.igneo.icv.entity.helmet.blackHole.BlackHoleRenderer;
import net.igneo.icv.entity.weapon.boostCharge.BoostChargeRenderer;
import net.igneo.icv.entity.weapon.FireRing.FireRingEntity;
import net.igneo.icv.entity.weapon.FireRing.FireRingRenderer;
import net.igneo.icv.entity.weapon.comet.CometRenderer;
import net.igneo.icv.entity.helmet.divineLightningRod.DivineLightningRodRenderer;
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpike.IceSpikeRenderer;
import net.igneo.icv.entity.helmet.glacialImpasse.iceSpikeSpawner.IceSpikeSpawnerRenderer;
import net.igneo.icv.entity.chestplate.meteorSummoner.MeteorSummonerRenderer;
import net.igneo.icv.entity.weapon.ember.EmberRenderer;
import net.igneo.icv.entity.weapon.snakeBite.SnakeBiteRenderer;
import net.igneo.icv.entity.boots.soulEmber.SoulEmberRenderer;
import net.igneo.icv.entity.chestplate.soulOrb.SoulOrbRenderer;
import net.igneo.icv.entity.chestplate.soulSpider.SoulSpiderRenderer;
import net.igneo.icv.entity.boots.stonePillar.StonePillarRenderer;
import net.igneo.icv.entity.boots.surfWave.SurfWaveRenderer;
import net.igneo.icv.entity.leggings.voidSpike.VoidSpikeRenderer;
import net.igneo.icv.entity.leggings.wave.WaveRenderer;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ICV.MOD_ID)
public class ICV
{
    public static final String MOD_ID = "icv";
    public static final Logger LOGGER = LogManager.getLogger("icv");

    public ICV() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ICVClientConfigs.SPEC,"icv-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ICVCommonConfigs.SPEC,"icv-common.toml");

        ModEnchantments.register(modEventBus);

        ModParticles.register(modEventBus);

        ModSounds.register(modEventBus);

        ModEntities.register(modEventBus);
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.COMET.get(), CometRenderer::new);
            EntityRenderers.register(ModEntities.EMBER.get(), EmberRenderer::new);
            EntityRenderers.register(ModEntities.FIRE_RING.get(), FireRingRenderer::new);
            EntityRenderers.register(ModEntities.BLACK_HOLE.get(), BlackHoleRenderer::new);
            EntityRenderers.register(ModEntities.SNAKE_BITE.get(), SnakeBiteRenderer::new);
            EntityRenderers.register(ModEntities.SOUL_ORB.get(), SoulOrbRenderer::new);
            EntityRenderers.register(ModEntities.SOUL_SPIDER.get(), SoulSpiderRenderer::new);
            EntityRenderers.register(ModEntities.SURF_WAVE.get(), SurfWaveRenderer::new);
            EntityRenderers.register(ModEntities.STONE_PILLAR.get(), StonePillarRenderer::new);
            EntityRenderers.register(ModEntities.SOUL_EMBER.get(), SoulEmberRenderer::new);
            EntityRenderers.register(ModEntities.ABYSS_STONE.get(), AbyssStoneRenderer::new);
            EntityRenderers.register(ModEntities.WAVE.get(), WaveRenderer::new);
            EntityRenderers.register(ModEntities.VOID_SPIKE.get(), VoidSpikeRenderer::new);
            EntityRenderers.register(ModEntities.METEOR_SUMMONER.get(), MeteorSummonerRenderer::new);
            EntityRenderers.register(ModEntities.ICE_SPIKE.get(), IceSpikeRenderer::new);
            EntityRenderers.register(ModEntities.ICE_SPIKE_SPAWNER.get(), IceSpikeSpawnerRenderer::new);
            EntityRenderers.register(ModEntities.DIVINE_LIGHTNING_ROD.get(), DivineLightningRodRenderer::new);
            EntityRenderers.register(ModEntities.BOOST_CHARGE.get(), BoostChargeRenderer::new);

            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                    new ResourceLocation(MOD_ID, "enchant_animator"),
                    42,
                    ICV::registerPlayerAnimation);
        }
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("enchantments", EnchantmentHudOverlay.HUD_ENCHANTMENTS);
        }
    }
    public static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        //This will be invoked for every new player
        return new ModifierLayer<>();
    }
}

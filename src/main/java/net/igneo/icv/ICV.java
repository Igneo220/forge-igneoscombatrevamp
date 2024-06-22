package net.igneo.icv;

import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.client.BlackHoleRenderer;
import net.igneo.icv.entity.client.BoltRenderer;
import net.igneo.icv.entity.client.CometRenderer;
import net.igneo.icv.entity.client.IcicleRenderer;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.particle.ModParticles;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        //ModEnchantments.register(modEventBus);
        ModParticles.register(modEventBus);
        System.out.println(LOGGER);
        if (FMLEnvironment.dist.isClient()) {
            ModEntities.register(modEventBus);
        }

    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.COMET.get(), CometRenderer::new);
            //EntityRenderers.register((EntityType)ModEntities.ICICLE.get(), IcicleRenderer::new);
            //EntityRenderers.register((EntityType)ModEntities.BOLT.get(), BoltRenderer::new);
            //EntityRenderers.register((EntityType)ModEntities.FIRE.get(), ThrownItemRenderer::new);
            //EntityRenderers.register((EntityType)ModEntities.BLACK_HOLE.get(), BlackHoleRenderer::new);
        }
    }
}

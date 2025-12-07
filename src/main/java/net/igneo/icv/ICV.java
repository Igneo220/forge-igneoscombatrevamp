package net.igneo.icv;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.config.ICVClientConfigs;
import net.igneo.icv.config.ICVCommonConfigs;
import net.igneo.icv.enchantment.ModEnchantments;
import net.igneo.icv.entity.ModEntities;
import net.igneo.icv.entity.ModEntityRenderers;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.particle.LodestoneModParticles;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.shader.ModShaders;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.client.player.AbstractClientPlayer;
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

@Mod (ICV.MOD_ID)
public class ICV {
    public static final String MOD_ID = "icv";
    public static final Logger LOGGER = LogManager.getLogger("icv");
    
    public ICV() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ICVClientConfigs.SPEC, "icv-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ICVCommonConfigs.SPEC, "icv-common.toml");
        
        ModEnchantments.register(modEventBus);
        LodestoneModParticles.register(modEventBus);
        ModParticles.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        ModMessages.register();
    }
    
    @Mod.EventBusSubscriber (modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModShaders.register();
            ModEntityRenderers.register();
            
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

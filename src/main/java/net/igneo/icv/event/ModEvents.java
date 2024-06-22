package net.igneo.icv.event;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlitzNBTUpdateS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static net.igneo.icv.enchantment.BlitzEnchantment.ATTACK_SPEED_MODIFIER_UUID;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).isPresent()) {
                event.addCapability(new ResourceLocation(ICV.MOD_ID, "properties"), new PlayerEnchantmentActionsProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerEnchantmentActions.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        event.player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {

            //Blitz check
            if (enchVar.getBlitzBoostCount() > 0) {
                if (System.currentTimeMillis() >= enchVar.getBlitzTime() + 1000) {
                    if (FMLEnvironment.dist.isClient()) {
                        enchVar.resetBoostCount();
                        enchVar.setBlitzTime(System.currentTimeMillis());
                    } else {
                        ServerPlayer player = (ServerPlayer) event.player;
                        ServerLevel level = player.serverLevel();
                        enchVar.resetBoostCount();
                        enchVar.setBlitzTime(System.currentTimeMillis());
                        player.getAttributes().getInstance(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER_UUID);
                        level.playSound(null,player.blockPosition(), SoundEvents.CREEPER_DEATH, SoundSource.PLAYERS,4F, 0.2F);
                        ModMessages.sendToPlayer(new BlitzNBTUpdateS2CPacket(enchVar.getBlitzBoostCount(),enchVar.getBlitzTime()),player);
                    }
                }
            }

            //Phantom pain check
            if (enchVar.getPhantomVictim() != null) {
                if (System.currentTimeMillis() >= enchVar.getPhantomDelay() + 4000) {
                    enchVar.getPhantomVictim().heal(enchVar.getPhantomHurt());
                    enchVar.resetPhantomHurt();
                    enchVar.deletePhantomVictim();
                    System.out.println("healing previous entity");
                }
            }
        });
    }
}

package net.igneo.icv.event;

import com.alrex.parcool.common.action.impl.Dodge;
import com.alrex.parcool.common.capability.Parkourability;
import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.*;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.StasisManager;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.igneo.icv.init.ICVUtils.*;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void equipEvent(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            ModMessages.sendToPlayer(new EquipmentUpdateS2CPacket(event.getSlot().getFilterFlag()), (ServerPlayer) player);
            updateManager(player,event.getSlot().getFilterFlag());
        }
    }

    @SubscribeEvent
    public static void onItemUseEvent(PlayerInteractEvent.RightClickItem event) {
        event.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (event.getEntity().isCrouching()) {
                if (enchVar.getManager(5) != null) {
                    useEnchant(event.getEntity(), 5);
                } else {
                    useEnchant(event.getEntity(), 4);
                }
            } else {
                useEnchant(event.getEntity(), 4);
            }
        });
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.Key event){
        if (Keybindings.boots.isDown()) {
            useEnchant(Minecraft.getInstance().player, 0);
            ModMessages.sendToServer(new EnchantUseC2SPacket(0));
        }
        if (Keybindings.leggings.isDown()) {
            useEnchant(Minecraft.getInstance().player,1);
            ModMessages.sendToServer(new EnchantUseC2SPacket(1));
        }
        if (Keybindings.chestplate.isDown()) {
            useEnchant(Minecraft.getInstance().player,2);
            ModMessages.sendToServer(new EnchantUseC2SPacket(2));
        }
        if (Keybindings.helmet.isDown()) {
            useEnchant(Minecraft.getInstance().player,3);
            ModMessages.sendToServer(new EnchantUseC2SPacket(3));
        }
    }

    @SubscribeEvent
    public static void blockBreakEvent(BlockEvent.BreakEvent event) {
        if (EnchantmentHelper.getEnchantments(event.getPlayer().getMainHandItem()).containsKey(ModEnchantments.BRUTE_TOUCH.get())) {
            event.getLevel().setBlock(event.getPos(),Blocks.AIR.defaultBlockState(),2);
            event.getPlayer().getMainHandItem().setDamageValue(event.getPlayer().getMainHandItem().getDamageValue() + 1);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void hurtEvent(AttackEntityEvent event) {
        if (!(event.getTarget() instanceof LivingEntity)) {
            event.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (enchVar.getManager(0) instanceof StasisManager manager) {
                    if (manager.entityData.containsKey(event.getTarget())) {
                        manager.addMovement(event.getTarget(),event.getEntity().getLookAngle().normalize().scale(0.2));
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void livingHurtEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            sendCooldownDamageBonuses(player);
            ModMessages.sendToPlayer(new EnchantHitS2CPacket(), (ServerPlayer) player);
        }
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (Parkourability.get(player).get(Dodge.class).isDoing()) {
                    ServerLevel level = player.serverLevel();
                    event.setCanceled(true);
                }
            });
        }
    }
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
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(oldStore -> {
            event.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);

                //Save NBT Data for enchantments if death-persistent logic is necessary to squish bugs

            });
        });
        event.getOriginal().invalidateCaps();
    }
    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        event.getEntity().reviveCaps();
        event.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {

            //Same deal as above comment, but this time when changing dimensions

        });
    }
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerEnchantmentActions.class);
    }
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        event.player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            for (EnchantmentManager manager : enchVar.getManagers()) {
                if (manager != null) {
                    manager.tick();
                }
            }
        });
    }
}

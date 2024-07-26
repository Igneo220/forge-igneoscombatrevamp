package net.igneo.icv.event;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.*;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.BlitzNBTUpdateS2CPacket;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

import static java.lang.Math.abs;
import static net.igneo.icv.enchantment.BlitzEnchantment.ATTACK_SPEED_MODIFIER_UUID;
import static net.igneo.icv.enchantment.SiphonEnchantment.consumeClick;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID)
public class ModEvents {
    public static int enchShift = 0;
    public static int enchLength = 0;
    @SubscribeEvent
    public static void blockBreakEvent(BlockEvent.BreakEvent event) {
        if (EnchantmentHelper.getEnchantments(event.getPlayer().getMainHandItem()).containsKey(ModEnchantments.BRUTE_TOUCH.get())) {
            event.getLevel().setBlock(event.getPos(),Blocks.AIR.defaultBlockState(),2);
            event.getPlayer().getMainHandItem().setDamageValue(event.getPlayer().getMainHandItem().getDamageValue() + 1);
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void livingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                if (EnchantmentHelper.getEnchantments(player.getInventory().getArmor(2)).containsKey(ModEnchantments.PARRY.get())) {
                    if (System.currentTimeMillis() <= enchVar.getParryTime() + 250) {
                        ServerLevel level = player.serverLevel();
                        level.playSound(null,player.blockPosition(),ModSounds.PARRY.get(), SoundSource.PLAYERS,30,0.9F);
                        level.sendParticles(ModParticles.PARRY_PARTICLE.get(), player.getX(),player.getEyeY(),player.getZ(),10,0,0,0,1);
                        event.setCanceled(true);
                    }
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
    public static void onKeyInputEvent(InputEvent.Key event){
        if (Minecraft.getInstance().player != null) {
            if (wearingArmor(Minecraft.getInstance().player)) {
                if (Keybindings.siphon.isDown()) {
                    SiphonEnchantment.onKeyInputEvent();
                } else {
                    consumeClick = false;
                }

            }
        }
    }

    private static boolean wearingArmor(LocalPlayer player) {
        boolean armordetected = false;
        for (ItemStack armor : player.getInventory().armor) {
            if (!armor.getItem().toString().contains("air")) {
                armordetected = true;
            }
        }
        return armordetected;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        event.player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (enchVar.getAcrobatBonus() && (event.player.onGround() || event.player.isInFluidType() || event.player.isPassenger())) {
                enchVar.setAcrobatBonus(false);
            }

            //Blitz check
            if (enchVar.getBlitzBoostCount() > 0) {
                if (System.currentTimeMillis() >= enchVar.getBlitzTime() + 1000) {
                    if (!(event.player instanceof ServerPlayer)) {
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
                if (enchVar.getPhantomVictim().isAlive()) {
                    if (System.currentTimeMillis() >= enchVar.getPhantomDelay() + 4000) {
                        ServerPlayer player = (ServerPlayer) event.player;
                        ServerLevel level = player.serverLevel();
                        level.sendParticles(ModParticles.PHANTOM_HEAL_PARTICLE.get(), enchVar.getPhantomVictim().getX(), enchVar.getPhantomVictim().getY() + 1.5, enchVar.getPhantomVictim().getZ(), 10, Math.random(), Math.random(), Math.random(), 0.5);
                        level.playSound(null, enchVar.getPhantomVictim().blockPosition(), ModSounds.PHANTOM_HEAL.get(), SoundSource.PLAYERS, 0.25F, (float) 0.3 + (float) abs(Math.random() + 0.5));
                        enchVar.getPhantomVictim().heal(enchVar.getPhantomHurt());
                        enchVar.resetPhantomHurt();
                        enchVar.deletePhantomVictim();
                    }
                }
            }

            //stone caller check
            if (enchVar.getStoneTime() != 0) {
                if (System.currentTimeMillis() >= enchVar.getStoneTime() + 6000) {
                    if (event.player instanceof ServerPlayer) {
                        ServerPlayer player = (ServerPlayer) event.player;
                        ServerLevel level = player.serverLevel();
                        level.playSound(null, new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + 1, enchVar.getStoneZ()), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.PLAYERS, 2F, 5.0F);
                        level.setBlock(new BlockPos(enchVar.getStoneX(), enchVar.getStoneY(), enchVar.getStoneZ()), Blocks.AIR.defaultBlockState(), 2);
                        level.setBlock(new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + 1, enchVar.getStoneZ()), Blocks.AIR.defaultBlockState(), 2);
                        level.setBlock(new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + 2, enchVar.getStoneZ()), Blocks.AIR.defaultBlockState(), 2);
                        level.setBlock(new BlockPos(enchVar.getStoneX(), enchVar.getStoneY() + 3, enchVar.getStoneZ()), Blocks.AIR.defaultBlockState(), 2);

                        double d0 = level.random.nextGaussian() * 0.02D;
                        double d1 = level.random.nextGaussian() * 0.02D;
                        double d2 = level.random.nextGaussian() * 0.02D;

                        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                enchVar.getStoneX() + (double) (level.random.nextFloat()),
                                enchVar.getStoneY() + 0.5,
                                enchVar.getStoneZ() + (double) (level.random.nextFloat()),
                                10, d0, d1, d2, 0.0F);
                        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                enchVar.getStoneX() + (double) (level.random.nextFloat()),
                                enchVar.getStoneY() + 1.5,
                                enchVar.getStoneZ() + (double) (level.random.nextFloat()),
                                10, d0, d1, d2, 0.0F);
                        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                enchVar.getStoneX() + (double) (level.random.nextFloat()),
                                enchVar.getStoneY() + 2.5,
                                enchVar.getStoneZ() + (double) (level.random.nextFloat()),
                                10, d0, d1, d2, 0.0F);
                        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                                enchVar.getStoneX() + (double) (level.random.nextFloat()),
                                enchVar.getStoneY() + 3.5,
                                enchVar.getStoneZ() + (double) (level.random.nextFloat()),
                                10, d0, d1, d2, 0.0F);
                    }
                    enchVar.setStoneTime(0);
                }
            }

            if (FMLEnvironment.dist.isClient()) {
                StoneCallerEnchantment.onClientTick();
                BlizzardEnchantment.onClientTick();
                SkyChargeEnchantment.onClientTick();
                CrushEnchantment.onClientTick();
                AcrobaticEnchantment.onClientTick();
                DoubleJumpEnchantment.onClientTick();
                FlamethrowerEnchantment.onClientTick();
                FlareEnchantment.onClientTick();
                MomentumEnchantment.onClientTick();
                TempoTheftEnchantment.onClientTick();
                TrainDashEnchantment.onClientTick();
                WardenspineEnchantment.onClientTick();

                if (Minecraft.getInstance().player != null) {
                    CometStrikeEnchantment.onKeyInputEvent();
                    BlackHoleEnchantment.onKeyInputEvent();
                    ConcussionEnchantment.onKeyInputEvent();
                    KineticEnchantment.onKeyInputEvent();
                    CounterweightedEnchantment.onKeyInputEvent();
                    IncapacitateEnchantment.onKeyInputEvent();
                    JudgementEnchantment.onKeyInputEvent();
                    RendEnchantment.onKeyInputEvent();
                    ParryEnchantment.onKeyInputEvent();
                    SmiteEnchantment.onKeyInputEvent();
                    WardenScreamEnchantment.onKeyInputEvent();
                }
            }
        });
    }
}

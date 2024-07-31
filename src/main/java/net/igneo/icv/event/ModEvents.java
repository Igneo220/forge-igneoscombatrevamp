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
import net.minecraft.nbt.*;
import net.minecraft.network.chat.contents.NbtContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ForgeMod;
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

import java.util.UUID;

import static java.lang.Math.abs;
import static net.igneo.icv.enchantment.BlitzEnchantment.ATTACK_SPEED_MODIFIER_UUID;
import static net.igneo.icv.enchantment.SiphonEnchantment.consumeClick;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID)
public class ModEvents {
    public static final UUID WAYFINDER_SPEED_MODIFIER_UUID = UUID.fromString("8a23719c-852d-47fc-bb41-8527955288d4");
    public static final UUID WILD_HEALTH_MODIFIER_UUID = UUID.fromString("c4e2d23f-4051-4d7d-a8d2-fd0e01a667e7");
    public static final UUID SILENCE_DAMAGE_MODIFIER_UUID = UUID.fromString("e01fe99d-7575-4820-8b0d-7b3b89ec2452");
    public static final UUID SILENCE_SPEED_MODIFIER_UUID = UUID.fromString("e01fe99d-7575-4820-8b0d-7b3b89ec2452");
    public static final UUID SNOUT_ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("b937f1d6-2575-4e54-a86a-8f69f48bfd52");
    public static final UUID HOST_ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("ba269bfe-1c9b-409e-a12d-0186eea83413");
    public static final UUID DUNE_ATTACK_MODIFIER_UUID = UUID.fromString("472a0f42-5303-460e-943c-fb1ad6e48a69");
    public static final UUID SHAPER_TOUGH_MODIFIER_UUID = UUID.fromString("472a0f42-5303-460e-943c-fb1ad6e48a69");
    public static BlockPos usedEnchTable;
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
                        level.sendParticles(ModParticles.PARRY_PARTICLE.get(), player.getX(),player.getEyeY(),player.getZ(),5,0,0,0,1);
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
            int wayTrim = 0;
            int wildTrim = 0;
            int silenceTrim = 0;
            int snoutTrim = 0;
            int hostTrim = 0;
            int duneTrim = 0;
            int shaperTrim = 0;
            for (int j = 0; j < 4; ++j) {
                if (!event.player.getInventory().getArmor(j).toString().contains("air")) {
                    if (event.player.getInventory().getArmor(j).getTag().getAllKeys().contains("Trim")) {
                        Tag tag = event.player.getInventory().getArmor(j).getTag().get("Trim");
                        if (tag.toString().contains("wayfinder")) {
                            ++wayTrim;
                        }
                        if (tag.toString().contains("wild")) {
                            ++wildTrim;
                        }
                        if (tag.toString().contains("silence")) {
                            ++silenceTrim;
                        }
                        if (tag.toString().contains("snout")) {
                            ++snoutTrim;
                        }
                        if (tag.toString().contains("host")) {
                            ++hostTrim;
                        }
                        if (tag.toString().contains("dune")) {
                            ++duneTrim;
                        }
                        if (tag.toString().contains("shaper")) {
                            ++shaperTrim;
                        }
                    }
                }
            }
            if (wayTrim > 0 || wayTrim != enchVar.getWayBuff()) {
                if (event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(WAYFINDER_SPEED_MODIFIER_UUID) != null) {
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(WAYFINDER_SPEED_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(WAYFINDER_SPEED_MODIFIER_UUID, "Wayfinder speed boost", (double) wayTrim /150, AttributeModifier.Operation.ADDITION));
                    enchVar.setWayBuff(wayTrim);
                } else {
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(WAYFINDER_SPEED_MODIFIER_UUID, "Wayfinder speed boost", (double) wayTrim /150, AttributeModifier.Operation.ADDITION));
                    enchVar.setWayBuff(wayTrim);
                }
            }
            if (shaperTrim > 0 || shaperTrim != enchVar.getShaperBuff()) {
                if (event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SHAPER_TOUGH_MODIFIER_UUID) != null) {
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SHAPER_TOUGH_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SHAPER_TOUGH_MODIFIER_UUID, "Shaper toughness boost", (double) shaperTrim*2, AttributeModifier.Operation.ADDITION));
                    enchVar.setShaperBuff(shaperTrim);
                } else {
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(WAYFINDER_SPEED_MODIFIER_UUID, "Shaper toughness boost", (double) shaperTrim*2, AttributeModifier.Operation.ADDITION));
                    enchVar.setShaperBuff(shaperTrim);
                }
            }
            if (wayTrim > 0 || wayTrim != enchVar.getWayBuff()) {
                if (event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(WAYFINDER_SPEED_MODIFIER_UUID) != null) {
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(WAYFINDER_SPEED_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(WAYFINDER_SPEED_MODIFIER_UUID, "Wayfinder speed boost", (double) wayTrim /150, AttributeModifier.Operation.ADDITION));
                    enchVar.setWayBuff(wayTrim);
                } else {
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(WAYFINDER_SPEED_MODIFIER_UUID, "Wayfinder speed boost", (double) wayTrim /150, AttributeModifier.Operation.ADDITION));
                    enchVar.setWayBuff(wayTrim);
                }
            }
            if (wildTrim > 0 || wildTrim != enchVar.getWildBuff()) {
                if (event.player.getAttributes().getInstance(Attributes.MAX_HEALTH).getModifier(WILD_HEALTH_MODIFIER_UUID) != null) {
                    event.player.getAttributes().getInstance(Attributes.MAX_HEALTH).removeModifier(WILD_HEALTH_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(WILD_HEALTH_MODIFIER_UUID, "Wild health boost", (double) wildTrim*2, AttributeModifier.Operation.ADDITION));
                    enchVar.setWildBuff(wildTrim);
                } else {
                    event.player.getAttributes().getInstance(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(WILD_HEALTH_MODIFIER_UUID, "Wild health boost", (double) wildTrim*2, AttributeModifier.Operation.ADDITION));
                    enchVar.setWildBuff(wildTrim);
                }
            }
            if (snoutTrim > 0 || snoutTrim != enchVar.getSnoutBuff()) {
                if (event.player.getAttributes().getInstance(Attributes.ATTACK_SPEED).getModifier(SNOUT_ATTACK_SPEED_MODIFIER_UUID) != null) {
                    event.player.getAttributes().getInstance(Attributes.ATTACK_SPEED).removeModifier(SNOUT_ATTACK_SPEED_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(SNOUT_ATTACK_SPEED_MODIFIER_UUID, "Snout attack speed boost", (double) snoutTrim, AttributeModifier.Operation.ADDITION));
                    enchVar.setSnoutBuff(snoutTrim);
                } else {
                    event.player.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(SNOUT_ATTACK_SPEED_MODIFIER_UUID, "Snout attack speed boost", (double) snoutTrim, AttributeModifier.Operation.ADDITION));
                    enchVar.setSnoutBuff(snoutTrim);
                }
            }
            if (hostTrim > 0 || hostTrim != enchVar.getHostBuff()) {
                if (event.player.getAttributes().getInstance(Attributes.ATTACK_SPEED).getModifier(HOST_ATTACK_SPEED_MODIFIER_UUID) != null) {
                    event.player.getAttributes().getInstance(Attributes.ATTACK_SPEED).removeModifier(HOST_ATTACK_SPEED_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(HOST_ATTACK_SPEED_MODIFIER_UUID, "Host attack speed debuff", (double) -hostTrim/5, AttributeModifier.Operation.ADDITION));
                    enchVar.setHostBuff(hostTrim);
                } else {
                    event.player.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(HOST_ATTACK_SPEED_MODIFIER_UUID, "Host attack speed debuff", (double) -hostTrim/5, AttributeModifier.Operation.ADDITION));
                    enchVar.setHostBuff(hostTrim);
                }
            }
            if (duneTrim > 0 || duneTrim != enchVar.getDuneBuff()) {
                if (event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).getModifier(DUNE_ATTACK_MODIFIER_UUID) != null) {
                    event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).removeModifier(DUNE_ATTACK_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(DUNE_ATTACK_MODIFIER_UUID, "Dune attack debuff", (double) -duneTrim/1.5, AttributeModifier.Operation.ADDITION));
                    enchVar.setDuneBuff(duneTrim);
                } else {
                    event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(DUNE_ATTACK_MODIFIER_UUID, "Dune attack debuff", (double) -duneTrim/1.5, AttributeModifier.Operation.ADDITION));
                    enchVar.setDuneBuff(duneTrim);
                }
            }
            if (silenceTrim > 0 || silenceTrim != enchVar.getSilenceBuff()) {
                if (event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).getModifier(SILENCE_DAMAGE_MODIFIER_UUID) != null) {
                    event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).removeModifier(SILENCE_DAMAGE_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SILENCE_SPEED_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(SILENCE_DAMAGE_MODIFIER_UUID, "silence damage boost", (double) silenceTrim, AttributeModifier.Operation.ADDITION));
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SILENCE_SPEED_MODIFIER_UUID, "silence speed debuff", (double) -silenceTrim/120, AttributeModifier.Operation.ADDITION));
                    enchVar.setSilenceBuff(silenceTrim);
                } else {
                    event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).removeModifier(SILENCE_DAMAGE_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SILENCE_SPEED_MODIFIER_UUID);
                    event.player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(SILENCE_DAMAGE_MODIFIER_UUID, "silence health boost", (double) silenceTrim, AttributeModifier.Operation.ADDITION));
                    event.player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(SILENCE_SPEED_MODIFIER_UUID, "silence speed debuff", (double) -silenceTrim/120, AttributeModifier.Operation.ADDITION));
                    enchVar.setSilenceBuff(silenceTrim);
                }
            }


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
                        level.sendParticles(ModParticles.PHANTOM_HEAL_PARTICLE.get(), enchVar.getPhantomVictim().getX(), enchVar.getPhantomVictim().getY() + 1.5, enchVar.getPhantomVictim().getZ(), 5, Math.random(), Math.random(), Math.random(), 0.5);
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

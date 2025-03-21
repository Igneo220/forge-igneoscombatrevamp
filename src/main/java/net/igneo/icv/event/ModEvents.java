package net.igneo.icv.event;

import com.alrex.parcool.common.action.impl.Dodge;
import com.alrex.parcool.common.capability.Parkourability;
import net.igneo.icv.ICV;
import net.igneo.icv.client.EnchantmentHudOverlay;
import net.igneo.icv.config.ICVCommonConfigs;
import net.igneo.icv.enchantment.*;
import net.igneo.icv.enchantment.armor.*;
import net.igneo.icv.enchantment.ranged.RendEnchantment;
import net.igneo.icv.enchantment.ICVEnchantment;
import net.igneo.icv.enchantment.weapon.KineticEnchantment;
import net.igneo.icv.enchantment.weapon.TempoTheftEnchantment;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActions;
import net.igneo.icv.enchantmentActions.PlayerEnchantmentActionsProvider;
import net.igneo.icv.enchantmentActions.enchantManagers.EnchantmentManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.ArmorEnchantManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.BlackHoleManager;
import net.igneo.icv.enchantmentActions.enchantManagers.armor.StasisManager;
import net.igneo.icv.entity.blackHole.BlackHoleEntity;
import net.igneo.icv.init.Keybindings;
import net.igneo.icv.networking.ModMessages;
import net.igneo.icv.networking.packet.*;
import net.igneo.icv.particle.ModParticles;
import net.igneo.icv.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;
import static net.igneo.icv.enchantment.weapon.FinesseEnchantment.hit;
import static net.igneo.icv.enchantment.armor.MomentumEnchantment.loopCount;
import static net.igneo.icv.enchantment.armor.MomentumEnchantment.spedUp;
import static net.igneo.icv.enchantment.armor.SiphonEnchantment.consumeClick;

@Mod.EventBusSubscriber(modid = ICV.MOD_ID)
public class ModEvents {
    public static boolean boosted = false;
    @OnlyIn(Dist.CLIENT)
    public static LocalPlayer uniPlayer;
    public static boolean refreshLegs = false;
    public static boolean refreshChest = false;
    public static boolean refreshHelmet = false;
    public static final UUID WAYFINDER_SPEED_MODIFIER_UUID = UUID.fromString("8a23719c-852d-47fc-bb41-8527955288d4");
    public static final UUID WAYFINDER_DAMAGE_MODIFIER_UUID = UUID.fromString("fce1270c-fecb-4543-964e-2b47e5161b00");
    public static final UUID WILD_HEALTH_MODIFIER_UUID = UUID.fromString("c4e2d23f-4051-4d7d-a8d2-fd0e01a667e7");
    public static final UUID SILENCE_DAMAGE_MODIFIER_UUID = UUID.fromString("2599b386-78e9-4bb0-adb2-6fb93ade2d09");
    public static final UUID SILENCE_ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("e01fe99d-7575-4820-8b0d-7b3b89ec2452");
    public static final UUID SNOUT_ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("b937f1d6-2575-4e54-a86a-8f69f48bfd52");
    public static final UUID SNOUT_HEALTH_MODIFIER_UUID = UUID.fromString("d342d129-7ebb-44c2-b6d3-72cee0342a21");
    public static final UUID HOST_ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("ba269bfe-1c9b-409e-a12d-0186eea83413");
    public static final UUID DUNE_GRAVITY_MODIFIER_UUID = UUID.fromString("ccef707f-414b-4c01-bbad-788791883af0");
    public static final UUID SHAPER_TOUGH_MODIFIER_UUID = UUID.fromString("da6f0a6f-c28a-4f59-af83-3191e643a828");
    public static final UUID CONCUSSION_GRAVITY_MODIFIER_UUID = UUID.fromString("472a0f42-5303-460e-943c-fb1ad6e48a69");
    public static BlockPos usedEnchTable;
    public static int enchShift = 0;
    public static int enchLength = 0;

    //----------------------------------------

    @SubscribeEvent
    public static void equipEvent(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            ModMessages.sendToPlayer(new EquipmentUpdateS2CPacket(event.getSlot().getFilterFlag()), (ServerPlayer) player);
            updateManager(player,event.getSlot().getFilterFlag());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void directUpdate(int pSlot) {
        if (uniPlayer != null) {
            updateManager(uniPlayer, pSlot);
        }
    }

    public static void updateManager(Player player, int pSlot) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            int slot = pSlot;
            if (pSlot == 0) {
                slot = 4;
            } else if (pSlot <= 4) {
                --slot;
            }
            List<Enchantment> enchList = new ArrayList<>();
            switch (slot) {
                case 0,1,2,3 -> enchList = player.getInventory().getArmor(slot).getAllEnchantments().keySet().stream().toList();
                case 4 -> enchList = player.getMainHandItem().getAllEnchantments().keySet().stream().toList();
                case 5 -> enchList = player.getOffhandItem().getAllEnchantments().keySet().stream().toList();
            }
            if (!enchList.isEmpty()) {
                for (Enchantment enchantment : enchList) {
                    if (enchantment instanceof ICVEnchantment enchant) {
                        enchVar.setManager(enchant.getManager(player), slot);
                    }
                }
            } else {
                enchVar.setManager(null, slot);
            }
        });
    }

    public static void useEnchant(Player player, int slot) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (enchVar.getManager(slot) != null) {
                enchVar.getManager(slot).use();
            }
        });
    }

    @SubscribeEvent
    public static void onItemUseEvent(PlayerInteractEvent.RightClickItem event) {
        event.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (event.getEntity().isCrouching()) {
                if (enchVar.getManager(5) != null) {
                    useEnchant(event.getEntity(), 5);
                    //ModMessages.sendToServer(new EnchantUseC2SPacket(5));
                } else {
                    useEnchant(event.getEntity(), 4);
                    //ModMessages.sendToServer(new EnchantUseC2SPacket(4));
                }
            } else {
                useEnchant(event.getEntity(), 4);
                //ModMessages.sendToServer(new EnchantUseC2SPacket(4));
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void tryBlackHoleUpdate(int ID) {
        Minecraft.getInstance().player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            if (enchVar.getManager(3) instanceof BlackHoleManager manager) {
                if (Minecraft.getInstance().level.getEntity(ID) instanceof BlackHoleEntity blackHole) {
                    manager.child = blackHole;
                    blackHole.setOwner(Minecraft.getInstance().player);
                }
            }
        });
    }





    //-------------------------------------------------------------


    @SubscribeEvent
    public static void renderTooltips(ItemTooltipEvent event) {
        if(ICVCommonConfigs.TRIM_EFFECTS.get() && event.getToolTip().toString().contains("trim")) {
            String trim = "null";
            int index = 0;
            if (event.getItemStack().isDamageableItem()) {
                index = findArmorTip(event.getToolTip());
            } else {
                index = findTrimTip(event.getToolTip());
            }
            if (event.getItemStack().serializeNBT().toString().contains("coast")) {
                trim = "coast";
            } else if (event.getItemStack().serializeNBT().toString().contains("dune")) {
                trim = "dune";
            } else if (event.getItemStack().serializeNBT().toString().contains("eye")) {
                trim = "eye";
            } else if (event.getItemStack().serializeNBT().toString().contains("host")) {
                trim = "host";
            } else if (event.getItemStack().serializeNBT().toString().contains("raiser")) {
                trim = "raiser";
            } else if (event.getItemStack().serializeNBT().toString().contains("rib")) {
                trim = "rib";
            } else if (event.getItemStack().serializeNBT().toString().contains("sentry")) {
                trim = "sentry";
            } else if (event.getItemStack().serializeNBT().toString().contains("shaper")) {
                trim = "shaper";
            } else if (event.getItemStack().serializeNBT().toString().contains("silence")) {
                trim = "silence";
            } else if (event.getItemStack().serializeNBT().toString().contains("snout")) {
                trim = "snout";
            } else if (event.getItemStack().serializeNBT().toString().contains("spire")) {
                trim = "spire";
            } else if (event.getItemStack().serializeNBT().toString().contains("tide")) {
                trim = "tide";
            } else if (event.getItemStack().serializeNBT().toString().contains("vex")) {
                trim = "vex";
            } else if (event.getItemStack().serializeNBT().toString().contains("wayfinder")) {
                trim = "wayfinder";
            } else if (event.getItemStack().serializeNBT().toString().contains("wild")) {
                trim = "wild";
            } else if (event.getItemStack().serializeNBT().toString().contains("ward")) {
                trim = "ward";
            }
            if (!trim.contains("null") && index != 0) {
                event.getToolTip().add(index, Component.translatable("icv.effects").withStyle(ChatFormatting.GRAY));
                event.getToolTip().add(index + 1, Component.translatable("icv." + trim + ".desc").withStyle(ChatFormatting.YELLOW));
            }
        }
    }

    private static int findTrimTip(List<Component> toolTip) {
        int f = 0;
        for (Component item : toolTip) {
            int j = 0;
            if (item.toString().contains("trim")) {
                f = j + 2;
                break;
            } else {
                ++j;
            }
        }
        return f;
    }
    private static int findArmorTip(List<Component> toolTip) {
        int f = 0;
        for (Component item : toolTip) {
            int j = 0;
            if (item.toString().contains("trim")) {
                f = j + 4;
                break;
            } else {
                ++j;
            }
        }
        return f;
    }
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.Key event){
        if (Keybindings.boots.isDown()) {
            useEnchant(uniPlayer,0);
            ModMessages.sendToServer(new EnchantUseC2SPacket(0));
        }
        if (Keybindings.leggings.isDown()) {
            useEnchant(uniPlayer,1);
            ModMessages.sendToServer(new EnchantUseC2SPacket(1));
        }
        if (Keybindings.chestplate.isDown()) {
            useEnchant(uniPlayer,2);
            ModMessages.sendToServer(new EnchantUseC2SPacket(2));
        }
        if (Keybindings.helmet.isDown()) {
            useEnchant(uniPlayer,3);
            ModMessages.sendToServer(new EnchantUseC2SPacket(3));
        }


        if (!Minecraft.getInstance().options.keyJump.isDown()) {
            boosted = false;
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
                int trimCount = 0;
                for (int j = 0; j < 4; ++j) {
                    if (!player.getInventory().getArmor(j).toString().contains("air") && player.getInventory().getArmor(j).serializeNBT().toString().contains("Trim")) {
                        if (player.getInventory().getArmor(j).getTag().getAllKeys().contains("Trim")) {
                            Tag tag = player.getInventory().getArmor(j).getTag().get("Trim");
                            if (tag.toString().contains("vex")) {
                                ++trimCount;
                            }
                        }
                    }
                }
                if (trimCount > 0) {
                    player.removeEffect(MobEffects.REGENERATION);
                    enchVar.setVexTimer(System.currentTimeMillis() + (10000 - (trimCount * 1500L)));
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientCooldownDamageBonuses() {
        sendCooldownDamageBonuses(uniPlayer);
    }

    public static void sendCooldownDamageBonuses(Player player) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            for (EnchantmentManager manager : enchVar.getManagers()) {
                if (manager instanceof ArmorEnchantManager aManager) {
                    aManager.targetDamaged();
                }
            }
        });
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
            if (oldStore.getStoneTime() != 0 && event.getOriginal() instanceof ServerPlayer) {
                breakStone((ServerLevel) event.getOriginal().level(), (ServerPlayer) event.getOriginal());
            }
            event.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
                if (event.getEntity() instanceof ServerPlayer) {
                    ModMessages.sendToPlayer(new ArmorS2CPacket(0), (ServerPlayer) event.getEntity());
                }
            });
        });
        event.getOriginal().invalidateCaps();
    }
    @SubscribeEvent @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().level() instanceof ServerLevel) {
            event.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
                breakStone((ServerLevel)event.getEntity().level(), (ServerPlayer) event.getEntity());
            });
        }
    }
    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        event.getEntity().reviveCaps();
        event.getEntity().getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            System.out.println(enchVar.getHelmetID());
            if (event.getEntity() instanceof ServerPlayer) {
                ModMessages.sendToPlayer(new ArmorS2CPacket(0), (ServerPlayer) event.getEntity());
                ModMessages.sendToPlayer(new ArmorS2CPacket(1), (ServerPlayer) event.getEntity());
                ModMessages.sendToPlayer(new ArmorS2CPacket(2), (ServerPlayer) event.getEntity());
                ModMessages.sendToPlayer(new ArmorS2CPacket(3), (ServerPlayer) event.getEntity());
                if (enchVar.getStoneTime() != 0) {
                    ServerLevel level = null;
                    for (ServerLevel level1 : event.getEntity().level().getServer().getAllLevels()) {
                        if (level1.dimension().equals(event.getFrom())) {
                            level = level1;
                            break;
                        }
                    }
                    if (level != null) {
                        breakStone(level, (ServerPlayer) event.getEntity());
                        enchVar.setStoneTime(0);
                        enchVar.setStoneCeiling(0);
                    }
                }
            }
        });
    }
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerEnchantmentActions.class);
    }
    @SubscribeEvent @OnlyIn(Dist.CLIENT)
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity().equals(uniPlayer) && !boosted && uniPlayer.onGround()) {
            int trimCount = 0;
                for (int j = 0; j < 4; ++j) {
                    if (!uniPlayer.getInventory().getArmor(j).toString().contains("air") && uniPlayer.getInventory().getArmor(j).serializeNBT().toString().contains("Trim")) {
                        if (uniPlayer.getInventory().getArmor(j).getTag().getAllKeys().contains("Trim")) {
                            Tag tag = uniPlayer.getInventory().getArmor(j).getTag().get("Trim");
                            if (tag.toString().contains("raiser")) {
                                ++trimCount;
                            }
                        }
                    }
                }
            uniPlayer.addDeltaMovement(new Vec3(0, (double) trimCount /15,0));
            boosted = true;
        }
    }
    public static void breakStone(ServerLevel level, ServerPlayer player) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            int i = Math.abs(enchVar.getStoneLookX());
            if (Math.abs(enchVar.getStoneLookZ()) > Math.abs(enchVar.getStoneLookX())) {
                i = Math.abs(enchVar.getStoneLookZ());
            }
            int m0 = 1;
            int m1 = 1;
            if (enchVar.getStoneLookX() < 0) {
                m0 = -1;
            } else if (enchVar.getStoneLookX() == 0) {
                m0 = 0;
            }
            if (enchVar.getStoneLookZ() < 0) {
                m1 = -1;
            } else if (enchVar.getStoneLookZ() == 0) {
                m1 = 0;
            }
            int climb = 0;
            for (int j = 0; j <= i; ++j) {
                for (int p = 0; p < enchVar.getStoneCeiling(); ++p) {
                    if (!(level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb, enchVar.getStoneZ() + (j * m1))).getBlock().equals(Blocks.DRIPSTONE_BLOCK) || level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb, enchVar.getStoneZ() + (j * m1))).getBlock().equals(Blocks.POINTED_DRIPSTONE))) {
                        ++climb;
                    }
                    boolean findFloor = true;
                    while (findFloor) {
                        if (!(level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb - 1, enchVar.getStoneZ() + (j * m1))).getBlock().equals(Blocks.DRIPSTONE_BLOCK) || level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb - 1, enchVar.getStoneZ() + (j * m1))).getBlock().equals(Blocks.POINTED_DRIPSTONE)) && !level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb, enchVar.getStoneZ() + (j * m1))).is(BlockTags.REPLACEABLE)) {
                            findFloor = false;
                        } else {
                            --climb;
                        }
                    }
                    if (level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb, enchVar.getStoneZ() + (j * m1))).getBlock().equals(Blocks.DRIPSTONE_BLOCK) || level.getBlockState(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb, enchVar.getStoneZ() + (j * m1))).getBlock().equals(Blocks.POINTED_DRIPSTONE)) {
                        level.setBlock(new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb, enchVar.getStoneZ() + (j * m1)), Blocks.AIR.defaultBlockState(), 2);
                        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double) enchVar.getStoneX() + (j * m0), (double) enchVar.getStoneY() + p + climb, (double) enchVar.getStoneZ() + (j * m1), 1, 0, 0, 0, 0.01);
                        if (p == 0) {
                            level.playSound(null, new BlockPos(enchVar.getStoneX() + (j * m0), enchVar.getStoneY() + p + climb, enchVar.getStoneZ() + (j * m1)), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.PLAYERS, 2F, 5.0F);
                        }
                    }
                }
            }
            enchVar.setStoneTime(0);
            enchVar.setStoneCeiling(0);
        });
    }
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        event.player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {



            for (EnchantmentManager manager : enchVar.getManagers()) {
                if (manager != null) {
                    manager.tick();
                }
            }





            if (event.player.level() instanceof ServerLevel) {
                ServerPlayer player = (ServerPlayer) event.player;
                ServerLevel level = player.serverLevel();

                if (enchVar.getVexTimer() != 0 && enchVar.getVexTimer() < System.currentTimeMillis()) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,150,1));
                    enchVar.setVexTimer(0);
                }

                for (int j = 0; j < 4; ++j) {
                    String enchantments;
                    if (player.getInventory().getArmor(j).serializeNBT().toString().contains("Enchantments")) {
                        //System.out.println(player.getInventory().getArmor(j).getTag().get("Enchantments"));
                        enchantments = player.getInventory().getArmor(j).getTag().get("Enchantments").toString();
                    } else {
                        enchantments = "null";
                    }
                    if (!enchantments.equals(enchVar.getPlayerEnchantments().get(j))) {
                        enchVar.setPlayerEnchantments(enchantments, j);
                        ModMessages.sendToPlayer(new ArmorS2CPacket(j),player);
                    }
                    String trims;
                    if (player.getInventory().getArmor(j).serializeNBT().toString().contains("Trim")) {
                        //System.out.println(player.getInventory().getArmor(j).getTag().get("Enchantments"));
                        //System.out.println("omg!!! a trim!!!");
                        trims = "trim:" + player.getInventory().getArmor(j).getTag().get("Trim");
                    } else {
                        //System.out.println("lebruh");
                        trims = "trim:null";
                    }
                    if (!enchantments.equals(enchVar.getPlayerTrims().get(j))) {
                        enchVar.setPlayerTrims(trims, j);
                        applyBuffs(player);
                    }
                }

                //Phantom pain check
                if (enchVar.getPhantomVictim() != null) {
                    if (enchVar.getPhantomVictim().isAlive()) {
                        if (System.currentTimeMillis() >= enchVar.getPhantomDelay() + 4000) {
                            level.sendParticles(ModParticles.PHANTOM_HEAL_PARTICLE.get(), enchVar.getPhantomVictim().getX(), enchVar.getPhantomVictim().getY() + 1.5, enchVar.getPhantomVictim().getZ(), 5, Math.random(), Math.random(), Math.random(), 0.5);
                            level.playSound(null, enchVar.getPhantomVictim().blockPosition(), ModSounds.PHANTOM_HEAL.get(), SoundSource.PLAYERS, 0.25F, (float) 0.3 + (float) abs(Math.random() + 0.5));
                            enchVar.getPhantomVictim().heal(8);
                            enchVar.resetPhantomHurt();
                            player.getMainHandItem().serializeNBT().putInt("phurt",enchVar.getPhantomHurt());
                            enchVar.deletePhantomVictim();
                        }
                    }
                }

                //stone caller check
                if (enchVar.getStoneTime() != 0) {
                    if (System.currentTimeMillis() >= enchVar.getStoneTime() + 10000) {
                        breakStone(level,player);
                    }
                }
            }

            if (enchVar.getAcrobatBonus() && (event.player.onGround() || event.player.isInFluidType() || event.player.isPassenger())) {
                enchVar.setAcrobatBonus(false);
            }

            if (FMLEnvironment.dist.isClient() && false) {


                enchantmentTick();

                if (Minecraft.getInstance().mouseHandler.isLeftPressed()) {
                    KineticEnchantment.onKeyInputEvent();
                    RendEnchantment.onKeyInputEvent();
                }

                //refreshing time based variables
                if (refreshLegs) {
                    TrainDashEnchantment.dashing = false;
                    TrainDashEnchantment.trainDelay = System.currentTimeMillis();
                    EnchantmentHudOverlay.trainFrames = 0;

                    IncapacitateEnchantment.incaCool = System.currentTimeMillis();
                    EnchantmentHudOverlay.incaFrames = 0;


                    EnchantmentHudOverlay.judgeFrames = 0;

                    refreshLegs = false;
                }
                if (refreshChest) {

                    WardenspineEnchantment.blind = false;
                    WardenspineEnchantment.blinding = false;
                    WardenspineEnchantment.wardenCooldown = System.currentTimeMillis();
                    if (Minecraft.getInstance().getConnection() != null) {
                    ModMessages.sendToServer(new WardenspineC2SPacket(0));
                    }

                    ParryEnchantment.parryCooldown = System.currentTimeMillis();
                    EnchantmentHudOverlay.parryFrames = 0;

                    FlareEnchantment.charging = false;
                    FlareEnchantment.chargeTime = System.currentTimeMillis();
                    EnchantmentHudOverlay.flareFrames = 0;

                    ConcussionEnchantment.concussTime = System.currentTimeMillis();
                    EnchantmentHudOverlay.concussFrames = 0;

                    refreshChest = false;
                }
                if (refreshHelmet) {

                    WardenScreamEnchantment.wardenTime = System.currentTimeMillis();
                    EnchantmentHudOverlay.screamFrames = 0;

                    SmiteEnchantment.smiteTime = System.currentTimeMillis();
                    EnchantmentHudOverlay.smiteFrames = 0;

                    FlamethrowerEnchantment.flameo = false;
                    FlamethrowerEnchantment.flameDelay = 0;
                    EnchantmentHudOverlay.flameFrames = 0;
                    FlamethrowerEnchantment.flameTime = System.currentTimeMillis();

                    EnchantmentHudOverlay.blizzardFrames = 0;
                    BlizzardEnchantment.iceTime = System.currentTimeMillis();

                    EnchantmentHudOverlay.holeFrames = 0;
                    enchVar.setHoleCooldown(System.currentTimeMillis());

                    refreshHelmet = false;
                }

                //enchant persistent logic
                if (enchVar.getStoneTime() != 0) {
                    if (System.currentTimeMillis() >= enchVar.getStoneTime() + 10000) {
                        enchVar.setStoneTime(0);
                    }
                }
                TempoTheftEnchantment.onClientTick();
                if (spedUp) {
                    double d0 = uniPlayer.getDeltaMovement().x;
                    double d1 = uniPlayer.getDeltaMovement().y;
                    double d2 = uniPlayer.getDeltaMovement().z;
                    if ((Math.abs(d0) + Math.abs(d1) + Math.abs(d2)) <= 0.15) {
                        spedUp = false;
                        ModMessages.sendToServer(new MomentumC2SPacket(0));
                        loopCount = 0;
                    }
                }
                if (hit) {
                    ModMessages.sendToServer(new WeightedC2SPacket());
                }
            }
        });
    }

    private static void enchantmentTick() {
        uniPlayer.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            switch (enchVar.getBootID()) {
                case 1:
                    //CometStrikeEnchantment.onKeyInputEvent();
                    break;
                case 2:
                    DoubleJumpEnchantment.onClientTick();
                    break;
                case 3:
                    MomentumEnchantment.onClientTick();
                    break;
                case 4:
                    SkyChargeEnchantment.onClientTick();
                    break;
                case 5:
                    StoneCallerEnchantment.onClientTick();
                    break;
            }
            switch (enchVar.getLegID()) {
                case 1:
                    //TempestEnchantment.onClientTick();
                    break;
                case 2:
                    CrushEnchantment.onClientTick();
                    break;
                case 3:
                    IncapacitateEnchantment.onKeyInputEvent();
                    break;
                case 4:
                    break;
                case 5:
                    TrainDashEnchantment.onClientTick();
                    break;
            }
            switch (enchVar.getChestID()) {
                case 1:
                    ConcussionEnchantment.onKeyInputEvent();
                    break;
                case 2:
                    FlareEnchantment.onClientTick();
                    break;
                case 3:
                    ParryEnchantment.onKeyInputEvent();
                    break;
                case 4:
                    if (Keybindings.siphon.isDown()) {
                        SiphonEnchantment.onKeyInputEvent();
                    } else {
                        consumeClick = false;
                    }
                    break;
                case 5:
                    WardenspineEnchantment.onClientTick();
                    break;
            }
            switch (enchVar.getHelmetID()) {
                case 1:
                    //BlackHoleEnchantment.onKeyInputEvent();
                    break;
                case 2:
                    BlizzardEnchantment.onClientTick();
                    break;
                case 3:
                    FlamethrowerEnchantment.onClientTick();
                    break;
                case 4:
                    SmiteEnchantment.onKeyInputEvent();
                    break;
                case 5:
                    WardenScreamEnchantment.onKeyInputEvent();
                    break;
            }
        });
    }

    private static void applyBuffs(Player player) {
        player.getCapability(PlayerEnchantmentActionsProvider.PLAYER_ENCHANTMENT_ACTIONS).ifPresent(enchVar -> {
            int wayTrim = 0;
            int wildTrim = 0;
            int silenceTrim = 0;
            int snoutTrim = 0;
            int hostTrim = 0;
            int duneTrim = 0;
            int shaperTrim = 0;
            for (int j = 0; j < 4; ++j) {
                if (!player.getInventory().getArmor(j).toString().contains("air") && player.getInventory().getArmor(j).serializeNBT().toString().contains("tag")) {
                    if (player.getInventory().getArmor(j).getTag().getAllKeys().contains("Trim")) {
                        Tag tag = player.getInventory().getArmor(j).getTag().get("Trim");
                        //System.out.println(tag.toString().split());
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
            if (shaperTrim > 0 || shaperTrim != enchVar.getShaperBuff()) {
                player.getAttributes().getInstance(Attributes.ARMOR_TOUGHNESS).removeModifier(SHAPER_TOUGH_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.ARMOR_TOUGHNESS).addTransientModifier(new AttributeModifier(SHAPER_TOUGH_MODIFIER_UUID, "Shaper toughness boost", (double) shaperTrim * 2, AttributeModifier.Operation.ADDITION));
                enchVar.setShaperBuff(shaperTrim);
            }
            if (wayTrim > 0 || wayTrim != enchVar.getWayBuff()) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(WAYFINDER_SPEED_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).removeModifier(WAYFINDER_DAMAGE_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(WAYFINDER_DAMAGE_MODIFIER_UUID, "Wayfinder damage debuff", (double) -wayTrim / 2, AttributeModifier.Operation.ADDITION));
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(WAYFINDER_SPEED_MODIFIER_UUID, "Wayfinder speed boost", (double) wayTrim / 150, AttributeModifier.Operation.ADDITION));
                enchVar.setWayBuff(wayTrim);
            }
            if (wildTrim > 0 || wildTrim != enchVar.getWildBuff()) {
                player.getAttributes().getInstance(Attributes.MAX_HEALTH).removeModifier(WILD_HEALTH_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(WILD_HEALTH_MODIFIER_UUID, "Wild health boost", (double) wildTrim * 3, AttributeModifier.Operation.ADDITION));
                enchVar.setWildBuff(wildTrim);
            }
            if (snoutTrim > 0 || snoutTrim != enchVar.getSnoutBuff()) {
                player.getAttributes().getInstance(Attributes.ATTACK_SPEED).removeModifier(SNOUT_ATTACK_SPEED_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.MAX_HEALTH).removeModifier(SNOUT_HEALTH_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(SNOUT_ATTACK_SPEED_MODIFIER_UUID, "Snout attack speed boost", (double) snoutTrim / 6, AttributeModifier.Operation.ADDITION));
                player.getAttributes().getInstance(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(SNOUT_HEALTH_MODIFIER_UUID, "Snout health debuff", (double) -snoutTrim * 4, AttributeModifier.Operation.ADDITION));
                enchVar.setSnoutBuff(snoutTrim);
            }
            if (hostTrim > 0 || hostTrim != enchVar.getHostBuff()) {
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(HOST_ATTACK_SPEED_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(HOST_ATTACK_SPEED_MODIFIER_UUID, "Host speed debuff", (double) -hostTrim / 120, AttributeModifier.Operation.ADDITION));
                enchVar.setHostBuff(hostTrim);
            }
            if (duneTrim > 0 || duneTrim != enchVar.getDuneBuff()) {
                player.getAttributes().getInstance(ForgeMod.ENTITY_GRAVITY.get()).removeModifier(DUNE_GRAVITY_MODIFIER_UUID);
                player.getAttributes().getInstance(ForgeMod.ENTITY_GRAVITY.get()).addTransientModifier(new AttributeModifier(DUNE_GRAVITY_MODIFIER_UUID, "Dune gravity increase", (double) duneTrim/120, AttributeModifier.Operation.ADDITION));
                enchVar.setDuneBuff(duneTrim);
            }
            if (silenceTrim > 0 || silenceTrim != enchVar.getSilenceBuff()) {
                player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).removeModifier(SILENCE_DAMAGE_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.ATTACK_SPEED).removeModifier(SILENCE_ATTACK_SPEED_MODIFIER_UUID);
                player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(SILENCE_DAMAGE_MODIFIER_UUID, "silence damage boost", (double) silenceTrim/1.5, AttributeModifier.Operation.ADDITION));
                player.getAttributes().getInstance(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(SILENCE_ATTACK_SPEED_MODIFIER_UUID, "silence attack speed debuff", (double) -silenceTrim/5, AttributeModifier.Operation.ADDITION));
                enchVar.setSilenceBuff(silenceTrim);
            }
        });
    }


}

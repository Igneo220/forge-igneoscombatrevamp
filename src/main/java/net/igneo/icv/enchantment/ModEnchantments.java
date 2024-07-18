package net.igneo.icv.enchantment;

import net.igneo.icv.ICV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ICV.MOD_ID);

    public static RegistryObject<Enchantment> BLITZ =
            ENCHANTMENTS.register("blitz",
                    () -> new BlitzEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> BREAKTHROUGH =
            ENCHANTMENTS.register("breakthrough",
                    () -> new BreakthroughEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> COUNTERWEIGHTED =
            ENCHANTMENTS.register("counterweighted",
                    () -> new CounterweightedEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> GUST =
            ENCHANTMENTS.register("gust",
                    () -> new GustEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> KINETIC =
            ENCHANTMENTS.register("kinetic",
                    () -> new KineticEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> PHANTOM_PAIN =
            ENCHANTMENTS.register("phantom_pain",
                    () -> new PhantomPainEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> SKEWERING =
            ENCHANTMENTS.register("skewering",
                    () -> new SkeweringEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));



    public static RegistryObject<Enchantment> MITOSIS =
            ENCHANTMENTS.register("mitosis",
                    () -> new MitosisEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.CROSSBOW, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> REND =
            ENCHANTMENTS.register("rend",
                    () -> new RendEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.CROSSBOW, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> SCATTER =
            ENCHANTMENTS.register("scatter",
                    () -> new ScatterEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.CROSSBOW, EquipmentSlot.MAINHAND));



    public static RegistryObject<Enchantment> ACCELERATE =
            ENCHANTMENTS.register("accelerate",
                    () -> new AccelerateEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> PHASING =
            ENCHANTMENTS.register("phasing",
                    () -> new PhasingEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));



    public static RegistryObject<Enchantment> EXTRACT =
            ENCHANTMENTS.register("extract",
                    () -> new ExtractEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.TRIDENT, EquipmentSlot.MAINHAND));
    public static RegistryObject<Enchantment> RECOIL =
            ENCHANTMENTS.register("recoil",
                    () -> new RecoilEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.TRIDENT, EquipmentSlot.MAINHAND));



    public static RegistryObject<Enchantment> BLACK_HOLE =
            ENCHANTMENTS.register("black_hole",
                    () -> new BlackHoleEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));
    public static RegistryObject<Enchantment> BLIZZARD =
            ENCHANTMENTS.register("blizzard",
                    () -> new BlizzardEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));
    public static RegistryObject<Enchantment> FLAMETHROWER =
            ENCHANTMENTS.register("flamethrower",
                    () -> new FlamethrowerEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));



    public static RegistryObject<Enchantment> CONCUSSION =
            ENCHANTMENTS.register("concussion",
                    () -> new ConcussionEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static RegistryObject<Enchantment> FLARE =
            ENCHANTMENTS.register("flare",
                    () -> new FlareEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static RegistryObject<Enchantment> PARRY =
            ENCHANTMENTS.register("parry",
                    () -> new ParryEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));
    public static RegistryObject<Enchantment> SIPHON =
            ENCHANTMENTS.register("siphon",
                    () -> new SiphonEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));



    public static RegistryObject<Enchantment> ACROBATIC =
            ENCHANTMENTS.register("acrobatic",
                    () -> new AcrobaticEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static RegistryObject<Enchantment> CRUSH =
            ENCHANTMENTS.register("crush",
                    () -> new CrushEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static RegistryObject<Enchantment> INCAPACITATE =
            ENCHANTMENTS.register("incapacitate",
                    () -> new CrushEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));
    public static RegistryObject<Enchantment> JUDGEMENT =
            ENCHANTMENTS.register("judgement",
                    () -> new JudgementEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));



    public static RegistryObject<Enchantment> COMET_STRIKE =
            ENCHANTMENTS.register("comet_strike",
                    () -> new CometStrikeEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> DOUBLE_JUMP =
            ENCHANTMENTS.register("double_jump",
                    () -> new DoubleJumpEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> MOMENTUM =
            ENCHANTMENTS.register("momentum",
                    () -> new MomentumEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> SKY_CHARGE =
            ENCHANTMENTS.register("sky_charge",
                    () -> new SkyChargeEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));
    public static RegistryObject<Enchantment> STONE_CALLER =
            ENCHANTMENTS.register("stone_caller",
                    () -> new StoneCallerEnchantment(Enchantment.Rarity.UNCOMMON,
                            EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET));



    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }

}

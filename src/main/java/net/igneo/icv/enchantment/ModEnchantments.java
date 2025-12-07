package net.igneo.icv.enchantment;

import net.igneo.icv.ICV;
import net.igneo.icv.enchantment.armor.boots.*;
import net.igneo.icv.enchantment.armor.chestplate.*;
import net.igneo.icv.enchantment.armor.helmet.*;
import net.igneo.icv.enchantment.armor.leggings.*;
import net.igneo.icv.enchantment.tool.BruteTouchEnchantment;
import net.igneo.icv.enchantment.trident.*;
import net.igneo.icv.enchantment.weapon.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.world.entity.EquipmentSlot.*;
import static net.minecraft.world.item.enchantment.Enchantment.Rarity.UNCOMMON;
import static net.minecraft.world.item.enchantment.EnchantmentCategory.*;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ICV.MOD_ID);
    
    private static RegistryObject<Enchantment> registerEnchantment(String name, Supplier<Enchantment> supplier) {
        return ENCHANTMENTS.register(name, supplier);
    }
    
    // Pickaxe Enchantments
    public static final RegistryObject<Enchantment> BRUTE_TOUCH = registerEnchantment("brute_touch", () -> new BruteTouchEnchantment(UNCOMMON, EnchantmentCategory.DIGGER, MAINHAND));
    
    // Weapon Enchantments
    public static final RegistryObject<Enchantment> CASCADE = registerEnchantment("cascade", () -> new CascadeEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> BURST = registerEnchantment("burst", () -> new BurstEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> TUNGSTEN_CORE = registerEnchantment("tungsten_core", () -> new TungstenCoreEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> FINESSE = registerEnchantment("finesse", () -> new FinesseEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> GUST = registerEnchantment("gust", () -> new GustEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> VIPER = registerEnchantment("viper", () -> new ViperEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> COMET_STRIKE = registerEnchantment("comet_strike", () -> new CometStrikeEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> KINETIC = registerEnchantment("kinetic", () -> new KineticEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> VOLATILE = registerEnchantment("volatile", () -> new VolatileEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> MOLTEN = registerEnchantment("molten", () -> new MoltenEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> BREAKTHROUGH = registerEnchantment("breakthrough", () -> new BreakthroughEnchantment(UNCOMMON, WEAPON, MAINHAND));
    public static final RegistryObject<Enchantment> MEATHOOK = registerEnchantment("meathooks", () -> new MeathookEnchantment(UNCOMMON, WEAPON, MAINHAND));
    
    // Trident Enchantments
    public static final RegistryObject<Enchantment> GEYSER = registerEnchantment("geyser", () -> new GeyserEnchantment(UNCOMMON, TRIDENT, MAINHAND));
    public static final RegistryObject<Enchantment> WHIRLPOOL = registerEnchantment("whirlpool", () -> new WhirlpoolEnchantment(UNCOMMON, TRIDENT, MAINHAND));
    public static final RegistryObject<Enchantment> UPWELL = registerEnchantment("upwell", () -> new UpwellEnchantment(UNCOMMON, TRIDENT, MAINHAND));
    public static final RegistryObject<Enchantment> CAVITATION = registerEnchantment("cavitation", () -> new CavitationEnchantment(UNCOMMON, TRIDENT, MAINHAND));
    public static final RegistryObject<Enchantment> UNDERTOW = registerEnchantment("undertow", () -> new UndertowEnchantment(UNCOMMON, TRIDENT, MAINHAND));
    
    // Helmet Enchantments
    public static final RegistryObject<Enchantment> BLACK_HOLE = registerEnchantment("black_hole", () -> new BlackHoleEnchantment(UNCOMMON, ARMOR_HEAD, HEAD));
    public static final RegistryObject<Enchantment> RIFT_RIPPER = registerEnchantment("rift_ripper", () -> new RiftRipperEnchantment(UNCOMMON, ARMOR_HEAD, HEAD));
    public static final RegistryObject<Enchantment> GRAVITY_WELL = registerEnchantment("gravity_well", () -> new GravityWellEnchantment(UNCOMMON, ARMOR_HEAD, HEAD));
    public static final RegistryObject<Enchantment> GLACIAL_IMPASSE = registerEnchantment("glacial_impasse", () -> new GlacialImpasseEnchantment(UNCOMMON, ARMOR_HEAD, HEAD));
    public static final RegistryObject<Enchantment> DIVINE_SMITE = registerEnchantment("divine_smite", () -> new DivineSmiteEnchantment(UNCOMMON, ARMOR_HEAD, HEAD));
    public static final RegistryObject<Enchantment> VOLCANO = registerEnchantment("volcano", () -> new VolcanoEnchantment(UNCOMMON, ARMOR_HEAD, HEAD));
    
    // Chestplate Enchantments
    public static final RegistryObject<Enchantment> PLANAR_SHIFT = registerEnchantment("planar_shift", () -> new PlanarShiftEnchantment(UNCOMMON, ARMOR_CHEST, CHEST));
    public static final RegistryObject<Enchantment> HAUNT = registerEnchantment("haunt", () -> new HauntEnchantment(UNCOMMON, ARMOR_CHEST, CHEST));
    public static final RegistryObject<Enchantment> MILKY_CHRYSALIS = registerEnchantment("milky_chrysalis", () -> new MilkyChrysalisEnchantment(UNCOMMON, ARMOR_CHEST, CHEST));
    public static final RegistryObject<Enchantment> IMMOLATE = registerEnchantment("immolate", () -> new ImmolateEnchantment(UNCOMMON, ARMOR_CHEST, CHEST));
    public static final RegistryObject<Enchantment> EXTINCTION = registerEnchantment("extinction", () -> new ExtinctionEnchantment(UNCOMMON, ARMOR_CHEST, CHEST));
    public static final RegistryObject<Enchantment> ABYSS_OMEN = registerEnchantment("abyss_omen", () -> new AbyssOmenEnchantment(UNCOMMON, ARMOR_CHEST, CHEST));
    
    // Leggings Enchantments
    public static final RegistryObject<Enchantment> TEMPEST = registerEnchantment("tempest", () -> new TempestEnchantment(UNCOMMON, ARMOR_LEGS, LEGS));
    public static final RegistryObject<Enchantment> JUDGEMENT = registerEnchantment("judgement", () -> new JudgementEnchantment(UNCOMMON, ARMOR_LEGS, LEGS));
    public static final RegistryObject<Enchantment> GALE = registerEnchantment("gale", () -> new GaleEnchantment(UNCOMMON, ARMOR_LEGS, LEGS));
    public static final RegistryObject<Enchantment> TSUNAMI = registerEnchantment("tsunami", () -> new TsunamiEnchantment(UNCOMMON, ARMOR_LEGS, LEGS));
    public static final RegistryObject<Enchantment> VOID_WAKE = registerEnchantment("void_wake", () -> new VoidWakeEnchantment(UNCOMMON, ARMOR_LEGS, LEGS));
    public static final RegistryObject<Enchantment> HURRICANE = registerEnchantment("hurricane", () -> new HurricaneEnchantment(UNCOMMON, ARMOR_LEGS, LEGS));
    
    // Boots Enchantments
    public static final RegistryObject<Enchantment> STASIS = registerEnchantment("stasis", () -> new StasisEnchantment(UNCOMMON, ARMOR_FEET, FEET));
    public static final RegistryObject<Enchantment> SURF = registerEnchantment("surf", () -> new SurfEnchantment(UNCOMMON, ARMOR_FEET, FEET));
    public static final RegistryObject<Enchantment> BLINK = registerEnchantment("blink", () -> new BlinkEnchantment(UNCOMMON, ARMOR_FEET, FEET));
    public static final RegistryObject<Enchantment> STONE_CALLER = registerEnchantment("stone_caller", () -> new StoneCallerEnchantment(UNCOMMON, ARMOR_FEET, FEET));
    public static final RegistryObject<Enchantment> ASCENSION = registerEnchantment("ascension", () -> new AscensionEnchantment(UNCOMMON, ARMOR_FEET, FEET));
    
    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}


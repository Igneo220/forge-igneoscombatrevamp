package net.igneo.icv.config;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.common.ForgeConfigSpec;

public class ICVClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> HELMET_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> HELMET_Y;
    public static final ForgeConfigSpec.ConfigValue<Integer> CHESTPLATE_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> CHESTPLATE_Y;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEGGINGS_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> LEGGINGS_Y;

    static {
        BUILDER.push("ICV CONFIGS");

        //CONFIGS HERE
        HELMET_X = BUILDER.comment("addition to the X coordinate of your helmet cooldown indicators")
                .define("Helmet X offset", 0);
        HELMET_Y = BUILDER.comment("addition to the Y coordinate of your helmet cooldown indicators")
                .define("Helmet Y offset", 0);

        CHESTPLATE_X = BUILDER.comment("addition to the X coordinate of your chestplate cooldown indicators")
                .define("Chestplate X offset", 0);
        CHESTPLATE_Y = BUILDER.comment("addition to the Y coordinate of your chestplate cooldown indicators")
                .define("Chestplate Y offset", 0);

        LEGGINGS_X = BUILDER.comment("addition to the Y coordinate of your leggings cooldown indicators")
                .define("Leggings X offset", 0);
        LEGGINGS_Y = BUILDER.comment("addition to the Y coordinate of your leggings cooldown indicators")
                .define("Leggings Y offset", 0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

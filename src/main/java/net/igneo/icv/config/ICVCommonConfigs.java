package net.igneo.icv.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ICVCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> TRIM_EFFECTS;

    static {
        BUILDER.push("ICV CONFIGS");

        //CONFIGS HERE
        TRIM_EFFECTS = BUILDER.comment("Do you want trim effects to be applied?(Highly recommended, it re-adds most vanilla enchantments. If you wanted them to simply be cosmetic I recommend using cosmetic armor)")
                       .define("Trim effects:", true);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

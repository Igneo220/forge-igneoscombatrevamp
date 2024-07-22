package net.igneo.icv.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ICVCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> INCAPACITATE_COOLDOWN;

    static {
        BUILDER.push("ICV CONFIGS");

        //CONFIGS HERE
        INCAPACITATE_COOLDOWN = BUILDER.comment("How long do you want the incapacitate cooldown to be? (in milliseconds)")
                       .define("Incapacitate cooldown", 10000);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}

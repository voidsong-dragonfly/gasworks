package voidsong.gasworks.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GSServerConfig {
    // Configs
    public static final ModConfigSpec.BooleanValue NON_METAL_RECIPE_MODIFICATIONS;
    public static final ModConfigSpec.BooleanValue METAL_RECIPE_MODIFICATIONS;
    // Init & technical variables
    public static final ModConfigSpec CONFIG_SPEC;
    static
    {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        NON_METAL_RECIPE_MODIFICATIONS = builder
            .comment("Should modifications to non-metal Vanilla recipes for realism be enabled? WARNING: This will make the game harder & make some resources non-renewable! (DEFAULT: FALSE)")
            .define("nonMetalRecipeModifications", false);
        METAL_RECIPE_MODIFICATIONS = builder
            .comment("Should modifications to metal Vanilla recipes for realism be enabled? WARNING: This will make the game harder & make some resources non-renewable! (DEFAULT: TRUE)")
            .define("metalRecipeModifications", true);
        CONFIG_SPEC = builder.build();
    }
}
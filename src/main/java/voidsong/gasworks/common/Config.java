package voidsong.gasworks.common;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import voidsong.gasworks.Gasworks;

@EventBusSubscriber(modid = Gasworks.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue NON_METAL_RECIPE_MODIFICATIONS = BUILDER
        .comment("Should modifications to non-metal Vanilla recipes for realism be enabled? WARNING: This will make the game harder & make some resources non-renewable!! (DEFAULT: FALSE)")
        .define("nonMetalRecipeModifications", false);
    private static final ModConfigSpec.BooleanValue METAL_RECIPE_MODIFICATIONS = BUILDER
        .comment("Should modifications to metal Vanilla recipes for realism be enabled? WARNING: This will make the game harder & make some resources non-renewable!! (DEFAULT: TRUE)")
        .define("metalRecipeModifications", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean nonMetalRecipeModifications;
    public static boolean metalRecipeModifications;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        nonMetalRecipeModifications = NON_METAL_RECIPE_MODIFICATIONS.get();
        metalRecipeModifications = METAL_RECIPE_MODIFICATIONS.get();
    }
}

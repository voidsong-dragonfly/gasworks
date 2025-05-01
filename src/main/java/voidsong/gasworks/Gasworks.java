package voidsong.gasworks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import voidsong.gasworks.common.Config;
import voidsong.gasworks.common.registries.RecipeConditions;

@Mod(Gasworks.MOD_ID)
public class Gasworks {
    public static final String MOD_ID = "gasworks";

    public Gasworks(IEventBus modEventBus, ModContainer modContainer) {
        // Register mod content
        //BLOCKS.register(modEventBus);
        //ITEMS.register(modEventBus);
        //CREATIVE_MODE_TABS.register(modEventBus);
        RecipeConditions.CONDITION_CODECS.register(modEventBus);
        // Register configs
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}

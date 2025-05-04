package voidsong.gasworks;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import voidsong.gasworks.common.Config;
import voidsong.gasworks.common.registry.Items;
import voidsong.gasworks.common.registry.RecipeConditions;

@Mod(Gasworks.MOD_ID)
public class Gasworks {
    public static final String MOD_ID = "gasworks";

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public Gasworks(IEventBus modEventBus, ModContainer modContainer) {
        // Register mod content
        //BLOCKS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        Items.CREATIVE_MODE_TABS.register(modEventBus);
        RecipeConditions.CONDITION_CODECS.register(modEventBus);
        // Register configs
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}

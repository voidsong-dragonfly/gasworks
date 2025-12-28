package voidsong.gasworks;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import voidsong.gasworks.api.recipe.GSRecipeTypes;
import voidsong.gasworks.common.config.GSServerConfig;
import voidsong.gasworks.common.registry.GSBlocks;
import voidsong.gasworks.common.registry.GSDataComponents;
import voidsong.gasworks.common.registry.GSItems;
import voidsong.gasworks.common.registry.GSRecipeConditions;

@Mod(Gasworks.MOD_ID)
public class Gasworks {
    public static final String MOD_ID = "gasworks";

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public Gasworks(IEventBus modEventBus, ModContainer container) {
        // Register mod content
        GSBlocks.BLOCKS.register(modEventBus);
        GSItems.ITEMS.register(modEventBus);
        GSItems.CREATIVE_MODE_TABS.register(modEventBus);
        GSRecipeConditions.CONDITION_CODECS.register(modEventBus);
        GSDataComponents.COMPONENTS.register(modEventBus);
        GSRecipeTypes.RECIPE_TYPES.register(modEventBus);
        // Register configs
        container.registerConfig(ModConfig.Type.SERVER, GSServerConfig.CONFIG_SPEC);
    }
}

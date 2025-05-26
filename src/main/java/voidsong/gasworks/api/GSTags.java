package voidsong.gasworks.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import voidsong.gasworks.Gasworks;

public class GSTags {

    private static ResourceLocation cLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    private static TagKey<Block> blockTag(ResourceLocation name) {
        return TagKey.create(Registries.BLOCK, name);
    }

    private static TagKey<Item> itemTag(ResourceLocation name) {
        return TagKey.create(Registries.ITEM, name);
    }

    public static class BlockTags {
        /*
         * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
         */
        // Pyrolysis walls for use in recipes
        public static final TagKey<Block> PYROLIZING_WALLS = blockTag(Gasworks.rl("pyrolyzing_walls"));
        // Log stacks for fuel
        public static final TagKey<Block> LOG_PILES = blockTag(Gasworks.rl("log_piles"));
        // Coal-like piles for fuel
        public static final TagKey<Block> COALLIKE_PILES = blockTag(Gasworks.rl("coallike_piles"));
        // Compost-accelerating blocks
        public static final TagKey<Block> COMPOST_ACCELERATORS = blockTag(Gasworks.rl("compost_accelerators"));
    }

    public static class ItemTags {
        /*
         * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
         */
        // Pyrolysis walls for use in recipes
        public static final TagKey<Item> PYROLIZING_WALLS = itemTag(Gasworks.rl("pyrolyzing_walls"));
        // Log stacks for fuel
        public static final TagKey<Item> LOG_PILES = itemTag(Gasworks.rl("log_piles"));
        // Coal-like piles for fuel
        public static final TagKey<Item> COALLIKE_PILES = itemTag(Gasworks.rl("coallike_piles"));
        // Ash products
        public static final TagKey<Item> DUSTS = itemTag(cLoc("dusts"));
        public static final TagKey<Item> ASH_DUST = itemTag(cLoc("dusts/ash"));
        public static final TagKey<Item> ASH = itemTag(cLoc("ash"));
        // Coke products
        public static final TagKey<Item> COAL_COKE = itemTag(cLoc("coal_coke"));
        public static final TagKey<Item> COMPOST_ACCELERATORS_DISPLAY = itemTag(Gasworks.rl("compost_accelerators_display"));
    }
}

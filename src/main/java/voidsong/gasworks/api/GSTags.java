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
        /*
         * Building blocks, including various 'functional' blocks
         */
        public static final TagKey<Block> CANDELABRAS = blockTag(Gasworks.rl("candelabras"));
        /*
         * Compatibility tags, other mods & Gasworks
         */
        // Tags for torch dowsing
        public static final TagKey<Block> DOWSE_IN_RAIN = blockTag(Gasworks.rl("dowse_in_rain"));
        public static final TagKey<Block> DOWSE_IN_WATER = blockTag(Gasworks.rl("dowse_in_water"));
        public static final TagKey<Block> LOW_LIGHT_TORCHES = blockTag(Gasworks.rl("low_light_torches"));
        // Tags for Fire Drill lighting
        public static final TagKey<Block> CONTAINS_TINDER = blockTag(Gasworks.rl("contains_tinder"));
        /*
         * Block break tags for the tool system
         */
        public static final TagKey<Block> INCORRECT_FOR_CRUDE_TOOL = blockTag(Gasworks.rl("tool_tiers/incorrect_for_crude_tool"));
        public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = blockTag(Gasworks.rl("tool_tiers/incorrect_for_copper_tool"));
        public static final TagKey<Block> INCORRECT_FOR_IRON_TOOL = blockTag(Gasworks.rl("tool_tiers/incorrect_for_iron_tool"));
        public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = blockTag(Gasworks.rl("tool_tiers/incorrect_for_steel_tool"));
        public static final TagKey<Block> INCORRECT_FOR_HSNA_TOOL = blockTag(Gasworks.rl("tool_tiers/incorrect_for_hsna_tool"));
        public static final TagKey<Block> NEEDS_CRUDE_TOOL = blockTag(Gasworks.rl("tool_tiers/needs_crude_tool"));
        public static final TagKey<Block> NEEDS_COPPER_TOOL = blockTag(Gasworks.rl("tool_tiers/needs_copper_tool"));
        public static final TagKey<Block> NEEDS_IRON_TOOL = blockTag(Gasworks.rl("tool_tiers/needs_iron_tool"));
        public static final TagKey<Block> NEEDS_STEEL_TOOL = blockTag(Gasworks.rl("tool_tiers/needs_steel_tool"));
        public static final TagKey<Block> NEEDS_HSNA_TOOL = blockTag(Gasworks.rl("tool_tiers/needs_hsna_tool"));
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
        /*
         * Building blocks, including various 'functional' blocks
         */
        public static final TagKey<Item> CANDELABRAS = itemTag(Gasworks.rl("candelabras"));
        /*
         * Building blocks, including various 'functional' blocks
         */
        public static final TagKey<Item> REFRACTORY_BRICK = itemTag(cLoc("bricks/refractory"));
        public static final TagKey<Item> FIREBRICK = itemTag(cLoc("bricks/firebrick"));
    }
}

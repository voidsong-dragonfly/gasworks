package voidsong.gasworks.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import voidsong.gasworks.Gasworks;

public class MaterialTags {

    private static ResourceLocation cLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    private static TagKey<Block> blockTag(ResourceLocation name) {
        return TagKey.create(Registries.BLOCK, name);
    }

    private static TagKey<Item> itemTag(ResourceLocation name) {
        return TagKey.create(Registries.ITEM, name);
    }

    public static class Blocks {
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

    public static class Items {
        // Copper nuggets as Vanilla doesn't have those yet
        public static final TagKey<Item> NUGGET_COPPER = itemTag(cLoc("nuggets/copper"));
        // Steel
        public static final TagKey<Item> NUGGET_STEEL = itemTag(cLoc("nuggets/steel"));
        public static final TagKey<Item> INGOT_STEEL = itemTag(cLoc("ingots/steel"));
        // Latenium
        // HSNA
        public static final TagKey<Item> NUGGET_HSNA = itemTag(cLoc("nuggets/hsna"));
        public static final TagKey<Item> INGOT_HSNA = itemTag(cLoc("ingots/hsna"));
    }
}

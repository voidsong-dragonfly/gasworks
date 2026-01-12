package voidsong.gasworks.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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

    public static class BlockTags {

    }

    public static class ItemTags {
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

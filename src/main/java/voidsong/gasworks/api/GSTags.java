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
        public static final TagKey<Block> PYROLIZING_WALLS = blockTag(Gasworks.rl("pyrolyzing_walls"));
    }

    public static class ItemTags {
        public static final TagKey<Item> COAL_COKE = itemTag(cLoc("coal_coke"));
        public static final TagKey<Item> DUSTS = itemTag(cLoc("dusts"));
        public static final TagKey<Item> ASH_DUST = itemTag(cLoc("dusts/ash"));
        public static final TagKey<Item> ASH = itemTag(cLoc("ash"));
    }
}

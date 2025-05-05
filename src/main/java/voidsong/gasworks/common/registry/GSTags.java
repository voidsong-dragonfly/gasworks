package voidsong.gasworks.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import voidsong.gasworks.Gasworks;

public class GSTags {

    private static ResourceLocation cLoc(String path)
    {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    private static TagKey<Block> blockTag(ResourceLocation name) {
        return TagKey.create(Registries.BLOCK, name);
    }

    public static class BlockTags {
        public static final TagKey<Block> PYROLIZING_WALLS = blockTag(Gasworks.rl("pyrolyzing_walls"));
    }

    public static class ItemTags {

    }
}

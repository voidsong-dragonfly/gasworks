package voidsong.gasworks.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.registry.GSBlocks;
import voidsong.gasworks.common.registry.GSTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class GasworksBlockTagsProvider extends BlockTagsProvider {
    public GasworksBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Gasworks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider lookupProvider) {
        /*
         * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
         */
        tag(GSTags.BlockTags.HIGH_TEMPERATURE_PYROLIZING_WALLS)
            .addTag(BlockTags.STONE_BRICKS).remove(Blocks.CHISELED_STONE_BRICKS)
            .add(Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_TILES)
            .add(Blocks.TUFF_BRICKS)
            .add(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
            .add(Blocks.BRICKS, Blocks.NETHER_BRICKS, Blocks.MUD_BRICKS)
            .add(Blocks.END_STONE_BRICKS);
        tag(GSTags.BlockTags.PYROLIZING_WALLS)
            .addTag(GSTags.BlockTags.HIGH_TEMPERATURE_PYROLIZING_WALLS)
            .addTag(BlockTags.DIRT).remove(Blocks.MOSS_BLOCK, Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS);
        tag(BlockTags.LOGS_THAT_BURN)
            .add(GSBlocks.OAK_LOG_PILE.get(), GSBlocks.SPRUCE_LOG_PILE.get(), GSBlocks.BIRCH_LOG_PILE.get(),
                GSBlocks.JUNGLE_LOG_PILE.get(), GSBlocks.ACACIA_LOG_PILE.get(), GSBlocks.DARK_OAK_LOG_PILE.get(),
                GSBlocks.CHERRY_LOG_PILE.get(), GSBlocks.MANGROVE_LOG_PILE.get(), GSBlocks.BAMBOO_LOG_PILE.get());
        /*
         * Tool tags for block breaking
         */
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(GSBlocks.OAK_LOG_PILE.get(), GSBlocks.SPRUCE_LOG_PILE.get(), GSBlocks.BIRCH_LOG_PILE.get(),
                 GSBlocks.JUNGLE_LOG_PILE.get(), GSBlocks.ACACIA_LOG_PILE.get(), GSBlocks.DARK_OAK_LOG_PILE.get(),
                 GSBlocks.CHERRY_LOG_PILE.get(), GSBlocks.MANGROVE_LOG_PILE.get(), GSBlocks.BAMBOO_LOG_PILE.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(GSBlocks.PYROLYTIC_ASH.get())
            .add(GSBlocks.CLAY_BRICK_PILE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(GSBlocks.COAL_PILE.get());

    }
}

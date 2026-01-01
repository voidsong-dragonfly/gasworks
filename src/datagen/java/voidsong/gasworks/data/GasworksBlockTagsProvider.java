package voidsong.gasworks.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import vectorwing.farmersdelight.common.tag.ModTags;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.CandelabraBlock;
import voidsong.gasworks.common.registry.GSBlocks;
import voidsong.gasworks.api.GSTags;

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
        // Pyrolysis walls for use in recipes
        tag(GSTags.BlockTags.PYROLIZING_WALLS)
            .addTag(BlockTags.DIRT).remove(Blocks.MOSS_BLOCK, Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS);
        // Log stacks for fuel
        tag(BlockTags.LOGS_THAT_BURN)
            .add(GSBlocks.OAK_LOG_PILE.get(), GSBlocks.SPRUCE_LOG_PILE.get(), GSBlocks.BIRCH_LOG_PILE.get(),
                GSBlocks.JUNGLE_LOG_PILE.get(), GSBlocks.ACACIA_LOG_PILE.get(), GSBlocks.DARK_OAK_LOG_PILE.get(),
                GSBlocks.CHERRY_LOG_PILE.get(), GSBlocks.MANGROVE_LOG_PILE.get(), GSBlocks.BAMBOO_LOG_PILE.get());
        tag(GSTags.BlockTags.LOG_PILES)
            .add(GSBlocks.OAK_LOG_PILE.get(), GSBlocks.SPRUCE_LOG_PILE.get(), GSBlocks.BIRCH_LOG_PILE.get(),
                GSBlocks.JUNGLE_LOG_PILE.get(), GSBlocks.ACACIA_LOG_PILE.get(), GSBlocks.DARK_OAK_LOG_PILE.get(),
                GSBlocks.CHERRY_LOG_PILE.get(), GSBlocks.MANGROVE_LOG_PILE.get(), GSBlocks.BAMBOO_LOG_PILE.get());
        // Coal-like piles for fuel
        tag(GSTags.BlockTags.COALLIKE_PILES)
            .add(GSBlocks.CHARCOAL_PILE.get(), GSBlocks.COAL_PILE.get());
        // Compost-accelerating blocks
        tag(GSTags.BlockTags.COMPOST_ACCELERATORS)
            // Standard blocks
            .add(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.GLOW_LICHEN)
            .add(Blocks.MOSS_BLOCK, Blocks.MOSS_CARPET)
            .add(Blocks.MYCELIUM, Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.MUSHROOM_STEM)
            // Nether blocks
            .add(Blocks.NETHER_WART)
            .add(Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_ROOTS, Blocks.CRIMSON_ROOTS, Blocks.NETHER_SPROUTS)
            .addTag(BlockTags.NYLIUM)
            // Farmer's Delight compat
            .remove(Blocks.PODZOL)
            .addOptionalTag(ModTags.COMPOST_ACTIVATORS);
        tag(BlockTags.MUSHROOM_GROW_BLOCK)
            .add(GSBlocks.COMPOST_PILE.get());
        /*
         * Building blocks, including various 'functional' blocks
         */
        // Fireclay blocks of various types
        tag(BlockTags.AZALEA_ROOT_REPLACEABLE)
            .add(GSBlocks.FIRECLAY.get());
        tag(BlockTags.AXOLOTLS_SPAWNABLE_ON)
            .add(GSBlocks.FIRECLAY.get());
        tag(BlockTags.SMALL_DRIPLEAF_PLACEABLE)
            .add(GSBlocks.FIRECLAY.get());
        tag(BlockTags.BIG_DRIPLEAF_PLACEABLE)
            .add(GSBlocks.FIRECLAY.get());
        tag(BlockTags.SCULK_REPLACEABLE)
            .add(GSBlocks.FIRECLAY.get());
        tag(BlockTags.LUSH_GROUND_REPLACEABLE)
            .add(GSBlocks.FIRECLAY.get());
        tag(BlockTags.WALLS)
            .add(GSBlocks.FIREBRICK_WALL.get());
        // Framed glass
        tag(BlockTags.IMPERMEABLE)
            .add(GSBlocks.FRAMED_GLASS.get());
        tag(Tags.Blocks.GLASS_BLOCKS)
            .add(GSBlocks.FRAMED_GLASS.get());
        tag(Tags.Blocks.GLASS_BLOCKS_COLORLESS)
            .add(GSBlocks.FRAMED_GLASS.get());
        tag(Tags.Blocks.GLASS_PANES)
            .add(GSBlocks.FRAMED_GLASS_PANE.get());
        tag(Tags.Blocks.GLASS_PANES_COLORLESS)
            .add(GSBlocks.FRAMED_GLASS_PANE.get());
        for(DeferredBlock<StainedGlassBlock> block : GSBlocks.STAINED_FRAMED_GLASS) {
            StainedGlassBlock glass = block.get();
            tag(BlockTags.IMPERMEABLE).add(glass);
            tag(Tags.Blocks.GLASS_BLOCKS).add(glass);
            tag(Tags.Blocks.DYED).add(glass);
            tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyed/" + glass.getColor())))
                .add(glass);
        }
        for(DeferredBlock<StainedGlassPaneBlock> block : GSBlocks.STAINED_FRAMED_GLASS_PANES) {
            StainedGlassPaneBlock glass = block.get();
            tag(Tags.Blocks.GLASS_PANES).add(glass);
            tag(Tags.Blocks.DYED).add(glass);
            tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyed/" + glass.getColor())))
                .add(glass);
        }
        // Candelabras
        tag(GSTags.BlockTags.CANDELABRAS)
            .add(GSBlocks.CANDELABRA.get());
        for(Pair<DyeColor, DeferredBlock<CandelabraBlock>> pair : GSBlocks.CANDELABRAS) {
            CandelabraBlock candelabra = pair.getSecond().get();
            tag(GSTags.BlockTags.CANDELABRAS).add(candelabra);
            tag(Tags.Blocks.DYED).add(candelabra);
            tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyed/" + pair.getFirst())))
                .add(candelabra);
        }
        /*
         * Tool tags for block breaking
         */
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(GSBlocks.OAK_LOG_PILE.get(), GSBlocks.SPRUCE_LOG_PILE.get(), GSBlocks.BIRCH_LOG_PILE.get(),
                 GSBlocks.JUNGLE_LOG_PILE.get(), GSBlocks.ACACIA_LOG_PILE.get(), GSBlocks.DARK_OAK_LOG_PILE.get(),
                 GSBlocks.CHERRY_LOG_PILE.get(), GSBlocks.MANGROVE_LOG_PILE.get(), GSBlocks.BAMBOO_LOG_PILE.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(GSBlocks.PYROLYTIC_ASH.get())
            .add(GSBlocks.UNFIRED_BRICK_CLAMP.get())
            .add(GSBlocks.FIRED_BRICK_CLAMP.get())
            .add(GSBlocks.UNFIRED_FIREBRICK_CLAMP.get())
            .add(GSBlocks.FIRED_FIREBRICK_CLAMP.get())
            .add(GSBlocks.COAL_PILE.get())
            .add(GSBlocks.CHARCOAL_PILE.get())
            .add(GSBlocks.COMPOST_PILE.get())
            .add(GSBlocks.FIRECLAY.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(GSBlocks.FIREBRICKS.get(), GSBlocks.FIREBRICK_STAIRS.get(), GSBlocks.FIREBRICK_SLAB.get(), GSBlocks.FIREBRICK_WALL.get())
            .addAll(GSBlocks.FIREBRICK_SILLS.stream().map(DeferredHolder::getKey).toList())
            .addAll(GSBlocks.FIREBRICK_QUOINS.stream().map(DeferredHolder::getKey).toList())
            .addAll(GSBlocks.BRICK_SILLS.stream().map(DeferredHolder::getKey).toList())
            .addAll(GSBlocks.BRICK_QUOINS.stream().map(DeferredHolder::getKey).toList());
        /*
         * Compatibility tags, other mods & Gasworks
         */
        // Tags from Gasworks
        tag(GSTags.BlockTags.DOWSE_IN_RAIN)
            .add(Blocks.TORCH, Blocks.WALL_TORCH);
        tag(GSTags.BlockTags.DOWSE_IN_WATER)
            .add(Blocks.TORCH, Blocks.WALL_TORCH)
            .add(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);
        tag(GSTags.BlockTags.LOW_LIGHT_TORCHES)
            .add(Blocks.TORCH, Blocks.WALL_TORCH)
            .add(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);
        // Tags from other mods
        tag(ModTags.MUSHROOM_COLONY_GROWABLE_ON)
            .add(GSBlocks.COMPOST_PILE.get());
        tag(ModTags.COMPOST_ACTIVATORS)
            .add(GSBlocks.COMPOST_PILE.get());
    }
}

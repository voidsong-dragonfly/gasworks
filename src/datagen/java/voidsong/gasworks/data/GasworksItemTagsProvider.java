package voidsong.gasworks.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.CandelabraBlock;
import voidsong.gasworks.common.registry.GSBlocks;
import voidsong.gasworks.common.registry.GSItems;
import voidsong.gasworks.api.GSTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class GasworksItemTagsProvider extends ItemTagsProvider {
    public GasworksItemTagsProvider(PackOutput output, CompletableFuture<TagLookup<Block>>  blocks, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blocks, Gasworks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider lookupProvider) {
        /*
         * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
         */
        // Pyrolysis walls for use in recipes
        tag(GSTags.Items.PYROLYZING_WALLS)
            .addTag(ItemTags.DIRT).remove(net.minecraft.world.item.Items.MOSS_BLOCK, net.minecraft.world.item.Items.MUD, net.minecraft.world.item.Items.MUDDY_MANGROVE_ROOTS);
        // Log stacks for fuel
        tag(ItemTags.LOGS_THAT_BURN)
            .add(GSItems.OAK_LOG_PILE.get(), GSItems.SPRUCE_LOG_PILE.get(), GSItems.BIRCH_LOG_PILE.get(),
                GSItems.JUNGLE_LOG_PILE.get(), GSItems.ACACIA_LOG_PILE.get(), GSItems.DARK_OAK_LOG_PILE.get(),
                GSItems.CHERRY_LOG_PILE.get(), GSItems.MANGROVE_LOG_PILE.get(), GSItems.BAMBOO_LOG_PILE.get());
        tag(GSTags.Items.LOG_PILES)
            .add(GSItems.OAK_LOG_PILE.get(), GSItems.SPRUCE_LOG_PILE.get(), GSItems.BIRCH_LOG_PILE.get(),
                GSItems.JUNGLE_LOG_PILE.get(), GSItems.ACACIA_LOG_PILE.get(), GSItems.DARK_OAK_LOG_PILE.get(),
                GSItems.CHERRY_LOG_PILE.get(), GSItems.MANGROVE_LOG_PILE.get(), GSItems.BAMBOO_LOG_PILE.get());
        // Coal-like piles for fuel
        tag(GSTags.Items.COALLIKE_PILES)
            .add(GSItems.CHARCOAL_PILE.get(), GSItems.COAL_PILE.get());
        // Ash products
        tag(GSTags.Items.DUSTS)
            .add(GSItems.ASH.asItem());
        tag(GSTags.Items.ASH_DUST)
            .add(GSItems.ASH.asItem());
        tag(GSTags.Items.ASH)
            .add(GSItems.ASH.asItem());
        // Coke products
        tag(GSTags.Items.COAL_COKE)
            .add(GSItems.COKE.asItem());
        // Special-case-encompassing compost-accelerating blocks
        tag(GSTags.Items.COMPOST_ACCELERATORS_DISPLAY)
            // Standard blocks
            .add(net.minecraft.world.item.Items.RED_MUSHROOM, net.minecraft.world.item.Items.BROWN_MUSHROOM, net.minecraft.world.item.Items.GLOW_LICHEN)
            .add(net.minecraft.world.item.Items.MOSS_BLOCK, net.minecraft.world.item.Items.MOSS_CARPET)
            .add(net.minecraft.world.item.Items.MYCELIUM, net.minecraft.world.item.Items.RED_MUSHROOM_BLOCK, net.minecraft.world.item.Items.BROWN_MUSHROOM_BLOCK, net.minecraft.world.item.Items.MUSHROOM_STEM)
            // Nether blocks
            .add(net.minecraft.world.item.Items.NETHER_WART)
            .add(net.minecraft.world.item.Items.WARPED_FUNGUS, net.minecraft.world.item.Items.CRIMSON_FUNGUS, net.minecraft.world.item.Items.WARPED_ROOTS, net.minecraft.world.item.Items.CRIMSON_ROOTS, net.minecraft.world.item.Items.NETHER_SPROUTS)
            .add(net.minecraft.world.item.Items.CRIMSON_NYLIUM, net.minecraft.world.item.Items.WARPED_NYLIUM)
            // Special case: Infested blocks
            .add(net.minecraft.world.item.Items.INFESTED_COBBLESTONE, net.minecraft.world.item.Items.INFESTED_DEEPSLATE, net.minecraft.world.item.Items.INFESTED_STONE, net.minecraft.world.item.Items.INFESTED_STONE_BRICKS, net.minecraft.world.item.Items.INFESTED_CRACKED_STONE_BRICKS, net.minecraft.world.item.Items.INFESTED_CHISELED_STONE_BRICKS, net.minecraft.world.item.Items.INFESTED_MOSSY_STONE_BRICKS)
            // Special case: Compost
            .add(GSItems.COMPOST_PILE.get())
            // Farmer's Delight compat
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "organic_compost"))
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "rich_soil"))
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "rich_soil_farmland"))
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "brown_mushroom_colony"))
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "red_mushroom_colony"));
        /*
         * Building blocks, including various 'functional' blocks
         */
        // Framed glass
        tag(Tags.Items.GLASS_BLOCKS)
            .add(GSItems.FRAMED_GLASS.get());
        tag(Tags.Items.GLASS_BLOCKS_COLORLESS)
            .add(GSItems.FRAMED_GLASS.get());
        tag(Tags.Items.GLASS_PANES)
            .add(GSItems.FRAMED_GLASS_PANE.get());
        tag(Tags.Items.GLASS_PANES_COLORLESS)
            .add(GSItems.FRAMED_GLASS_PANE.get());
        for (DeferredBlock<StainedGlassBlock> block : GSBlocks.STAINED_FRAMED_GLASS) {
            StainedGlassBlock glass = block.get();
            tag(Tags.Items.GLASS_BLOCKS).add(glass.asItem());
            tag(Tags.Items.DYED).add(glass.asItem());
            tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyed/" + glass.getColor())))
                .add(glass.asItem());
        }
        for (DeferredBlock<StainedGlassPaneBlock> block : GSBlocks.STAINED_FRAMED_GLASS_PANES) {
            StainedGlassPaneBlock glass = block.get();
            tag(Tags.Items.GLASS_PANES).add(glass.asItem());
            tag(Tags.Items.DYED).add(glass.asItem());
            tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyed/" + glass.getColor())))
                .add(glass.asItem());
        }
        // Candelabras
        tag(GSTags.Items.CANDELABRAS)
            .add(GSItems.CANDELABRA.get());
        for (Pair<DyeColor, DeferredBlock<CandelabraBlock>> pair : GSBlocks.CANDELABRAS) {
            CandelabraBlock candelabra = pair.getSecond().get();
            tag(GSTags.Items.CANDELABRAS).add(candelabra.asItem());
            tag(Tags.Items.DYED).add(candelabra.asItem());
            tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyed/" + pair.getFirst())))
                .add(candelabra.asItem());
        }
        // Tools
        tag(ItemTags.BOOKSHELF_BOOKS).add(GSItems.TRADESWOMANS_JOURNAL.get());
        tag(Tags.Items.TOOLS_IGNITER).add(GSItems.FIRE_DRILL.get());
        tag(Tags.Items.TOOLS).add(GSItems.FIRE_DRILL.get(), GSItems.BAROMETER.get());
    }
}

package voidsong.gasworks.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.Gasworks;
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
        tag(GSTags.ItemTags.PYROLIZING_WALLS)
            .addTag(ItemTags.DIRT).remove(Items.MOSS_BLOCK, Items.MUD, Items.MUDDY_MANGROVE_ROOTS);
        // Log stacks for fuel
        tag(ItemTags.LOGS_THAT_BURN)
            .add(GSItems.OAK_LOG_PILE.get(), GSItems.SPRUCE_LOG_PILE.get(), GSItems.BIRCH_LOG_PILE.get(),
                GSItems.JUNGLE_LOG_PILE.get(), GSItems.ACACIA_LOG_PILE.get(), GSItems.DARK_OAK_LOG_PILE.get(),
                GSItems.CHERRY_LOG_PILE.get(), GSItems.MANGROVE_LOG_PILE.get(), GSItems.BAMBOO_LOG_PILE.get());
        tag(GSTags.ItemTags.LOG_PILES)
            .add(GSItems.OAK_LOG_PILE.get(), GSItems.SPRUCE_LOG_PILE.get(), GSItems.BIRCH_LOG_PILE.get(),
                GSItems.JUNGLE_LOG_PILE.get(), GSItems.ACACIA_LOG_PILE.get(), GSItems.DARK_OAK_LOG_PILE.get(),
                GSItems.CHERRY_LOG_PILE.get(), GSItems.MANGROVE_LOG_PILE.get(), GSItems.BAMBOO_LOG_PILE.get());
        // Coal-like piles for fuel
        tag(GSTags.ItemTags.COALLIKE_PILES)
            .add(GSItems.CHARCOAL_PILE.get(), GSItems.COAL_PILE.get());
        // Ash products
        tag(GSTags.ItemTags.DUSTS)
            .add(GSItems.ASH.asItem());
        tag(GSTags.ItemTags.ASH_DUST)
            .add(GSItems.ASH.asItem());
        tag(GSTags.ItemTags.ASH)
            .add(GSItems.ASH.asItem());
        // Coke products
        tag(GSTags.ItemTags.COAL_COKE)
            .add(GSItems.COKE.asItem());
        // Special-case-encompassing compost-accelerating blocks
        tag(GSTags.ItemTags.COMPOST_ACCELERATORS_DISPLAY)
            // Standard blocks
            .add(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM, Items.GLOW_LICHEN)
            .add(Items.MOSS_BLOCK, Items.MOSS_CARPET)
            .add(Items.MYCELIUM, Items.RED_MUSHROOM_BLOCK, Items.BROWN_MUSHROOM_BLOCK, Items.MUSHROOM_STEM)
            // Nether blocks
            .add(Items.NETHER_WART)
            .add(Items.WARPED_FUNGUS, Items.CRIMSON_FUNGUS, Items.WARPED_ROOTS, Items.CRIMSON_ROOTS, Items.NETHER_SPROUTS)
            .add(Items.CRIMSON_NYLIUM, Items.WARPED_NYLIUM)
            // Special case: Infested blocks
            .add(Items.INFESTED_COBBLESTONE, Items.INFESTED_DEEPSLATE, Items.INFESTED_STONE, Items.INFESTED_STONE_BRICKS, Items.INFESTED_CRACKED_STONE_BRICKS, Items.INFESTED_CHISELED_STONE_BRICKS, Items.INFESTED_MOSSY_STONE_BRICKS)
            // Special case: Compost
            .add(GSItems.COMPOST_PILE.get())
            // Farmer's Delight compat
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "organic_compost"))
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "rich_soil"))
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "rich_soil_farmland"))
            .addOptional(ResourceLocation.fromNamespaceAndPath( "farmersdelight", "brown_mushroom_colony"))
            .addOptional(ResourceLocation.fromNamespaceAndPath("farmersdelight", "red_mushroom_colony"));
        /*
         * Building blocks, including various 'functional' blocks
         */
        tag(Tags.Items.BRICKS)
            .add(GSItems.FIREBRICK.get());
        tag(GSTags.ItemTags.REFRACTORY_BRICK)
            .add(GSItems.FIREBRICK.get());
        tag(GSTags.ItemTags.FIREBRICK)
            .add(GSItems.FIREBRICK.get());
    }
}

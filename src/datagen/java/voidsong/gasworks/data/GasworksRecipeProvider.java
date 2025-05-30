package voidsong.gasworks.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.registry.GSItems;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class GasworksRecipeProvider extends RecipeProvider {
    public GasworksRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput output) {
        /*
         * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
         */
        // Log stacks for fuel
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.OAK_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_OAK_LOG)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.OAK_LOG_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.SPRUCE_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_SPRUCE_LOG)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.SPRUCE_LOG_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.BIRCH_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_BIRCH_LOG)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.BIRCH_LOG_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.JUNGLE_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_JUNGLE_LOG)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.JUNGLE_LOG_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.ACACIA_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_ACACIA_LOG)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.ACACIA_LOG_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.DARK_OAK_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_DARK_OAK_LOG)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.DARK_OAK_LOG_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.CHERRY_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_CHERRY_LOG)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.CHERRY_LOG_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.MANGROVE_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_MANGROVE_LOG)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.MANGROVE_LOG_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.BAMBOO_LOG_PILE, 4)
            .pattern(" l ")
            .pattern("l l")
            .pattern(" l ")
            .define('l', Items.STRIPPED_BAMBOO_BLOCK)
            .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
            .save(output, rl(GSItems.BAMBOO_LOG_PILE, "crafting"));
        // Coal stacks for fuel
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.COAL_PILE)
            .pattern("ccc")
            .pattern("c c")
            .pattern("ccc")
            .define('c', Items.COAL)
            .unlockedBy("has_coal", has(Items.COAL))
            .save(output, rl(GSItems.COAL_PILE, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.CHARCOAL_PILE)
            .pattern("ccc")
            .pattern("c c")
            .pattern("ccc")
            .define('c', Items.CHARCOAL)
            .unlockedBy("has_charcoal", has(Items.CHARCOAL))
            .save(output, rl(GSItems.CHARCOAL_PILE, "crafting"));
        // Brick piles for firing
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.UNFIRED_BRICK_CLAMP, 2)
            .pattern("ccc")
            .pattern("c c")
            .pattern("ccc")
            .define('c', Items.CLAY_BALL)
            .unlockedBy("has_clay", has(Items.CLAY_BALL))
            .save(output, rl(GSItems.UNFIRED_BRICK_CLAMP, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FIRED_BRICK_CLAMP, 2)
            .pattern("bbb")
            .pattern("b b")
            .pattern("bbb")
            .define('b', Items.BRICK)
            .unlockedBy("has_brick", has(Items.BRICK))
            .save(output, rl(GSItems.FIRED_BRICK_CLAMP, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.UNFIRED_FIREBRICK_CLAMP, 2)
            .pattern("ccc")
            .pattern("c c")
            .pattern("ccc")
            .define('c', GSItems.FIRECLAY_BALL)
            .unlockedBy("has_fireclay", has(GSItems.FIRECLAY_BALL))
            .save(output, rl(GSItems.UNFIRED_FIREBRICK_CLAMP, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FIRED_FIREBRICK_CLAMP, 2)
            .pattern("bbb")
            .pattern("b b")
            .pattern("bbb")
            .define('b', GSItems.FIREBRICK)
            .unlockedBy("has_fireclay", has(GSItems.FIREBRICK))
            .save(output, rl(GSItems.FIRED_FIREBRICK_CLAMP, "crafting"));
        /*
         * Building blocks, including various 'functional' blocks
         */
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FIRECLAY)
            .pattern("cc")
            .pattern("cc")
            .define('c', GSItems.FIRECLAY_BALL)
            .unlockedBy("has_fireclay", has(GSItems.FIRECLAY_BALL))
            .save(output, rl(GSItems.FIRECLAY, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FIREBRICKS)
            .pattern("bb")
            .pattern("bb")
            .define('b', GSItems.FIREBRICK)
            .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
            .save(output, rl(GSItems.FIREBRICKS, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FIREBRICK_STAIRS, 4)
            .pattern("b  ")
            .pattern("bb ")
            .pattern("bbb")
            .define('b', GSItems.FIREBRICKS)
            .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
            .save(output, rl(GSItems.FIREBRICK_STAIRS, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FIREBRICK_SLAB, 6)
            .pattern("bbb")
            .define('b', GSItems.FIREBRICKS)
            .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
            .save(output, rl(GSItems.FIREBRICK_SLAB, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FIREBRICK_WALL, 6)
            .pattern("bbb")
            .pattern("bbb")
            .define('b', GSItems.FIREBRICKS)
            .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
            .save(output, rl(GSItems.FIREBRICK_WALL, "crafting"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(GSItems.FIREBRICKS), RecipeCategory.BUILDING_BLOCKS, GSItems.FIREBRICK_STAIRS)
            .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
            .save(output, rl(GSItems.FIREBRICK_STAIRS, "stonecutting"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(GSItems.FIREBRICKS), RecipeCategory.BUILDING_BLOCKS, GSItems.FIREBRICK_SLAB, 2)
            .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
            .save(output, rl(GSItems.FIREBRICK_SLAB, "stonecutting"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(GSItems.FIREBRICKS), RecipeCategory.BUILDING_BLOCKS, GSItems.FIREBRICK_WALL)
            .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
            .save(output, rl(GSItems.FIREBRICK_WALL, "stonecutting"));
        /*
         * Tool items & other useful items
         */
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, GSItems.TRADESWOMANS_JOURNAL)
            .requires(Items.BOOK)
            .requires(Tags.Items.FEATHERS)
            .requires(Items.INK_SAC)
            .requires(Tags.Items.NUGGETS_GOLD)
            .unlockedBy("has_book", has(Items.BOOK))
            .save(output, rl(GSItems.TRADESWOMANS_JOURNAL, "crafting"));
    }


    /*
     * Library functions and other functions not directly related to models; none of these should be accessed outside this class
     */

    protected ResourceLocation rl(ItemLike src, String path) {
        return ResourceLocation.fromNamespaceAndPath(Gasworks.MOD_ID, path+"/"+BuiltInRegistries.ITEM.getKey(src.asItem()).getPath());
    }

    protected ShapedRecipeBuilder shapedMisc(ItemLike output) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output);
    }

    protected ShapedRecipeBuilder shapedMisc(ItemLike output, int count) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, count);
    }

    protected ShapelessRecipeBuilder shapelessMisc(ItemLike output) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output);
    }

    protected ShapelessRecipeBuilder shapelessMisc(ItemLike output, int count) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, count);
    }
}

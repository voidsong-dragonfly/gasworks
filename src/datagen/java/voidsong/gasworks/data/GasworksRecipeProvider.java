package voidsong.gasworks.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.recipe.NonMetalRealismCondition;
import voidsong.gasworks.common.registry.GSBlocks;
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
        // Fireclay blocks of various types
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
        // Temporary recipe for firebrick;
        // TODO: Implement ore generation so firebrick doesn't need a recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, GSItems.FIRECLAY_BALL, 8)
            .requires(Tags.Items.SANDS)
            .requires(Items.CLAY_BALL, 4)
            .unlockedBy("has_clay", has(Items.CLAY_BALL))
            .save(output, rl(GSItems.FIRECLAY_BALL, "crafting"));
        for(DeferredItem<BlockItem> item : GSItems.FIREBRICK_SILLS) {
            // This is a hack to make generating the recipes easier; since stone doesn't have a polished variant we specialcase it until I get around to adding it
            // TODO: Add polished stone
            Item polished = BuiltInRegistries.ITEM.get(ResourceLocation.parse("polished_" + BuiltInRegistries.ITEM.getKey(item.get()).getPath().replace("firebrick_sill_", "").replace("polished_", "")));
            polished = polished.equals(Items.AIR) ? Items.STONE_BRICKS : polished;
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 4)
                .pattern("ss")
                .pattern("bb")
                .define('s', polished)
                .define('b', GSItems.FIREBRICKS)
                .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
                .save(output, rl(item, "crafting"));
        }
        for(DeferredItem<BlockItem> item : GSItems.FIREBRICK_QUOINS) {
            // This is a hack to make generating the recipes easier; since andesite, etc don't have a bricks variant we specialcase it until I get around to adding it
            // TODO: Add andesite, diorite, etc bricks
            Item bricks = BuiltInRegistries.ITEM.get(ResourceLocation.parse(BuiltInRegistries.ITEM.getKey(item.get()).getPath().replace("firebrick_quoin_", "") + "_bricks"));
            bricks = bricks.equals(Items.AIR) ? BuiltInRegistries.ITEM.get(ResourceLocation.parse("polished_" + BuiltInRegistries.ITEM.getKey(item.get()).getPath().replace("firebrick_quoin_", ""))) : bricks;
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 4)
                .pattern("sb")
                .pattern("bs")
                .define('s', bricks)
                .define('b', GSItems.FIREBRICKS)
                .unlockedBy("has_firebrick", has(GSItems.FIREBRICK))
                .save(output, rl(item, "crafting"));
        }
        // Normal brick quoins & specialty blocks
        for(DeferredItem<BlockItem> item : GSItems.BRICK_SILLS) {
            // This is a hack to make generating the recipes easier; since stone doesn't have a polished variant we specialcase it until I get around to adding it
            // TODO: Add polished stone
            Item polished = BuiltInRegistries.ITEM.get(ResourceLocation.parse("polished_" + BuiltInRegistries.ITEM.getKey(item.get()).getPath().replace("brick_sill_", "").replace("polished_", "")));
            polished = polished.equals(Items.AIR) ? Items.STONE_BRICKS : polished;
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 4)
                .pattern("ss")
                .pattern("bb")
                .define('s', polished)
                .define('b', Items.BRICKS)
                .unlockedBy("has_brick", has(Items.BRICK))
                .save(output, rl(item, "crafting"));
        }
        for(DeferredItem<BlockItem> item : GSItems.BRICK_QUOINS) {
            // This is a hack to make generating the recipes easier; since andesite, etc don't have a bricks variant we specialcase it until I get around to adding it
            // TODO: Add andesite, diorite, etc bricks
            Item bricks = BuiltInRegistries.ITEM.get(ResourceLocation.parse(BuiltInRegistries.ITEM.getKey(item.get()).getPath().replace("brick_quoin_", "") + "_bricks"));
            bricks = bricks.equals(Items.AIR) ? BuiltInRegistries.ITEM.get(ResourceLocation.parse("polished_" + BuiltInRegistries.ITEM.getKey(item.get()).getPath().replace("brick_quoin_", ""))) : bricks;
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 4)
                .pattern("sb")
                .pattern("bs")
                .define('s', bricks)
                .define('b', Items.BRICKS)
                .unlockedBy("has_brick", has(Items.BRICK))
                .save(output, rl(item, "crafting"));
        }
        // Framed glass
        // TODO: Fix these recipes once I get more of the crafting infrastructure implemented: Should all be framing, not otherwise. One rod bundle per block, one per four panes
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FRAMED_GLASS, 5)
            .pattern("gig")
            .pattern("igi")
            .pattern("gig")
            .define('i', Tags.Items.INGOTS_IRON)
            .define('g', Tags.Items.GLASS_BLOCKS_CHEAP)
            .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS_CHEAP))
            .save(output, rl(GSItems.FRAMED_GLASS, "crafting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GSItems.FRAMED_GLASS_PANE, 16)
            .pattern("ggg")
            .pattern("ggg")
            .define('g', GSItems.FRAMED_GLASS)
            .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS_CHEAP))
            .save(output.withConditions(new NotCondition(new NonMetalRealismCondition())), rl(GSItems.FRAMED_GLASS_PANE, "crafting"));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(GSItems.FRAMED_GLASS), RecipeCategory.BUILDING_BLOCKS, GSItems.FRAMED_GLASS_PANE, 4)
            .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS_CHEAP))
            .save(output, rl(GSItems.FRAMED_GLASS_PANE, "stonecutting"));
        for(DeferredBlock<StainedGlassBlock> block : GSBlocks.STAINED_FRAMED_GLASS) {
            Item item = block.get().asItem();
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 8)
                .pattern("ggg")
                .pattern("gdg")
                .pattern("ggg")
                .define('g', GSItems.FRAMED_GLASS)
                .define('d', block.get().getColor().getTag())
                .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS_CHEAP))
                .save(output, rl(item, "crafting"));
        }
        for(DeferredBlock<StainedGlassPaneBlock> block : GSBlocks.STAINED_FRAMED_GLASS_PANES) {
            Item item = block.get().asItem();
            Item full = GSBlocks.STAINED_FRAMED_GLASS_PANES.stream().filter(b -> b.get().getColor().equals(block.get().getColor())).map(DeferredBlock::asItem).toList().getFirst();
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 16)
                .pattern("ggg")
                .pattern("ggg")
                .define('g', full)
                .unlockedBy("has_glass", has(full))
                .save(output.withConditions(new NotCondition(new NonMetalRealismCondition())), rl(item, "crafting"));
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item, 8)
                .pattern("ggg")
                .pattern("gdg")
                .pattern("ggg")
                .define('g', GSItems.FRAMED_GLASS_PANE)
                .define('d', block.get().getColor().getTag())
                .unlockedBy("has_glass", has(GSItems.FRAMED_GLASS_PANE))
                .save(output, rl(item, "crafting", "_from_glass_pane"));
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(full), RecipeCategory.BUILDING_BLOCKS, item, 4)
                .unlockedBy("has_glass", has(full))
                .save(output, rl(item, "stonecutting"));
        }
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

    protected ResourceLocation rl(ItemLike src, String path, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(Gasworks.MOD_ID, path+"/"+BuiltInRegistries.ITEM.getKey(src.asItem()).getPath()+suffix);
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

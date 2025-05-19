package voidsong.gasworks.api.recipe.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import voidsong.gasworks.api.recipe.GSRecipeTypes;
import voidsong.gasworks.common.block.BurnableFuelBlock;
import voidsong.gasworks.common.registry.GSItems;
import voidsong.gasworks.api.GSTags;

import javax.annotation.Nonnull;

public class PyrolysisRecipe implements Recipe<PyrolysisRecipe.BlockRecipeInput> {
    protected final BurnableFuelBlock ingredient;
    protected final ItemStack result;
    protected final int ash;
    protected final float experience;

    public PyrolysisRecipe(BurnableFuelBlock ingredient, ItemStack result, int ash, float experience) {
        this.ingredient = ingredient;
        this.ash = ash;
        this.result = result;
        this.experience = experience;
    }

    public boolean matches(BlockRecipeInput input, @Nonnull Level level) {
        return this.ingredient.equals(input.block);
    }

    @Nonnull
    public ItemStack assemble(@Nonnull BlockRecipeInput input, @Nonnull HolderLookup.Provider registries) {
        return result.copy();
    }

    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Nonnull
    public ItemStack getResultItem(@Nonnull HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Nonnull
    public RecipeType<?> getType() {
        return GSRecipeTypes.PYROLYSIS.get();
    }

    public HolderSet.Named<Block> getValidWalls() {
        return BuiltInRegistries.BLOCK.getOrCreateTag(GSTags.BlockTags.PYROLIZING_WALLS);
    }

    public ItemStack getAsh() {
        return new ItemStack(GSItems.ASH.get(), ash);
    }

    public record BlockRecipeInput(Block block) implements RecipeInput {
        @Override
        @Nonnull
        public ItemStack getItem(int slot) {
            if (slot != 0) {
                throw new IllegalArgumentException("No item for index " + slot);
            } else {
                return new ItemStack(block);
            }
        }

        @Override
        public int size() {
            return 1;
        }
    }
}

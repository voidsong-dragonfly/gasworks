package voidsong.gasworks.api.recipe.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

/**
 * Minecraft makes some, charitably, extremely wacko decisions about recipes. Following the words of the venerable
 * Nick Fury, given that it's a stupid-ass [set of] decision[s], I've elected to ignore [them].
 * This class removes a lot of the boilerplate necessary to interact with the Vanilla system by returning empty values.
 * Recipes based on this class should cache their own recipes using {@link voidsong.gasworks.api.recipe.CachedRecipeList CachedRecipeList}
 * and bypass the Vanilla recipe lookup system that iterates over all recipes in the game.
 */
public abstract class SensibleRecipeBase implements Recipe<RecipeInput> {
    protected final ItemStack dummy;
    protected final RecipeType<?> type;
    protected final RecipeSerializer<?> serializer;

    protected SensibleRecipeBase(RecipeType<?> type, RecipeSerializer<?> serializer) {
        this.dummy = ItemStack.EMPTY;
        this.type = type;
        this.serializer = serializer;
    }

    protected SensibleRecipeBase(RecipeType<?> type, RecipeSerializer<?> serializer, ItemStack dummyOutput) {
        this.dummy = dummyOutput;
        this.type = type;
        this.serializer = serializer;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    @Nonnull
    public ItemStack getResultItem(@Nonnull HolderLookup.Provider registries) {
        return dummy;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(@Nonnull RecipeInput inv, @Nonnull Level worldIn) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull RecipeInput inv, @Nonnull HolderLookup.Provider access) {
        return this.dummy.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return this.type;
    }
}

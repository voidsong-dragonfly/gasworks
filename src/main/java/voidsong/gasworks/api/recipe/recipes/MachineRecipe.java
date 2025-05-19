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
 * This class reimplements a lot of the idiocy from scratch in a much better & more effective method.
 * It has a cached and machine-specific recipe list, as well as sensible outputs and no fucking around with RecipeInputs
 */
public abstract class MachineRecipe implements Recipe<RecipeInput> {
    protected final ItemStack dummy;
    protected final RecipeType<?> type;

    protected MachineRecipe(RecipeType<?> type, RecipeSerializer<?> serializer) {
        this.dummy = ItemStack.EMPTY;
        this.type = type;
    }

    protected MachineRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, ItemStack dummyOutput) {
        this.dummy = dummyOutput;
        this.type = type;
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

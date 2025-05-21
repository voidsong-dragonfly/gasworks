package voidsong.gasworks.api.recipe.recipes;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import voidsong.gasworks.api.recipe.GSRecipeTypes;
import voidsong.gasworks.common.block.ClampBlock;

/**
 * WARNING: This class is a dummy class & will not affect the game at all!
 * This class is solely to hold objects necessary for registering JEI recipes for the given in-world brick firing recipes.
 * It also does NOT cache its recipes because they will not be used for lookups!
 */
public class ClampRecipe extends SensibleRecipeBase {
    // Recipe parameters
    protected final TagKey<Item> fuels;
    protected final int fuelAmount;
    protected final ClampBlock ingredient;
    protected final ItemStack result;
    protected final float experience;

    /**
     * WARNING: This class is a dummy class & will not affect the game at all!
     * This class is solely to hold objects necessary for registering JEI recipes for the given in-world brick firing recipes.
     * It also does NOT cache its recipes because they will not be used for lookups!
     */
    public ClampRecipe(TagKey<Item> fuels, int fuelAmount, ClampBlock ingredient, ItemStack result, float experience) {
        super(GSRecipeTypes.CLAMP_FIRING.get(), null, result);
        this.fuels = fuels;
        this.fuelAmount = fuelAmount;
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
    }

    /**
     * WARNING: This class is a dummy class & will not affect the game at all!
     * This class is solely to hold objects necessary for registering JEI recipes for the given in-world brick firing recipes.
     * It also does NOT cache its recipes because they will not be used for lookups!
     */
    public ClampRecipe(TagKey<Item> fuels, int fuelAmount, ClampBlock ingredient, Item result, Item ash, float experience) {
        super(GSRecipeTypes.CLAMP_FIRING.get(), null, new ItemStack(result));
        this.fuels = fuels;
        this.fuelAmount = fuelAmount;
        this.ingredient = ingredient;
        this.result = new ItemStack(result);
        this.experience = experience;
    }

    public TagKey<Item> getFuels() {
        return fuels;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    public ItemStack getIngredient() {
        return new ItemStack(ingredient);
    }

    public ItemStack getResult() {
        return result;
    }

    public float getExperience() {
        return experience;
    }
}

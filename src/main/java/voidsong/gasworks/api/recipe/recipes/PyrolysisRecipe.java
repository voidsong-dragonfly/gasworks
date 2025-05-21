package voidsong.gasworks.api.recipe.recipes;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import voidsong.gasworks.api.recipe.GSRecipeTypes;
import voidsong.gasworks.common.block.BurnableFuelBlock;

/**
 * WARNING: This class is a dummy class & will not affect the game at all!
 * This class is solely to hold objects necessary for registering JEI recipes for the given in-world pyrolysis recipes.
 * It also does NOT cache its recipes because they will not be used for lookups!
 */
public class PyrolysisRecipe extends SensibleRecipeBase {
    // Recipe parameters
    protected final TagKey<Item> walls;
    protected final BurnableFuelBlock ingredient;
    protected final ItemStack result;
    protected final ItemStack ash;
    protected final float experience;

    /**
     * WARNING: This class is a dummy class & will not affect the game at all!
     * This class is solely to hold objects necessary for registering JEI recipes for the given in-world pyrolysis recipes.
     * It also does NOT cache its recipes because they will not be used for lookups!
     */
    public PyrolysisRecipe(TagKey<Item> walls, BurnableFuelBlock ingredient, ItemStack result, ItemStack ash, float experience) {
        super(GSRecipeTypes.PYROLYSIS.get(), null, result);
        this.walls = walls;
        this.ingredient = ingredient;
        this.ash = ash;
        this.result = result;
        this.experience = experience;
    }

    /**
     * WARNING: This class is a dummy class & will not affect the game at all!
     * This class is solely to hold objects necessary for registering JEI recipes for the given in-world pyrolysis recipes.
     * It also does NOT cache its recipes because they will not be used for lookups!
     */
    public PyrolysisRecipe(TagKey<Item> walls, BurnableFuelBlock ingredient, Item result, Item ash, float experience) {
        super(GSRecipeTypes.PYROLYSIS.get(), null, new ItemStack(result));
        this.walls = walls;
        this.ingredient = ingredient;
        this.ash = new ItemStack(ash);
        this.result = new ItemStack(result);
        this.experience = experience;
    }

    public TagKey<Item> getWalls() {
        return walls;
    }

    public ItemStack getIngredient() {
        return new ItemStack(ingredient);
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack getAsh() {
        return ash;
    }

    public float getExperience() {
        return experience;
    }
}

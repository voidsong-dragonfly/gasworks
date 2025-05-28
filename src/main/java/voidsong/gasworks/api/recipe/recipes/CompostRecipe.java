package voidsong.gasworks.api.recipe.recipes;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import voidsong.gasworks.api.recipe.GSRecipeTypes;
import voidsong.gasworks.common.block.CompostBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * WARNING: This class is a dummy class & will not affect the game at all!
 * This class is solely to hold objects necessary for registering JEI recipes for the given in-world composting recipes.
 * It also does NOT cache its recipes because they will not be used for lookups!
 */
public class CompostRecipe extends SensibleRecipeBase {
    // Recipe parameters
    protected final TagKey<Item> accelerators;
    protected final CompostBlock ingredient;
    protected final List<ItemStack> result;
    protected final float experience;

    /**
     * WARNING: This class is a dummy class & will not affect the game at all!
     * This class is solely to hold objects necessary for registering JEI recipes for the given in-world composting recipes.
     * It also does NOT cache its recipes because they will not be used for lookups!
     */
    public CompostRecipe(TagKey<Item> accelerators, CompostBlock ingredient, Item result, int countMin, int countMax, float experience) {
        super(GSRecipeTypes.COMPOSTING.get(), null, new ItemStack(result));
        this.accelerators = accelerators;
        this.ingredient = ingredient;
        List<ItemStack> list = new ArrayList<>();
        for(int i = countMin; i <= countMax; i++)
            list.add(new ItemStack(result, i));
        this.result = list;
        this.experience = experience;
    }

    public TagKey<Item> getAccelerators() {
        return accelerators;
    }

    public ItemStack getIngredient() {
        return new ItemStack(ingredient);
    }

    public List<ItemStack> getResult() {
        return result;
    }

    public float getExperience() {
        return experience;
    }
}

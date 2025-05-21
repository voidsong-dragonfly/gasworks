package voidsong.gasworks.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.RecipeHolder;
import voidsong.gasworks.api.recipe.GSRecipeTypes;
import voidsong.gasworks.api.recipe.recipes.ClampRecipe;
import voidsong.gasworks.api.recipe.recipes.PyrolysisRecipe;

public class JEIRecipeTypes {

    public static final RecipeType<RecipeHolder<PyrolysisRecipe>> PYROLYSIS = RecipeType.createFromVanilla(GSRecipeTypes.PYROLYSIS.get());
    public static final RecipeType<RecipeHolder<ClampRecipe>> CLAMP_FIRING = RecipeType.createFromVanilla(GSRecipeTypes.CLAMP_FIRING.get());

}

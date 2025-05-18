package voidsong.gasworks.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.RecipeHolder;
import voidsong.gasworks.api.recipe.GSRecipeTypes;
import voidsong.gasworks.api.recipe.PyrolysisRecipe;

public class RecipeTypes {

    public static final RecipeType<RecipeHolder<PyrolysisRecipe>> PYROLYSIS = RecipeType.createFromVanilla(GSRecipeTypes.PYROLYSIS.get());

}

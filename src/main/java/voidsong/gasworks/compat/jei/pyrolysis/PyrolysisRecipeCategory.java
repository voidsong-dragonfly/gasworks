package voidsong.gasworks.compat.jei.pyrolysis;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import voidsong.gasworks.api.recipe.recipes.PyrolysisRecipe;
import voidsong.gasworks.common.registry.GSItems;
import voidsong.gasworks.compat.jei.JEIRecipeTypes;
import voidsong.gasworks.compat.jei.RecipeCategory;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class PyrolysisRecipeCategory extends RecipeCategory<PyrolysisRecipe> {
    public PyrolysisRecipeCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.PYROLYSIS, "jei.gasworks.pyrolysis");
        setIcon(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, GSItems.CHARCOAL_PILE.toStack()));
    }

    @Override
    public int getWidth() {
        return 120;
    }

    @Override
    public int getHeight() {
        return 38;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, RecipeHolder<PyrolysisRecipe> recipeHolder, @Nonnull IFocusGroup focuses) {
        PyrolysisRecipe recipe = recipeHolder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 17)
            .addItemStacks(Arrays.stream(Ingredient.of(recipe.getWalls()).getItems()).toList());

        builder.addSlot(RecipeIngredientRole.INPUT, 21, 17)
            .addItemStack(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 17)
            .addItemStack(recipe.getResult());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 17)
            .addItemStack(recipe.getAsh());
    }

    @Override
    public void draw(@Nonnull RecipeHolder<PyrolysisRecipe> recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics graphics, double mouseX, double mouseY) {
        /*
        this.flame.draw(graphics, 52, 0);
        this.flameAnimated.draw(graphics, 52, 0);
        this.arrow.draw(graphics, 50, 16);
        this.arrowAnimated.draw(graphics, 50, 16);
         */
    }
}

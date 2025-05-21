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
import voidsong.gasworks.compat.jei.JEIPlugin;
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
        return 142;
    }

    @Override
    public int getHeight() {
        return 22;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, RecipeHolder<PyrolysisRecipe> recipeHolder, @Nonnull IFocusGroup focuses) {
        PyrolysisRecipe recipe = recipeHolder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 3)
            .addItemStacks(Arrays.stream(Ingredient.of(recipe.getWalls()).getItems()).toList());

        builder.addSlot(RecipeIngredientRole.INPUT, 21, 3)
            .addItemStack(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 3)
            .addItemStack(recipe.getResult());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 3)
            .addItemStack(recipe.getAsh());
    }

    @Override
    public void draw(@Nonnull RecipeHolder<PyrolysisRecipe> recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics graphics, double mouseX, double mouseY) {
        // Draw the slots
        JEIPlugin.slotDrawable.draw(graphics, 0, 2);
        JEIPlugin.slotDrawable.draw(graphics, 20, 2);
        JEIPlugin.slotDrawable.draw(graphics, 104, 2);
        JEIPlugin.slotDrawable.draw(graphics, 124, 2);
        // Draw the moving parts
        /*
        this.flame.draw(graphics, 52, 0);
        this.flameAnimated.draw(graphics, 52, 0);
        this.arrow.draw(graphics, 50, 16);
        this.arrowAnimated.draw(graphics, 50, 16);
         */
    }
}

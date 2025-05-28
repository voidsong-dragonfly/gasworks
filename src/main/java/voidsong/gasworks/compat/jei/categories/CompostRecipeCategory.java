package voidsong.gasworks.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.api.recipe.recipes.CompostRecipe;
import voidsong.gasworks.common.registry.GSItems;
import voidsong.gasworks.compat.jei.JEIPlugin;
import voidsong.gasworks.compat.jei.JEIRecipeTypes;
import voidsong.gasworks.compat.jei.RecipeCategory;
import voidsong.gasworks.compat.jei.TranslatedSlotTooltipCallback;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class CompostRecipeCategory extends RecipeCategory<CompostRecipe> {
    private static final ResourceLocation guiElements = Gasworks.rl("textures/gui/in_world.png");
    private final IDrawableStatic arrow;
    private final IDrawableAnimated arrowAnimated;

    public CompostRecipeCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.COMPOSTING, "jei.gasworks.composting");
        setIcon(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, GSItems.COMPOST_PILE.toStack()));
        // Setup for the texture parts drawn in JEI
        arrow = helper.drawableBuilder(guiElements, 16, 0, 24, 16).setTextureSize(48, 48).build();
        arrowAnimated = helper.drawableBuilder(guiElements, 16, 16, 24, 16).setTextureSize(48, 48).buildAnimated(160, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public int getWidth() {
        return 82;
    }

    @Override
    public int getHeight() {
        return 22;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, RecipeHolder<CompostRecipe> recipeHolder, @Nonnull IFocusGroup focuses) {
        CompostRecipe recipe = recipeHolder.value();
        List<ItemStack> accelerators = Arrays.stream(Ingredient.of(recipe.getAccelerators()).getItems()).toList();
        builder.addSlot(RecipeIngredientRole.CATALYST, 1, 3)
            .addItemStacks(accelerators);

        // Set all the standard recipe category things, these are not what will be displayed
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 3)
            .addItemStack(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 65, 3)
            .addItemStacks(recipe.getResult())
            .addRichTooltipCallback(new TranslatedSlotTooltipCallback(Component.translatable("jei.gasworks.composting.chance")));
    }

    @Override
    public void draw(@Nonnull RecipeHolder<CompostRecipe> recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics graphics, double mouseX, double mouseY) {
        // Draw the slots
        JEIPlugin.slotDrawable.draw(graphics, 0, 2);
        JEIPlugin.slotDrawable.draw(graphics, 20, 2);
        JEIPlugin.slotDrawable.draw(graphics, 64, 2);
        // Draw the moving parts
        this.arrow.draw(graphics, 39, 3);
        this.arrowAnimated.draw(graphics, 39, 3);
    }
}
package voidsong.gasworks.compat.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.api.recipe.recipes.ClampRecipe;
import voidsong.gasworks.common.registry.GSItems;
import voidsong.gasworks.compat.jei.JEIPlugin;
import voidsong.gasworks.compat.jei.JEIRecipeTypes;
import voidsong.gasworks.compat.jei.RecipeCategory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ClampRecipeCategory extends RecipeCategory<ClampRecipe> {
    private static final ResourceLocation guiElements = Gasworks.rl("textures/gui/in_world.png");
    private final IDrawableStatic arrow;
    private final IDrawableAnimated arrowAnimated;
    private final IDrawableStatic flame;
    private final IDrawableAnimated flameAnimated;
    private static final int MAX_STACKS = 26;
    private int current = 1;
    private long tick = -1;
    int bricks = 0;

    public ClampRecipeCategory(IGuiHelper helper) {
        super(helper, JEIRecipeTypes.CLAMP_FIRING, "jei.gasworks.clamp_firing");
        setIcon(helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, GSItems.FIRED_BRICK_CLAMP.toStack()));
        // Setup for the texture parts drawn in JEI
        arrow = helper.drawableBuilder(guiElements, 16, 0, 24, 16).setTextureSize(48, 48).build();
        arrowAnimated = helper.drawableBuilder(guiElements, 16, 16, 24, 16).setTextureSize(48, 48).buildAnimated(160, IDrawableAnimated.StartDirection.LEFT, false);
        flame = helper.drawableBuilder(guiElements, 0, 0, 16, 16).setTextureSize(48, 48).build();
        flameAnimated = helper.drawableBuilder(guiElements, 0, 16, 16, 16).setTextureSize(48, 48).buildAnimated(160, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public int getWidth() {
        return 122;
    }

    @Override
    public int getHeight() {
        return 22;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, RecipeHolder<ClampRecipe> recipeHolder, @Nonnull IFocusGroup focuses) {
        ClampRecipe recipe = recipeHolder.value();
        List<ItemStack> fuels = Arrays.stream(Ingredient.of(recipe.getFuels()).getItems()).toList();
        fuels.iterator().forEachRemaining(s -> s.grow(recipe.getFuelAmount() - 1));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 3)
            .addItemStacks(fuels);

        // Set all the standard recipe category things, these are not what will be displayed
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 3)
            .addItemStack(recipe.getIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 3)
            .addItemStack(recipe.getResult());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 105, 3)
            .addItemStack(ItemStack.EMPTY);
    }

    @Override
    public void draw(@Nonnull RecipeHolder<ClampRecipe> recipe, @Nonnull IRecipeSlotsView recipeSlotsView, @Nonnull GuiGraphics graphics, double mouseX, double mouseY) {
        // Draw the slots
        JEIPlugin.slotDrawable.draw(graphics, 0, 2);
        JEIPlugin.slotDrawable.draw(graphics, 20, 2);
        JEIPlugin.slotDrawable.draw(graphics, 84, 2);
        JEIPlugin.slotDrawable.draw(graphics, 104, 2);
        // Draw the moving parts
        this.flame.draw(graphics, 39, 3);
        this.flameAnimated.draw(graphics, 39, 3);
        this.arrow.draw(graphics, 57, 3);
        this.arrowAnimated.draw(graphics, 57, 3);
    }

    @Override
    public void onDisplayedIngredientsUpdate(@Nonnull RecipeHolder<ClampRecipe> recipe, @Nonnull List<IRecipeSlotDrawable> recipeSlots, @Nonnull IFocusGroup focuses) {
        long time = 0;
        // Calculate whether we need to be switching stack amounts at this time
        if(Minecraft.getInstance().level != null)
            time = Minecraft.getInstance().level.getGameTime();
        if (time > tick) {
            current = current >= MAX_STACKS ? 1 : current + 1;
            tick = time;
            bricks = recipe.value().getResult().getCount()*current;
        }
        // Cycle through 1x to 26x recipes to display that you can cook more than one brick stack at once
        // Includes special handling to ensure that we do not display 104 bricks in a single stack
        for(IRecipeSlotDrawable slot : recipeSlots) {
            slot.clearDisplayOverrides();
            if (slot.getDisplayedIngredient().isEmpty())  {
                if (bricks > 64)
                    slot.createDisplayOverrides().addItemStack(recipe.value().getResult().copyWithCount(bricks-64));
            } else if (slot.getDisplayedIngredient().get().getItemStack().isPresent()){
                Item result = recipe.value().getResult().getItem();
                ItemStack stack = slot.getDisplayedIngredient().get().getItemStack().get();
                if (stack.getItem().equals(recipe.value().getIngredient().getItem()))
                    slot.createDisplayOverrides().addItemStack(recipe.value().getIngredient().copyWithCount(current));
                if (stack.getItem().equals(result)) {
                    slot.createDisplayOverrides().addItemStack(new ItemStack(result, Math.min(bricks, 64)));
                }
            }
        }
    }
}
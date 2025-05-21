package voidsong.gasworks.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class RecipeCategory<T extends Recipe<?>> implements IRecipeCategory<RecipeHolder<T>> {
    protected final IGuiHelper guiHelper;
    private final RecipeType<RecipeHolder<T>> type;
    public MutableComponent title;
    private IDrawable icon;

    public RecipeCategory(IGuiHelper guiHelper, RecipeType<RecipeHolder<T>> type, String localKey) {
        this.guiHelper = guiHelper;
        this.type = type;
        this.title = Component.translatable(localKey);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    protected void setIcon(ItemStack stack) {
        this.setIcon(this.guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, stack));
    }

    protected void setIcon(IDrawable icon) {
        this.icon = icon;
    }

    @Override
    @Nonnull
    public Component getTitle() {
        return this.title;
    }

    @Override
    @Nonnull
    public final RecipeType<RecipeHolder<T>> getRecipeType() {
        return type;
    }
}

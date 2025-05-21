package voidsong.gasworks.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.api.GSTags;
import voidsong.gasworks.api.recipe.recipes.PyrolysisRecipe;
import voidsong.gasworks.common.registry.GSBlocks;
import voidsong.gasworks.common.registry.GSItems;
import voidsong.gasworks.compat.jei.pyrolysis.PyrolysisRecipeCategory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation UID = Gasworks.rl("main");
    public static final ResourceLocation JEI_GUI = Gasworks.rl("textures/gui/jei_elements.png");
    public static IDrawableStatic slotDrawable;

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
            new PyrolysisRecipeCategory(helper)
        );

        slotDrawable = helper.getSlotDrawable();
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        registration.addRecipes(JEIRecipeTypes.PYROLYSIS, getPyrolysisJEIRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Pyrolysis special-casing
        registration.addRecipeCatalysts(JEIRecipeTypes.PYROLYSIS, GSItems.COAL_PILE);
        registration.addRecipeCatalysts(JEIRecipeTypes.PYROLYSIS,
            GSBlocks.OAK_LOG_PILE,
            GSBlocks.SPRUCE_LOG_PILE,
            GSBlocks.BIRCH_LOG_PILE,
            GSBlocks.JUNGLE_LOG_PILE,
            GSBlocks.ACACIA_LOG_PILE,
            GSBlocks.DARK_OAK_LOG_PILE,
            GSBlocks.CHERRY_LOG_PILE,
            GSBlocks.MANGROVE_LOG_PILE,
            GSBlocks.BAMBOO_LOG_PILE);
        // Other more traditional recipes types
    }

    private List<RecipeHolder<PyrolysisRecipe>> getPyrolysisJEIRecipes() {
        List<RecipeHolder<PyrolysisRecipe>> recipes = new ArrayList<>();
        // Log piles, but since we don't have easy access to tags, we do it manually
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_oak"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.OAK_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_spruce"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.SPRUCE_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_birch"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.BIRCH_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_jungle"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.JUNGLE_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_acacia"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.ACACIA_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_dark_oak"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.DARK_OAK_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_cherry"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.CHERRY_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_mangrove"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.MANGROVE_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        recipes.add(new RecipeHolder<>(Gasworks.rl("charcoal_bamboo"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.BAMBOO_LOG_PILE.get(), Items.CHARCOAL, GSItems.ASH.get(), 0.5f)));
        // Coal piles
        recipes.add(new RecipeHolder<>(Gasworks.rl("coke"), new PyrolysisRecipe(GSTags.ItemTags.PYROLIZING_WALLS, GSBlocks.COAL_PILE.get(), new ItemStack(GSItems.COKE.get(), 5), new ItemStack(GSItems.ASH.get()), 0.5f)));
        return recipes;
    }
}

package voidsong.gasworks.api.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.api.recipe.recipes.ClampRecipe;
import voidsong.gasworks.api.recipe.recipes.CompostRecipe;
import voidsong.gasworks.api.recipe.recipes.PyrolysisRecipe;

public class GSRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Gasworks.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<PyrolysisRecipe>> PYROLYSIS = RECIPE_TYPES.register("pyrolysis", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Gasworks.MOD_ID, "pyrolysis")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ClampRecipe>> CLAMP_FIRING = RECIPE_TYPES.register("clamp_firing", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Gasworks.MOD_ID, "clamp_firing")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<CompostRecipe>> COMPOSTING = RECIPE_TYPES.register("composting", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Gasworks.MOD_ID, "composting")));
}

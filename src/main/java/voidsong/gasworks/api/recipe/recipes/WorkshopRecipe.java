package voidsong.gasworks.api.recipe.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import voidsong.gasworks.api.recipe.CachedRecipeList;
import voidsong.gasworks.api.recipe.GSRecipeTypes;
import voidsong.gasworks.api.recipe.serializers.WorkshopRecipeSerializers.*;
import voidsong.gasworks.api.workshop.WorkshopAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class WorkshopRecipe extends SensibleRecipeBase {
    // Recipe parameters
    private final List<SizedIngredient> items;
    private final List<SizedFluidIngredient> fluids;
    private final List<WorkshopAction> actions;
    private final ItemStack result;
    private final float experience;

    public WorkshopRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, List<SizedIngredient> items, List<SizedFluidIngredient> fluids, List<WorkshopAction> actions, ItemStack result, float experience) {
        super(type, serializer, result);
        this.items = items;
        this.fluids = fluids;
        this.actions = actions;
        this.result = result;
        this.experience = experience;
    }

    public boolean matches(List<ItemStack> inventory, List<FluidStack> tanks, List<WorkshopAction> possibleActions) {
        // Check if we can perform all of the required actions
        for (WorkshopAction requiredAction : this.actions) {
            // For all of our possible actions, check if we fulfill this action.
            for (WorkshopAction possibleAction : possibleActions) {
                // If we can perform, break the loop
                if (possibleAction.canPerform(requiredAction))
                    break;
                // We cannot fulfill this recipe otherwise
                return false;
            }
        }
        // Check if we have all of the required item stacks
        // If we have a stack, update local copy of the inventory and continue. If we don't, continue until we have
        // tested all of the stacks and if we still fail, return false
        List<ItemStack> availableInventory = new ArrayList<>();
        for (ItemStack stack : inventory)
            if(!stack.isEmpty()) availableInventory.add(stack.copy());
        for (SizedIngredient ingredient : this.items) {
            Iterator<ItemStack> iterator = availableInventory.iterator();
            boolean fulfilled = false;
            while(iterator.hasNext()) {
                ItemStack stack = iterator.next();
                if (ingredient.test(stack)) {
                    stack.shrink(ingredient.count());
                    if (stack.isEmpty())
                        iterator.remove();
                    fulfilled = true;
                    break;
                }
            }
            if (!fulfilled)
                return false;
        }
        // Check if we have all of the required fluid stacks
        // We may not require fluids at all. if so, exit early
        if (fluids.isEmpty()) return true;
        // If we have a stack, update local copy of the tanks and continue. If we don't, continue until we have
        // tested all of the stacks and if we still fail, return false
        List<FluidStack> availableTanks = new ArrayList<>();
        for (FluidStack stack : tanks)
            if(!stack.isEmpty()) availableTanks.add(stack.copy());
        for (SizedFluidIngredient ingredient : this.fluids) {
            Iterator<FluidStack> iterator = availableTanks.iterator();
            boolean fulfilled = false;
            while(iterator.hasNext()) {
                FluidStack stack = iterator.next();
                if (ingredient.test(stack)) {
                    stack.shrink(ingredient.amount());
                    if (stack.isEmpty())
                        iterator.remove();
                    fulfilled = true;
                    break;
                }
            }
            if (!fulfilled)
                return false;
        }
        // Return true if we've passed all previous checks
        return true;
    }

    public List<WorkshopAction> getActions() {
        return actions;
    }

    public List<SizedIngredient> getItemIngredients() {
        return items;
    }

    public List<SizedFluidIngredient> getFluidIngredients() {
        return fluids;
    }

    public ItemStack getResult() {
        return result;
    }

    public float getExperience() {
        return experience;
    }

    public static class MachineShopRecipe extends WorkshopRecipe {
        public static final CachedRecipeList<MachineShopRecipe> RECIPES = new CachedRecipeList<>(GSRecipeTypes.MACHINE_SHOP);

        public MachineShopRecipe(List<SizedIngredient> items, List<SizedFluidIngredient> fluids, List<WorkshopAction> actions, ItemStack result, float experience) {
            super(GSRecipeTypes.MACHINE_SHOP.get(), new MachineShopRecipeSerializer(), items, fluids, actions, result, experience);
        }

        public static RecipeHolder<MachineShopRecipe> findRecipe(Level level, NonNullList<ItemStack> inventory, NonNullList<FluidStack> tanks, List<WorkshopAction> possibleActions) {
            if(inventory.isEmpty())
                return null;
            for(RecipeHolder<MachineShopRecipe> recipe : RECIPES.getRecipes(level))
                if(recipe.value().matches(inventory, tanks, possibleActions))
                    return recipe;
            return null;
        }
    }

    public static class ToolForgeRecipe extends WorkshopRecipe {
        public static final CachedRecipeList<ToolForgeRecipe> RECIPES = new CachedRecipeList<>(GSRecipeTypes.TOOL_FORGE);

        public ToolForgeRecipe(List<SizedIngredient> items, List<SizedFluidIngredient> fluids, List<WorkshopAction> actions, ItemStack result, float experience) {
            super(GSRecipeTypes.TOOL_FORGE.get(), new ToolForgeRecipeSerializer(), items, fluids, actions, result, experience);
        }

        public static RecipeHolder<ToolForgeRecipe> findRecipe(Level level, NonNullList<ItemStack> inventory, NonNullList<FluidStack> tanks, List<WorkshopAction> possibleActions) {
            if(inventory.isEmpty())
                return null;
            for(RecipeHolder<ToolForgeRecipe> recipe : RECIPES.getRecipes(level))
                if(recipe.value().matches(inventory, tanks, possibleActions))
                    return recipe;
            return null;
        }
    }
}

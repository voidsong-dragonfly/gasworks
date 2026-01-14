package voidsong.gasworks.api.recipe.serializers;

import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import voidsong.gasworks.api.recipe.recipes.WorkshopRecipe;
import voidsong.gasworks.api.utils.GSDualCodecs;
import voidsong.gasworks.api.workshop.WorkshopAction;

public class WorkshopRecipeSerializers {
    public static class MachineShopRecipeSerializer extends SensibleRecipeSerializer<WorkshopRecipe.MachineShopRecipe> {
        public static final DualMapCodec<RegistryFriendlyByteBuf, WorkshopRecipe.MachineShopRecipe> CODECS = DualCompositeMapCodecs.composite(
                GSDualCodecs.SIZED_INGREDIENT.listOf().fieldOf("inputs"), WorkshopRecipe::getItemIngredients,
                GSDualCodecs.SIZED_FLUID_INGREDIENT.listOf().fieldOf("fluids"), WorkshopRecipe::getFluidIngredients,
                WorkshopAction.CODECS.listOf().fieldOf("actions"), WorkshopRecipe::getActions,
                DualCodecs.ITEM_STACK.fieldOf("result"), WorkshopRecipe::getResult,
                DualCodecs.FLOAT.fieldOf("experience"), WorkshopRecipe::getExperience,
                WorkshopRecipe.MachineShopRecipe::new
        );

        @Override
        protected DualMapCodec<RegistryFriendlyByteBuf, WorkshopRecipe.MachineShopRecipe> codecs() {
            return CODECS;
        }
    }

    public static class ToolForgeRecipeSerializer extends SensibleRecipeSerializer<WorkshopRecipe.ToolForgeRecipe> {
        public static final DualMapCodec<RegistryFriendlyByteBuf, WorkshopRecipe.ToolForgeRecipe> CODECS = DualCompositeMapCodecs.composite(
                GSDualCodecs.SIZED_INGREDIENT.listOf().fieldOf("inputs"), WorkshopRecipe::getItemIngredients,
                GSDualCodecs.SIZED_FLUID_INGREDIENT.listOf().fieldOf("fluids"), WorkshopRecipe::getFluidIngredients,
                WorkshopAction.CODECS.listOf().fieldOf("actions"), WorkshopRecipe::getActions,
                DualCodecs.ITEM_STACK.fieldOf("result"), WorkshopRecipe::getResult,
                DualCodecs.FLOAT.fieldOf("experience"), WorkshopRecipe::getExperience,
                WorkshopRecipe.ToolForgeRecipe::new
        );

        @Override
        protected DualMapCodec<RegistryFriendlyByteBuf, WorkshopRecipe.ToolForgeRecipe> codecs() {
            return CODECS;
        }
    }
}

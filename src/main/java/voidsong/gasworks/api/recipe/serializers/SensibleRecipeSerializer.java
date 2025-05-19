package voidsong.gasworks.api.recipe.serializers;

import com.mojang.serialization.MapCodec;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import voidsong.gasworks.api.utils.GSDualCodecs;

import javax.annotation.Nonnull;

public abstract class SensibleRecipeSerializer<R extends Recipe<?>> implements RecipeSerializer<R> {

    public static DualMapCodec<RegistryFriendlyByteBuf, ItemStack> optionalItemOutput(String name) {
        return DualCodecs.ITEM_STACK.optionalFieldOf(name, ItemStack.EMPTY);
    }

    public static DualMapCodec<RegistryFriendlyByteBuf, FluidStack> optionalFluidOutput(String name) {
        return GSDualCodecs.FLUID_STACK.optionalFieldOf(name, FluidStack.EMPTY);
    }

    protected abstract DualMapCodec<RegistryFriendlyByteBuf, R> codecs();

    @Override
    @Nonnull
    public final MapCodec<R> codec() {
        return codecs().mapCodec();
    }

    @Override
    @Nonnull
    public final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
        return codecs().streamCodec();
    }
}

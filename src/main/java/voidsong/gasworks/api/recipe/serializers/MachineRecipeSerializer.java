package voidsong.gasworks.api.recipe.serializers;

import com.mojang.serialization.MapCodec;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public abstract class MachineRecipeSerializer<R extends Recipe<?>> implements RecipeSerializer<R> {

    public static DualMapCodec<RegistryFriendlyByteBuf, ItemStack> optionalItemOutput(String name) {
        return GSDualCodecs.ITEMSTACK.optionalFieldOf(name, ItemStack.EMPTY);
    }

    public static DualMapCodec<RegistryFriendlyByteBuf, FluidStack> optionalFluidOutput(String name) {
        return GSDualCodecs.FLUIDSTACK.optionalFieldOf(name, FluidStack.EMPTY);
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

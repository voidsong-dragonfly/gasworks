package voidsong.gasworks.api.utils;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import malte0811.dualcodecs.DualCodec;
import malte0811.dualcodecs.DualCompositeCodecs;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GSDualCodecs {
    public static final DualCodec<ByteBuf, Integer> POSITIVE_INT = new DualCodec<>(ExtraCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT);
    public static final DualCodec<ByteBuf, Integer> NON_NEGATIVE_INT = new DualCodec<>(ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
    public static final DualCodec<RegistryFriendlyByteBuf, FluidStack> FLUID_STACK = new DualCodec<>(
        FluidStack.OPTIONAL_CODEC, FluidStack.OPTIONAL_STREAM_CODEC
    );

    public static final DualCodec<RegistryFriendlyByteBuf, SizedFluidIngredient> SIZED_FLUID_INGREDIENT = new DualCodec<>(
        SizedFluidIngredient.FLAT_CODEC,
        SizedFluidIngredient.STREAM_CODEC
    );

    public static <E extends Enum<E>> DualCodec<ByteBuf, E> forEnum(E[] values) {
        return new DualCodec<>(enumCodec(values), enumStreamCodec(values));
    }

    public static <B extends ByteBuf, K, V> DualCodec<B, Map<K, V>> forMap(DualCodec<? super B, K> keyCodec, DualCodec<? super B, V> valueCodec) {
        DualCodec<B, Map.Entry<K, V>> entryCodec = DualCompositeCodecs.composite(
            keyCodec.fieldOf("key"), Map.Entry::getKey,
            valueCodec.fieldOf("value"), Map.Entry::getValue,
            AbstractMap.SimpleEntry::new
        );
        return entryCodec.listOf().map(
            l -> l.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
            m -> ImmutableList.copyOf(m.entrySet())
        );
    }

    public static <T> DualCodec<ByteBuf, TagKey<T>> tag(ResourceKey<Registry<T>> registry) {
        return new DualCodec<>(TagKey.codec(registry), ResourceLocation.STREAM_CODEC.map(rl -> TagKey.create(registry, rl), TagKey::location));
    }

    public static <E extends Enum<E>> Codec<E> enumCodec(E[] keys) {
        Map<String, E> reverseLookup = Arrays.stream(keys).collect(Collectors.toMap(E::name, Function.identity()));
        return Codec.STRING.xmap(reverseLookup::get, E::name);
    }

    public static <E extends Enum<E>> StreamCodec<ByteBuf, E> enumStreamCodec(E[] keys) {
        return ByteBufCodecs.VAR_INT.map(i -> keys[i], E::ordinal);
    }
}

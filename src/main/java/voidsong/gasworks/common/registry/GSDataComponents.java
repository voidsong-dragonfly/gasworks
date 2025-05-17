package voidsong.gasworks.common.registry;

import malte0811.dualcodecs.DualCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.item.TradeswomansJournalItem.StoredExperience;

public class GSDataComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Gasworks.MOD_ID);

    public static DeferredHolder<DataComponentType<?>, DataComponentType<StoredExperience>> EXPERIENCE_DATA = make("experience_data", StoredExperience.CODECS);

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> make(String name, DualCodec<? super RegistryFriendlyByteBuf, T> codec) {
        return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec.codec()).networkSynchronized(codec.streamCodec()).build());
    }
}

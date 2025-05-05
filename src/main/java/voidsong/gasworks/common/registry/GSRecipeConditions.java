package voidsong.gasworks.common.registry;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.recipe.MetalRealismCondition;
import voidsong.gasworks.common.recipe.NonMetalRealismCondition;

import java.util.function.Supplier;

public class GSRecipeConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, Gasworks.MOD_ID);

    public static final Supplier<MapCodec<NonMetalRealismCondition>> NON_METAL_REALISM = CONDITION_CODECS.register("non_metal_realism", () -> NonMetalRealismCondition.CODEC);
    public static final Supplier<MapCodec<MetalRealismCondition>> METAL_REALISM = CONDITION_CODECS.register("metal_realism", () -> MetalRealismCondition.CODEC);
}

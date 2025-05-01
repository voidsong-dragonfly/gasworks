package voidsong.gasworks.common.recipe;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import voidsong.gasworks.common.Config;

import javax.annotation.Nonnull;

public class MetalRealismCondition implements ICondition {
    public static final MetalRealismCondition INSTANCE = new MetalRealismCondition();

    public static final MapCodec<MetalRealismCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    private MetalRealismCondition() {}

    @Override
    public boolean test(@Nonnull IContext condition) {
        return Config.metalRecipeModifications;
    }

    @Override
    @Nonnull
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    public String toString() {
        return String.valueOf(Config.metalRecipeModifications);
    }
}

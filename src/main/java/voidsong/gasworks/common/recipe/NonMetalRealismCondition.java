package voidsong.gasworks.common.recipe;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import voidsong.gasworks.common.Config;

import javax.annotation.Nonnull;

public class NonMetalRealismCondition implements ICondition {
    public static final NonMetalRealismCondition INSTANCE = new NonMetalRealismCondition();

    public static final MapCodec<NonMetalRealismCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    public NonMetalRealismCondition() {}

    @Override
    public boolean test(@Nonnull IContext condition) {
        return Config.nonMetalRecipeModifications;
    }

    @Override
    @Nonnull
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    public String toString() {
        return String.valueOf(Config.nonMetalRecipeModifications);
    }
}

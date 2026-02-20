package voidsong.gasworks.common.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

/**
 * This interface is for use in mixins to add random ticking to more blocks. Blocks of this class will have the included
 * "divert" or "modify" methods called from those in {@link net.minecraft.world.level.block.Block} so as not to have
 * conflicts of @Override methods in their classes.
 * <p>
 * This interface SHOULD NOT be used outside of mixins!
 * Mixins that use this interface or its subclasses should limit the applicable classes to ensure no subclass blocks have
 * random ticking implemented on them!
 */
public interface VanillaRandomTickBlock {
    boolean gasworks$shouldRandomTickMixinApply();
    boolean gasworks$modifyIsRandomlyTicking(BlockState state, Boolean ticking);
    void gasworks$divertRandomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random);
}

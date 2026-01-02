package voidsong.gasworks.common.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import voidsong.gasworks.mixin.accessor.FlowingFluidAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is an extension of {@link net.minecraft.world.level.block.SimpleWaterloggedBlock}  through VanillaWaterloggedBlock
 * It provides the ability to have the waterlogged block be popped off when fluids flow into it, similar to torches or
 * buttons in Vanilla. It reimplements code from {@link net.minecraft.world.level.material.FlowingFluid#spreadTo(LevelAccessor, BlockPos, BlockState, Direction, FluidState)}
 * and allows any fluid to replace the block, though anything other water will pop it off.
 */
public interface FragileVanillaWaterloggedBlock extends VanillaWaterloggedBlock {
    @Override
    default boolean placeLiquid(@Nonnull LevelAccessor level, @Nonnull BlockPos pos, BlockState state, @Nonnull FluidState fluidState) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED)) {
            if (fluidState.getType() == Fluids.WATER) {
                BlockState blockstate = state.setValue(BlockStateProperties.WATERLOGGED, true);
                // Check if we did things like have torches handle their own dowsed torch placement, if so we don't set the state
                if (!gasworks$handleWaterlogPlacement(state, blockstate, pos, level))
                    level.setBlock(pos, blockstate, 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
                // These exceptions to the placeLiquid code from SimpleWaterloggedBlock exists explicitly to allow torches
                // to be washed away, while also containing waterlogging, it is copied from FlowingFluid#spreadTo
            } else if (fluidState.getType() instanceof FlowingFluidAccessor flowingFluid) {
                flowingFluid.callBeforeDestroyingBlock(level, pos, state);
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            } else {
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            }
            return true;
        } else {
            return false;
        }
    }

    default boolean gasworks$handleWaterlogPlacement(@Nonnull BlockState before, @Nonnull BlockState after, @Nonnull BlockPos pos, @Nonnull LevelAccessor level) {
        return false;
    }

    @Override
    default boolean canPlaceLiquid(@Nullable Player player, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Fluid fluid) {
        return true;
    }

}

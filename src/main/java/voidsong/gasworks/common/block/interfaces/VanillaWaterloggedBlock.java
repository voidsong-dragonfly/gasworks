package voidsong.gasworks.common.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This interface is an extension of {@link net.minecraft.world.level.block.SimpleWaterloggedBlock} for use in mixins to
 * add waterlogging to more blocks. Blocks of this class will have the included "before" or "modify" methods called from
 * those in {@link net.minecraft.world.level.block.Block} so as not to have @Override conflicts in their classes.
 * <p>
 * If this block has an override for {@link net.minecraft.world.level.block.Block#updateShape} or {@link net.minecraft.world.level.block.Block#getStateForPlacement},
 * somewhere in its inheritance chain, it should check for those booleans in gasworks$shouldWaterlogMixinApply! Blocks
 * that extend {@link net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock} should check these,
 * even if the block itself does not override these methods; it has its own special handling for these methods
 * <p>
 * This interface SHOULD NOT be used outside of mixins!
 * Mixins that use this interface or its subclasses should limit the applicable classes to ensure no subclass blocks have
 * waterlogging implemented on them!
 */
public interface VanillaWaterloggedBlock extends SimpleWaterloggedBlock {
    default boolean gasworks$shouldWaterlogMixinApply() {
        return gasworks$shouldWaterlogMixinApply(false, false);
    }
    boolean gasworks$shouldWaterlogMixinApply(boolean updateShapeOverride, boolean getStateForPlacementOverride);

    default BlockState gasworks$addStatesToDefaultState(BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, false) : state;
    }

    default BlockState gasworks$modifyStateForPlacement(BlockState place, BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluid.getType() == Fluids.WATER;
        return place == null ? null : place.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    default FluidState gasworks$modifyGetFluidState(FluidState fluid, BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : fluid;
    }

    /*
     * The two below methods are re-implementations of SimpleWaterloggedBlock methods to check IF a state has a value
     * before trying to place the liquid into it, so it does not crash on calling placeLiquid during other times
     */

    @Override
    default boolean placeLiquid(@Nonnull LevelAccessor level, @Nonnull BlockPos pos, BlockState state, @Nonnull FluidState fluidState) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && !state.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    default ItemStack pickupBlock(@Nullable Player player, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE), 3);
            if (!state.canSurvive(level, pos)) {
                level.destroyBlock(pos, true);
            }
            return new ItemStack(Items.WATER_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }
}

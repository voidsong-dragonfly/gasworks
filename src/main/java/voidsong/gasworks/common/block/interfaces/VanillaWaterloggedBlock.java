package voidsong.gasworks.common.block.interfaces;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/**
 * This interface is an extension of {@link net.minecraft.world.level.block.SimpleWaterloggedBlock} for use in mixins to
 * add waterlogging to more blocks. Blocks of this class will have the included "before" or "modify" methods called from
 * those in {@link net.minecraft.world.level.block.Block} so as not to have @Override conflicts in their classes.
 * <p>
 * This interface SHOULD NOT be used outside of mixins!
 * Mixins that use this interface or its subclasses should limit the applicable classes to ensure no subclass blocks have
 * waterlogging implemented on them!
 */
public interface VanillaWaterloggedBlock extends SimpleWaterloggedBlock {
    boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz);

    default BlockState gasworks$addStatesToDefaultState(BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state : state.setValue(BlockStateProperties.WATERLOGGED, false);
    }

    default void gasworks$addStatesToBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    default BlockState gasworks$modifyStateForPlacement(BlockState place, BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluid.getType() == Fluids.WATER;
        return place == null ? null : place.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    default FluidState gasworks$modifyGetFluidState(FluidState fluid, BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : fluid;
    }
}

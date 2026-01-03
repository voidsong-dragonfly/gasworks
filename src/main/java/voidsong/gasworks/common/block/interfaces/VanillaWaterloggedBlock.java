package voidsong.gasworks.common.block.interfaces;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;


/**
 * This interface is an extension of {@link net.minecraft.world.level.block.SimpleWaterloggedBlock} for use in mixins to
 * add waterlogging to more blocks. Blocks of this class will have the included "before" or "modify" methods called from
 * those in {@link net.minecraft.world.level.block.Block} so as not to have @Override conflicts in their classes.
 * <p>
 * If this block has an override for {@link net.minecraft.world.level.block.Block#getShape} or {@link net.minecraft.world.level.block.Block#getStateForPlacement},
 * somewhere in its inheritance chain, it should check for those booleans in gasworks$shouldWaterlogMixinApply! Blocks
 * that extend {@link net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock} should check these,
 * even if the block itself does not override these methods; it has its own special handling for these methods
 * <p>
 * This interface SHOULD NOT be used outside of mixins!
 * Mixins that use this interface or its subclasses should limit the applicable classes to ensure no subclass blocks have
 * waterlogging implemented on them!
 */
public interface VanillaWaterloggedBlock extends SimpleWaterloggedBlock {
    default boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz) {
        return gasworks$shouldWaterlogMixinApply(clazz, false, false);
    }
    boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz, boolean getShapeOverride, boolean getStateForPlacementOverride);

    default BlockState gasworks$addStatesToDefaultState(BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state : state.setValue(BlockStateProperties.WATERLOGGED, false);
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

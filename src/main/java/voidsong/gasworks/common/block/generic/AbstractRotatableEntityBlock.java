package voidsong.gasworks.common.block.generic;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import voidsong.gasworks.common.block.properties.GSProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractRotatableEntityBlock extends Block implements EntityBlock {
    public AbstractRotatableEntityBlock(Properties props) {
        super(props);
        this.registerDefaultState(getInitDefaultState());
    }

    protected BlockState getInitDefaultState() {
        BlockState ret = this.stateDefinition.any();
        if(ret.hasProperty(GSProperties.FACING_ALL))
            ret = ret.setValue(GSProperties.FACING_ALL, getDefaultFacing());
        else if(ret.hasProperty(GSProperties.FACING_HORIZONTAL))
            ret = ret.setValue(GSProperties.FACING_HORIZONTAL, getDefaultFacing());
        return ret;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        Property<Direction> prop = state == null ? null : findFacingProperty(state);
        if (prop != null) {
            if (prop==GSProperties.FACING_HORIZONTAL)
                return state.setValue(GSProperties.FACING_HORIZONTAL, context.getHorizontalDirection());
            else if (prop==GSProperties.FACING_ALL)
                return state.setValue(GSProperties.FACING_ALL, context.getClickedFace());
        }
        return this.defaultBlockState();
    }

    protected Direction getDefaultFacing() {
        return Direction.NORTH;
    }

    @Override
    @Nonnull
    public BlockState rotate(@Nonnull BlockState state, @Nonnull Rotation rot) {
        Property<Direction> facingProp = findFacingProperty(state);
        if(facingProp!=null&&canRotate()) {
            Direction currentDirection = state.getValue(facingProp);
            Direction newDirection = rot.rotate(currentDirection);
            return state.setValue(facingProp, newDirection);
        }
        return super.rotate(state, rot);
    }

    @Override
    @Nonnull
    public BlockState mirror(BlockState state, @Nonnull Mirror mirrorIn) {
        if(state.hasProperty(GSProperties.MIRRORED)&&canRotate()&&mirrorIn==Mirror.LEFT_RIGHT)
            return state.setValue(GSProperties.MIRRORED, !state.getValue(GSProperties.MIRRORED));
        else {
            Property<Direction> facingProp = findFacingProperty(state);
            if(facingProp!=null&&canRotate()) {
                Direction currentDirection = state.getValue(facingProp);
                Direction newDirection = mirrorIn.mirror(currentDirection);
                return state.setValue(facingProp, newDirection);
            }
        }
        return super.mirror(state, mirrorIn);
    }

    @Nullable
    private Property<Direction> findFacingProperty(BlockState state) {
        if(state.hasProperty(GSProperties.FACING_ALL))
            return GSProperties.FACING_ALL;
        else if(state.hasProperty(GSProperties.FACING_HORIZONTAL))
            return GSProperties.FACING_HORIZONTAL;
        else
            return null;
    }

    /**
     * Multiblocks that can rotate effectively are few and far between - only the cloche
     * & preheater work well in IE for that. Gasworks has none that need it.
     * @return Whether this block should rotate, default false for multiblocks
     */
    protected boolean canRotate() {
        return !getStateDefinition().getProperties().contains(GSProperties.MULTIBLOCK_SLAVE);
    }
}

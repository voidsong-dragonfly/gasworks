package voidsong.gasworks.common.block.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import voidsong.gasworks.common.block.properties.GSProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractMultiblockEntityBlock extends AbstractRedstoneEntityBlock {
    public AbstractMultiblockEntityBlock(Properties props, boolean source, boolean comparable) {
        super(props, source, comparable);
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(GSProperties.FACING_HORIZONTAL, GSProperties.MULTIBLOCK_SLAVE);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if(canPlaceAssembled(context) && state != null)
            return getGuaranteedStateForPlacement(context, state);
        else
            return null;
    }

    @Nonnull
    protected abstract BlockState getGuaranteedStateForPlacement(@Nonnull BlockPlaceContext context, @Nonnull BlockState current);

    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            assemble(level, pos, state, placer, stack);
        }
    }

    @Override
    protected BlockState getInitDefaultState() {
        return super.getInitDefaultState()
            .setValue(GSProperties.MULTIBLOCK_SLAVE, false)
            .setValue(GSProperties.ACTIVE, false);
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState replace, boolean isMoving) {
        if(state.getBlock()!=replace.getBlock())
            disassemble(level, pos, state, replace);
        super.onRemove(state, level, pos, replace, isMoving);
    }

    protected abstract boolean canPlaceAssembled(BlockPlaceContext context);

    protected abstract void assemble(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack);

    protected abstract void disassemble(Level level, BlockPos pos, BlockState state, BlockState replace);
}

package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import voidsong.gasworks.common.block.generic.AbstractMultiblockEntityBlock;
import voidsong.gasworks.common.block.properties.GSProperties;

import javax.annotation.Nonnull;

public abstract class SluiceBlock extends AbstractMultiblockEntityBlock {
    public SluiceBlock(Properties props) {
        super(props, false, true);
    }

    @Override
    protected BlockState getGuaranteedStateForPlacement(@Nonnull BlockPlaceContext context, @Nonnull BlockState current) {
       return current.setValue(GSProperties.FACING_HORIZONTAL, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected boolean canPlaceAssembled(BlockPlaceContext context) {
        Direction horizontal = context.getHorizontalDirection().getOpposite();
        BlockPos secondPosition = context.getClickedPos().relative(horizontal);
        return context.getLevel().getBlockState(secondPosition).canBeReplaced() && context.getLevel().getEntities(context.getPlayer(), new AABB(secondPosition)).isEmpty();
    }

    @Override
    protected void assemble(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        Direction horizontal = state.getValue(GSProperties.FACING_HORIZONTAL);
        level.setBlockAndUpdate(pos.relative(horizontal), this.defaultBlockState().setValue(GSProperties.FACING_HORIZONTAL, horizontal).setValue(GSProperties.MULTIBLOCK_SLAVE, true));
    }

    @Override
    protected void disassemble(Level level, BlockPos pos, BlockState state, BlockState replace) {
        Direction horizontal = state.getValue(GSProperties.FACING_HORIZONTAL);
        if (state.getValue(GSProperties.MULTIBLOCK_SLAVE)) {
            if (level.getBlockState(pos.relative(horizontal.getOpposite())).getBlock() instanceof SluiceBlock block)
                block.disassemble(level, pos, state, replace);
        } else
            level.removeBlock(pos.relative(horizontal), false);
    }


}

package voidsong.gasworks.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import voidsong.gasworks.api.GSTags;
import voidsong.gasworks.common.block.UnlitTorchBlock;
import voidsong.gasworks.common.block.properties.GSProperties;
import voidsong.gasworks.common.registry.GSBlocks;

import javax.annotation.Nullable;

public class BlockUtil {
    public static boolean canReplaceRegion(BlockPos start, BlockPos end, BlockPlaceContext context) {
        Level w = context.getLevel();
        return BlockPos.betweenClosedStream(start, end).allMatch(
            pos -> {
                BlockPlaceContext subContext = BlockPlaceContext.at(context, pos, context.getClickedFace());
                return w.getBlockState(pos).canBeReplaced(subContext);
            });
    }

    public static BlockState getDowsedTorchState(BlockState state, boolean water) {
        Direction facing = state.hasProperty(HorizontalDirectionalBlock.FACING) ? state.getValue(HorizontalDirectionalBlock.FACING) : null;
        if (state.is(Blocks.TORCH)) {
            return GSBlocks.UNLIT_TORCH.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, water).setValue(GSProperties.SMOLDERING, !water);
        } else if (state.is(Blocks.WALL_TORCH)) {
            BlockState placement = GSBlocks.UNLIT_WALL_TORCH.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, water).setValue(GSProperties.SMOLDERING, !water);
            return facing != null ? placement.setValue(HorizontalDirectionalBlock.FACING, facing) : placement;
        } else if (state.is(Blocks.SOUL_TORCH)) {
            return GSBlocks.UNLIT_SOUL_TORCH.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, water).setValue(GSProperties.SMOLDERING, !water);
        } else if (state.is(Blocks.SOUL_WALL_TORCH)) {
            BlockState placement = GSBlocks.UNLIT_SOUL_WALL_TORCH.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, water).setValue(GSProperties.SMOLDERING, !water);
            return facing != null ? placement.setValue(HorizontalDirectionalBlock.FACING, facing) : placement;
        } else if (state.getBlock() instanceof UnlitTorchBlock) {
            boolean previouslyWaterlogged = state.getValue(BlockStateProperties.WATERLOGGED);
            return state.setValue(GSProperties.SMOLDERING, false).setValue(BlockStateProperties.WATERLOGGED, previouslyWaterlogged || water);
        }
        return state;
    }

    public static void dowseTorch2(@Nullable Entity player, BlockState state, LevelAccessor level, BlockPos pos, boolean waterlog) {
        if (state.is(waterlog ? GSTags.Blocks.DOWSE_IN_WATER : GSTags.Blocks.DOWSE_IN_RAIN)) {
            level.setBlock(pos, getDowsedTorchState(state, waterlog), 3);
            level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
    }
}

package voidsong.gasworks.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import voidsong.gasworks.common.block.properties.GSProperties;

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

    public static void dowseTorch(@Nullable Entity player, BlockState state, LevelAccessor level, BlockPos pos) {
        if ((state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH) && state.getValue(GSProperties.LIT))) {
            level.setBlock(pos, state.setValue(GSProperties.LIT, false), 11);
            level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }
    }
}

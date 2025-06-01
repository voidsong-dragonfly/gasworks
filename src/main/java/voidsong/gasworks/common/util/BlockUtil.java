package voidsong.gasworks.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;

public class BlockUtil {
    public static boolean canReplaceRegion(BlockPos start, BlockPos end, BlockPlaceContext context) {
        Level w = context.getLevel();
        return BlockPos.betweenClosedStream(start, end).allMatch(
            pos -> {
                BlockPlaceContext subContext = BlockPlaceContext.at(context, pos, context.getClickedFace());
                return w.getBlockState(pos).canBeReplaced(subContext);
            });
    }
}

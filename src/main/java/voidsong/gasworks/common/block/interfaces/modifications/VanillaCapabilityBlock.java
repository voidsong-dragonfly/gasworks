package voidsong.gasworks.common.block.interfaces.modifications;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This interface is for use in mixins to add capabilities to Vanilla blocks. Blocks of this class will have the included
 * methods called from those in {@link net.minecraft.world.level.block.Block} or other superclasses so as not to have
 * conflicts of @Override methods in their classes.
 * <p>
 * This interface SHOULD NOT be used outside of mixins!
 * Mixins that use this interface or its subclasses should limit the applicable blocks so as to only invalidate
 * capabilities on the blocks capabilities are registered to elsewhere.
 * <p>
 * {@link net.minecraft.world.level.block.state.BlockBehaviour#onPlace(BlockState, Level, BlockPos, BlockState, boolean)} and
 * {@link net.minecraft.world.level.block.state.BlockBehaviour#onRemove(BlockState, Level, BlockPos, BlockState, boolean)}
 * are handled separated in {@link voidsong.gasworks.mixin.BaseBlockInjections.BlockBehaviorMixin} for all blocks which
 * {@link VanillaCapabilityBlock#gasworks$shouldProcessCapabilities(Block, boolean)} specifies as allowable
 */
public interface VanillaCapabilityBlock {
    boolean gasworks$shouldProcessCapabilities(Block block, boolean shapeUpdate);
    void gasworks$onUpdateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos);
}

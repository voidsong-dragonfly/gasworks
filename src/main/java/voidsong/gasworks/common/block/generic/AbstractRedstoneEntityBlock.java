package voidsong.gasworks.common.block.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import voidsong.gasworks.api.block.IComparatorBlockEntity;
import voidsong.gasworks.api.block.IRedstoneBlockEntity;

import javax.annotation.Nonnull;

public abstract class AbstractRedstoneEntityBlock extends AbstractRotatableEntityBlock {
    final boolean comparable;
    final boolean source;

    public AbstractRedstoneEntityBlock(Properties props, boolean connectable, boolean comparable) {
        super(props);
        this.comparable = comparable;
        this.source = connectable;
    }

    @Override
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return comparable;
    }

    @Override
    public int getAnalogOutputSignal(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos) {
        if (!comparable) return 0;
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof IComparatorBlockEntity comparableEntity)
            return comparableEntity.getComparatorOutput();
        return 0;
    }

    @Override
    public boolean isSignalSource(@Nonnull BlockState state) {
        return source;
    }

    @Override
    public boolean canConnectRedstone(@Nonnull BlockState state, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, Direction side) {
        if (!source) return false;
        BlockEntity entity = getter.getBlockEntity(pos);
        if(entity instanceof IRedstoneBlockEntity redstoneEntity)
            return redstoneEntity.canConnectRedstone(side);
        return false;
    }

    @Override
    public int getSignal(@Nonnull BlockState blockState, @Nonnull BlockGetter getter, @Nonnull BlockPos pos, @Nonnull Direction side) {
        if (!source) return 0;
        BlockEntity entity = getter.getBlockEntity(pos);
        if(entity instanceof IRedstoneBlockEntity redstoneEntity)
            return redstoneEntity.getWeakRedstoneOutput(side);
        return 0;
    }

    @Override
    public int getDirectSignal(@Nonnull BlockState blockState, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull Direction side) {
        if (!source) return 0;
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof IRedstoneBlockEntity redstoneEntity)
            return redstoneEntity.getStrongRedstoneOutput(side);
        return 0;
    }
}

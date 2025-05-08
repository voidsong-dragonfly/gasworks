package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ClampBlock extends Block {
    public static final BooleanProperty FIRED = BooleanProperty.create("fired");
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 48);

    public ClampBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FIRED, false).setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FIRED).add(AGE);
    }

    public static BlockState getFiredState(@Nonnull BlockState previous, int increment) {
        int age = Math.clamp(previous.getValue(ClampBlock.AGE)+increment, 0, 48);
        return previous.setValue(AGE, age).setValue(FIRED, age==48);
    }

    @Override
    @Nonnull
    public SoundType getSoundType(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos, @Nullable Entity entity) {
        return state.getValue(FIRED)?SoundType.STONE:SoundType.GRAVEL;
    }
}

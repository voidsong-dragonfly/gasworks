package voidsong.gasworks.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nonnull;

public class ClampBlock extends Block {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 47);
    public final BlockState fired;

    public ClampBlock(BlockBehaviour.Properties properties, BlockState firedState) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
        fired = firedState;
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    public static BlockState getHeatedState(@Nonnull BlockState previous, int increment) {
        if(previous.getBlock() instanceof ClampBlock) {
            int age = Math.clamp(previous.getValue(ClampBlock.AGE) + increment, 0, 48);
            return age < 48 ? previous.setValue(AGE, age) : ((ClampBlock) previous.getBlock()).fired;
        } else
            return previous;
    }
}

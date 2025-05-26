package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import voidsong.gasworks.api.GSTags;

import javax.annotation.Nonnull;

public class CompostBlock extends Block {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);

    public CompostBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    @Override
    public boolean isRandomlyTicking(@Nonnull BlockState state) {
        return state.getValue(AGE) < 4;
    }

    @Override
    protected void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (level.isClientSide) return;
        // Check the chance of the composting state advancing & advance the composting state as necessary
        if (random.nextInt(96) < getSurroundingsBoost(level, pos, state.getValue(AGE)))
            level.setBlockAndUpdate(pos, state.setValue(AGE, Math.clamp(state.getValue(AGE) + 1, 0, 4)));
    }

    /**
     * Get the boost from the surrounding blocks
     * @param level LevelReader world the check happens in
     * @param pos BlockPos position to check
     * @param age the age of the {@link CompostBlock} at this position
     * @return boolean valid to continue pyrolysis
     */
    private int getSurroundingsBoost(@Nonnull LevelReader level, @Nonnull BlockPos pos, int age) {
        int boost = 0;
        boolean water = false;
        boolean overwatered = false;
        // Check surrounding positions for various boost blocks
        for (BlockPos search : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            BlockState state = level.getBlockState(search);
            // Blocks by tag are one boost only
            if (state.is(GSTags.BlockTags.COMPOST_ACCELERATORS))
                boost += 1;
            // Silverfish are 3 because they are quite hard to get ahold of
            else if (state.getBlock() instanceof InfestedBlock)
                boost += 3;
            // Compost blocks provide a boost as well, in two parts - the basic is just one
            else if (state.getBlock() instanceof CompostBlock)
                boost += 1;
            // Water only provides a boost if there's not too much of it
            if (state.getFluidState().is(FluidTags.WATER)) {
                if (water) overwatered = true;
                water = true;
            }
        }
        // Compost directly abutting this compost provides a boost based upon the value of the difference of ages
        for(Direction dir : Direction.values()) {
            BlockPos check = pos.offset(dir.getNormal());
            BlockState neighbor = level.getBlockState(check);
            boost += getDirectSurroundingsBoost(neighbor, age);
        }
        return boost + (overwatered ? -5 : water ? 5 : 0);
    }

    /**
     * Gets the boost from the local-surrounding blocks & makes sure the block is covered
     * @param state BlockState to check the validity of
     * @param baseAge int age of the compost block the check is from
     * @return validity as a wall or fuel block
     */
    protected int getDirectSurroundingsBoost(@Nonnull BlockState state, int baseAge) {
        return state.canBeReplaced() ? -1 : state.getBlock() instanceof CompostBlock ? Math.max((state.getValue(AGE)-baseAge), 0) : 0;
    }

    @Override
    public int getFireSpreadSpeed(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return state.getValue(AGE) > 2 ? 0 : 10;
    }

    @Override
    public int getFlammability(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return state.getValue(AGE) > 2 ? 0 : 30;
    }
}

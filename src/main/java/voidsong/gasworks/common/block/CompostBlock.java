package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;
import voidsong.gasworks.api.GSTags;

import javax.annotation.Nonnull;

public class CompostBlock extends Block {
    public static final int WATER_MODIFIER = 5;
    public static final int AIR_DETRIMENT = 3;
    public static final int MAX_AIR_DETRIMENT = -6;
    public static final int SILVERFISH_BOOST = 3;
    public static final int BASE_BOOST = 1;
    public static final int MIN_BOOST = 3;
    public static final int MAX_AGE = 4;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

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
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    protected void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (level.isClientSide) return;
        // Check the chance of the composting state advancing & advance the composting state as necessary
        if (random.nextInt(48) < getSurroundingsBoost(level, pos, state.getValue(AGE)))
            level.setBlockAndUpdate(pos, state.setValue(AGE, Math.clamp(state.getValue(AGE) + 1, 0, MAX_AGE)));
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
            if (state.is(GSTags.Blocks.COMPOST_ACCELERATORS))
                boost += BASE_BOOST;
            // Silverfish are 3 because they are quite hard to get ahold of
            else if (state.getBlock() instanceof InfestedBlock)
                boost += SILVERFISH_BOOST;
            // Compost blocks provide a boost as well, in two parts - the basic is just one
            else if (state.getBlock() instanceof CompostBlock)
                boost += BASE_BOOST;
            // Water only provides a boost if there's not too much of it
            if (state.getFluidState().is(FluidTags.WATER)) {
                if (water) overwatered = true;
                water = true;
            }
        }
        // Compost directly abutting this compost provides a boost based upon the value of the difference of ages
        // There is also a detriment for being open to the air on a maximum of two sides
        int exposure = 0;
        for(Direction dir : Direction.values()) {
            BlockPos check = pos.offset(dir.getNormal());
            BlockState neighbor = level.getBlockState(check);
            boost += getDirectSurroundingsBoost(neighbor, age);
            exposure = Math.clamp(exposure - AIR_DETRIMENT, MAX_AIR_DETRIMENT, 0);
        }
        // Maximum reasonably likely boost is ~33, maximum silverfish reasonable boost is 57, maximum possible boost is ~90
        return Math.max(boost + (overwatered ? -WATER_MODIFIER : water ? WATER_MODIFIER : 0) - exposure, MIN_BOOST);
    }

    /**
     * Gets the boost from the local-surrounding blocks & makes sure the block is covered
     * @param state BlockState to check the validity of
     * @param baseAge int age of the compost block the check is from
     * @return validity as a wall or fuel block
     */
    protected int getDirectSurroundingsBoost(@Nonnull BlockState state, int baseAge) {
        return state.getBlock() instanceof CompostBlock ? Math.max((state.getValue(AGE)-baseAge), 0) : 0;
    }

    @Override
    public int getExpDrop(@Nonnull BlockState state, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, @Nonnull ItemStack tool) {
        return state.getValue(AGE) == 5 ? UniformInt.of(1, 3).sample(level.getRandom()) : 0;
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

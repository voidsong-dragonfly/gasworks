package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;
import voidsong.gasworks.common.block.properties.AshType;
import voidsong.gasworks.common.registry.GSBlocks;
import voidsong.gasworks.common.registry.GSTags;

import javax.annotation.Nonnull;

public class PyrolizingBlock extends RotatedPillarBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 15);
    private final AshType product;
    private final TagKey<Block> validWalls;

    public PyrolizingBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(AGE, 0));
        product = AshType.CHARCOAL;
        validWalls = GSTags.BlockTags.PYROLIZING_WALLS;
    }

    public PyrolizingBlock(BlockBehaviour.Properties properties, @Nonnull AshType type, @Nonnull TagKey<Block> walls) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(AGE, 0));
        product = type;
        validWalls = walls;
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT).add(AGE);
    }

    @Override
    protected void onPlace(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        level.scheduleTick(pos, this, getFireTickDelay(level.random));
    }

    @Override
    public void onCaughtFire(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        level.setBlockAndUpdate(pos, state.setValue(LIT, true));
        super.onCaughtFire(state, level, pos, direction, igniter);
    }

    @Override
    @Nonnull
    protected BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction direction, BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        return neighborState.isBurning(level, neighborPos) ? state.setValue(LIT, true) : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(@Nonnull BlockState state, @Nonnull UseOnContext context, @Nonnull ItemAbility itemAbility, boolean simulate) {
        return ItemAbility.getActions().contains(ItemAbilities.FIRESTARTER_LIGHT) ? state.setValue(LIT, true) : super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    @Override
    protected void tick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        //Schedule our next time to tick
        level.scheduleTick(pos, this, getFireTickDelay(level.random));
        //Only want to progress burning if firetick is enabled
        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)&&state.getValue(LIT)&&!this.isNearRain(level, pos)) {
            int age = state.getValue(AGE);
            //Check if we should continue with the pyrolysis process & continue as necessary
            if(age > 5) {
                if (this.validSurroundings(level, pos)) {
                    for (Direction dir : Direction.values()) {
                        BlockPos next = pos.offset(dir.getNormal());
                        if (level.getBlockState(next).getBlock() instanceof PyrolizingBlock)
                            level.setBlockAndUpdate(next, level.getBlockState(next).setValue(LIT, true));
                    }
                    if (age+1 > 15)
                        level.setBlockAndUpdate(pos, finalProduct());
                    else
                        level.setBlockAndUpdate(pos, state.setValue(AGE, age + 1));
                } else
                    level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
            }
            else
                level.setBlockAndUpdate(pos, state.setValue(AGE, age+1));
        } else if (state.getValue(LIT))
            level.setBlockAndUpdate(pos, state.setValue(LIT, false));
    }

    /**
     * Check to make sure the surroundings are valid to continue pyrolysis
     * Checks for solid faces and valid blocks
     * @param level LevelReader world the check happens in
     * @param pos BlockPos position to check
     * @return boolean valid to continue pyrolysis
     */
    private boolean validSurroundings(@Nonnull LevelReader level, @Nonnull BlockPos pos) {
        for(Direction dir : Direction.values()) {
            BlockPos check = pos.offset(dir.getNormal());
            BlockState neighbor = level.getBlockState(check);
            if(!(validNeighborBlock(level, neighbor, check, dir.getOpposite())&&neighbor.isFaceSturdy(level, check, dir.getOpposite()))) return false;
        }
        return true;
    }

    /**
     * Checks whether a material is valid (walls, ash, or fuel) as well as inflammability of walls
     * @param level LevelReader world the check happens in
     * @param state BlockState to check the validity of
     * @param pos BlockPos position to check
     * @param dir Direction to check flammability on the side of
     * @return validity as a wall or fuel block
     */
    protected boolean validNeighborBlock(@Nonnull LevelReader level, @Nonnull BlockState state, @Nonnull BlockPos pos, @Nonnull Direction dir) {
        return (state.getTags().toList().contains(validWalls)&&!state.isFlammable(level, pos, dir))||
               (state.getBlock() instanceof PyrolizingBlock)||
               (state.getBlock() instanceof PyrolyticAshBlock);
    }

    /**
     * The product we want to turn the pyrolytic block into
     * @return BlockState final product
     */
    private BlockState finalProduct() {
        return GSBlocks.PYROLYTIC_ASH.get().defaultBlockState().setValue(PyrolyticAshBlock.ASH_TYPE, product);
    }


    @Override
    public int getFireSpreadSpeed(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return 5;
    }

    @Override
    public int getFlammability(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return 5;
    }

    /*
     * The methods below are modified from BaseFireBlock & FireBlock to work for a block which operates on slightly different assumptions
     */
    @Override
    public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if(!state.getValue(LIT)) return;
        if (random.nextInt(36) == 0) {
            level.playLocalSound(
                (double) pos.getX() + 0.5,
                (double) pos.getY() + 0.5,
                (double) pos.getZ() + 0.5,
                SoundEvents.FIRE_AMBIENT,
                SoundSource.BLOCKS,
                1.0F + random.nextFloat(),
                random.nextFloat() * 0.7F + 0.3F,
                false
            );
        }

        for (int i = 0; i < 2; i++) {
            double d0 = (double) pos.getX() + random.nextDouble();
            double d1 = (double) pos.getY() + random.nextDouble() * 0.5 + 0.5;
            double d2 = (double) pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
        }
    }

    protected boolean isNearRain(@Nonnull Level level, @Nonnull BlockPos pos) {
        return level.isRainingAt(pos) || level.isRainingAt(pos.west()) || level.isRainingAt(pos.east()) || level.isRainingAt(pos.north()) || level.isRainingAt(pos.south());
    }

    private static int getFireTickDelay(RandomSource random) {
        return 480 + random.nextInt(160);
    }
}

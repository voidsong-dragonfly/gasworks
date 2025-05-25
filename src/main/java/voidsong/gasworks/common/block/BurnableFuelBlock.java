package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import voidsong.gasworks.api.GSTags;

import javax.annotation.Nonnull;

public class BurnableFuelBlock extends RotatedPillarBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 15);
    private final AshType product;
    private final int clampCookMult;

    public BurnableFuelBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(AGE, 0));
        product = AshType.NONE;
        clampCookMult = 1;
    }

    public BurnableFuelBlock(BlockBehaviour.Properties properties, @Nonnull AshType type, int speed) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(AGE, 0));
        product = type;
        clampCookMult = speed;
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
        //Only want to progress burning if firetick is enabled, the pile is not lit, & the block has not been put out by rain
        if (level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)&&state.getValue(LIT)&&!this.isNearRain(level, pos)) {
            int age = state.getValue(AGE)+1;
            //Increment burn time to make sure state is tracked
            if(age < 16) level.setBlockAndUpdate(pos, state.setValue(AGE, age));
            //We can fire bricks with this, so we check for clamp structures and increase the firing of the bricks
            for(int x=-1;x<=1;x++) {
                for(int z=-1;z<=1;z++) {
                    BlockState check0 = level.getBlockState(pos.offset(x, 0, z));
                    BlockState check1 = level.getBlockState(pos.offset(x, 1, z));
                    if(check0.getBlock() instanceof ClampBlock)
                        level.setBlockAndUpdate(pos.offset(x, 0, z), ClampBlock.getHeatedState(check0, clampCookMult));
                    if (check1.getBlock() instanceof ClampBlock) {
                        BlockState check2 = level.getBlockState(pos.offset(x, 2, z));
                        level.setBlockAndUpdate(pos.offset(x, 1, z), ClampBlock.getHeatedState(check1, clampCookMult));
                        if (check2.getBlock() instanceof ClampBlock)
                            level.setBlockAndUpdate(pos.offset(x, 2, z), ClampBlock.getHeatedState(check2, clampCookMult));
                    }
                }
            }
            //If age is >1, it's hot enough to set other blocks near it alight
            if(age > 1) {
                for (Direction dir : Direction.values()) {
                    BlockPos next = pos.offset(dir.getNormal());
                    if (level.getBlockState(next).getBlock() instanceof BurnableFuelBlock)
                        level.setBlockAndUpdate(next, level.getBlockState(next).setValue(LIT, true));
                }
            }
            //If age is >5, it's burned long enough to go out if it's not undergoing pyrolysis, so we have additional checks
            if(age > 5) {
                //Check that the surroundings are valid for pyrolysis, & if so, check if we need to complete pyrolysis. Else, burn the block
                if (this.validSurroundings(level, pos)) {
                    if (age > 15)
                        level.setBlockAndUpdate(pos, finalProduct());
                } else {
                    level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                }
            }
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
        return (product!=AshType.NONE&&state.is(GSTags.BlockTags.PYROLIZING_WALLS)&&!state.isFlammable(level, pos, dir))||
               (state.getBlock() instanceof BurnableFuelBlock)||
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

package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import voidsong.gasworks.common.block.properties.GSProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;

public class CandelabraBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = state -> state.getValue(LIT) ? 4 * state.getValue(CANDLES) - 1 : 0;

    public CandelabraBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(CANDLES, 1).setValue(LIT, false).setValue(WATERLOGGED, false).setValue(GSProperties.FACING_ALL, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CANDLES, LIT, WATERLOGGED, GSProperties.FACING_ALL);
    }

    @Override
    @Nonnull
    protected VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return Shapes.box(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
        // TODO: DEFINE STATES
    }

    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return switch (state.getValue(GSProperties.FACING_ALL)) {
            case DOWN -> switch(state.getValue(CANDLES)) {
                case 1 -> List.of(new Vec3(0.5, 0.9375, 0.5));
                case 2 -> List.of(new Vec3(0.4375, 0.875, 0.4375), new Vec3(0.5625, 0.9375, 0.5625));
                case 3 -> List.of(new Vec3(0.25f, 0.9375, 0.75), new Vec3(0.25, 1.0625, 0.25), new Vec3(0.8125, 1, 0.5));
                case 4 -> List.of(new Vec3(0.1875, 0.875, 0.5), new Vec3(0.8125, 0.9375, 0.5), new Vec3(0.5, 1.0625, 0.1875), new Vec3(0.5, 1, 0.8125));
                default -> List.of();
            };
            case NORTH -> switch(state.getValue(CANDLES)) {
                case 1 -> List.of(new Vec3(0.5, 1, 0.1875));
                case 2 -> List.of(new Vec3(0.3125, 1, 0.1875), new Vec3(0.6875, 0.9375, 0.1875));
                case 3 -> List.of(new Vec3(0.8125, 0.875, 0.1875), new Vec3(0.1875, 0.9375, 0.1875), new Vec3(0.5, 1, 0.3125));
                case 4 -> List.of(new Vec3(0.1875, 0.9375, 0.1875), new Vec3(0.8125, 1, 0.1875), new Vec3(0.3125, 0.75, 0.375), new Vec3(0.6875, 0.9375, 0.375));
                default -> List.of();
            };
            case EAST -> switch(state.getValue(CANDLES)) {
                case 1 -> List.of(new Vec3(0.8125, 1, 0.5));
                case 2 -> List.of(new Vec3(0.8125, 1, 0.3125), new Vec3(0.8125, 0.9375, 0.6875));
                case 3 -> List.of(new Vec3(0.8125, 0.875, 0.8125), new Vec3(0.8125, 0.9375, 0.1875), new Vec3(0.6875, 1, 0.5));
                case 4 -> List.of(new Vec3(0.8125, 0.9375, 0.1875), new Vec3(0.8125, 1, 0.8125), new Vec3(0.625, 0.75, 0.3125), new Vec3(0.625, 0.9375, 0.6875));
                default -> List.of();
            };
            case WEST -> switch(state.getValue(CANDLES)) {
                case 1 -> List.of(new Vec3(0.1875, 1, 0.5));
                case 2 -> List.of(new Vec3(0.1875, 0.9375, 0.3125), new Vec3(0.1875, 1, 0.6875));
                case 3 -> List.of(new Vec3(0.1875, 0.9375, 0.8125), new Vec3(0.1875, 0.875, 0.1875), new Vec3(0.3125, 1, 0.5));
                case 4 -> List.of(new Vec3(0.1875, 1, 0.1875), new Vec3(0.1875, 0.9375, 0.8125), new Vec3(0.375, 0.9375, 0.3125), new Vec3(0.375, 0.75, 0.6875));
                default -> List.of();
            };
            case SOUTH -> switch(state.getValue(CANDLES)) {
                case 1 -> List.of(new Vec3(0.5, 1, 0.8125));
                case 2 -> List.of(new Vec3(0.3125, 0.9375, 0.8125), new Vec3(0.6875, 1, 0.8125));
                case 3 -> List.of(new Vec3(0.8125, 0.9375, 0.8125), new Vec3(0.1875, 0.875, 0.8125), new Vec3(0.5, 1, 0.6875));
                case 4 -> List.of(new Vec3(0.1875, 1, 0.8125), new Vec3(0.8125, 0.9375, 0.8125), new Vec3(0.3125, 0.9375, 0.6875), new Vec3(0.6875, 0.75, 0.625));
                default -> List.of();
            };
            case UP -> switch(state.getValue(CANDLES)) {
                case 1 -> List.of(new Vec3(0.5, 0.5625, 0.5));
                case 2 -> List.of(new Vec3(0.4375, 0.5, 0.4375), new Vec3(0.5625, 0.5625, 0.5625));
                case 3 -> List.of(new Vec3(0.25f, 0.8125, 0.75), new Vec3(0.25, 0.9375, 0.25), new Vec3(0.8125, 0.875, 0.5));
                case 4 -> List.of(new Vec3(0.1875, 0.75, 0.1875), new Vec3(0.8125, 0.875, 0.1875), new Vec3(0.8125, 0.8125, 0.8125), new Vec3(0.1875, 0.6875, 0.8125));
                default -> List.of();
            };
        };
    }

    @Override
    public @Nullable BlockState getToolModifiedState(@Nonnull BlockState state, @Nonnull UseOnContext context, @Nonnull ItemAbility itemAbility, boolean simulate) {
        if (ItemAbility.getActions().contains(ItemAbilities.FIRESTARTER_LIGHT) && !state.getValue(LIT) && !state.getValue(WATERLOGGED))
            return state.setValue(LIT, true);
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    @Override
    protected float getShadeBrightness(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return 1.0F;
    }

    /*
     * Methods from CandleBlock
     */

    @Override
    @Nonnull
    protected ItemInteractionResult useItemOn(ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hitResult) {
        if (stack.isEmpty() && player.getAbilities().mayBuild && state.getValue(LIT)) {
            extinguish(player, state, level, pos);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
    }

    @Override
    protected boolean canBeReplaced(@Nonnull BlockState state, BlockPlaceContext context) {
        return !context.isSecondaryUseActive() && context.getItemInHand().is(asItem()) && state.getValue(CANDLES) < 4 || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clicked = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(clicked);
        if (state.is(this)) {
            return state.cycle(CANDLES);
        } else {
            FluidState fluid = context.getLevel().getFluidState(clicked);
            boolean flag = fluid.getType() == Fluids.WATER;
            BlockState place = super.getStateForPlacement(context);
            return place == null ? null : place.setValue(WATERLOGGED, flag).setValue(GSProperties.FACING_ALL, context.getClickedFace().getOpposite());
        }
    }

    @Override
    @Nonnull
    protected BlockState updateShape(BlockState state, @Nonnull Direction direction, @Nonnull BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return state.getValue(GSProperties.FACING_ALL).equals(direction) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    @Nonnull
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(@Nonnull LevelAccessor level, @Nonnull BlockPos pos, BlockState state, @Nonnull FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            BlockState blockstate = state.setValue(WATERLOGGED, true);
            if (state.getValue(LIT))
                extinguish(null, blockstate, level, pos);
            else
                level.setBlock(pos, blockstate, 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
        Direction base = state.getValue(GSProperties.FACING_ALL);
        return canSupportCenter(level, pos.offset(base.getStepX(), base.getStepY(), base.getStepZ()), base);
    }

    /*
     * Methods from AbstractCandleBlock
     */

    @Override
    protected void onProjectileHit(Level level, @Nonnull BlockState state, @Nonnull BlockHitResult hit, @Nonnull Projectile projectile) {
        if (!level.isClientSide && projectile.isOnFire() && !state.getValue(WATERLOGGED)) {
            level.setBlock(hit.getBlockPos(), state.setValue(LIT, true), 11);
        }
    }

    @Override
    public void animateTick(BlockState state,@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (state.getValue(LIT)) {
            this.getParticleOffsets(state).forEach(vec3 -> addParticlesAndSound(level, vec3.add(pos.getX(), pos.getY(), pos.getZ()), random));
        }
    }

    private static void addParticlesAndSound(Level level, Vec3 offset, RandomSource random) {
        float f = random.nextFloat();
        if (f < 0.3F) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
            if (f < 0.17F) {
                level.playLocalSound(offset.x + 0.5, offset.y + 0.5, offset.z + 0.5, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
        }
        level.addParticle(ParticleTypes.SMALL_FLAME, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
    }

    public static void extinguish(@Nullable Player player, BlockState state, LevelAccessor level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LIT, false), 11);
        if (state.getBlock() instanceof CandelabraBlock candelabra) {
            candelabra.getParticleOffsets(state).forEach(vec3 -> level.addParticle(
                ParticleTypes.SMOKE, (double)pos.getX() + vec3.x(), (double)pos.getY() + vec3.y(), (double)pos.getZ() + vec3.z(), 0.0, 0.1F, 0.0)
            );
        }
        level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    protected void onExplosionHit(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Explosion explosion, @Nonnull BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (explosion.canTriggerBlocks() && state.getValue(LIT))
            extinguish(null, state, level, pos);
        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }
}

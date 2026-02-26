package voidsong.gasworks.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import voidsong.gasworks.common.block.properties.GSProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class UnlitWallTorchBlock extends UnlitTorchBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(
            ImmutableMap.of(
                    Direction.NORTH,
                    Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0),
                    Direction.SOUTH,
                    Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0),
                    Direction.WEST,
                    Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5),
                    Direction.EAST,
                    Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5)
            )
    );

    public UnlitWallTorchBlock(BlockBehaviour.Properties properties, boolean extinguishInRain) {
        super(properties, extinguishInRain);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(GSProperties.SMOLDERING, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, GSProperties.SMOLDERING, HorizontalDirectionalBlock.FACING);
    }

    @Override
    @Nonnull
    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    @Override
    @Nonnull
    protected VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return getShape(state);
    }

    public static VoxelShape getShape(BlockState state) {
        return AABBS.get(state.getValue(FACING));
    }

    @Override
    protected boolean canSurvive(BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        return WallTorchBlock.canSurvive(level, pos, state.getValue(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        Direction[] directions = context.getNearestLookingDirections();
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                state = state.setValue(FACING, direction1);
                if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                    return state.setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
                }
            }
        }

        return null;
    }

    @Override
    @Nonnull
    protected BlockState updateShape(BlockState state, @Nonnull Direction facing, @Nonnull BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return facing.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    @Nonnull
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if(!state.getValue(BlockStateProperties.WATERLOGGED) && state.getValue(GSProperties.SMOLDERING)) {
            Direction direction = state.getValue(FACING);
            double d0 = (double) pos.getX() + 0.5;
            double d1 = (double) pos.getY() + 0.7;
            double d2 = (double) pos.getZ() + 0.5;
            Direction direction1 = direction.getOpposite();
            level.addParticle(ParticleTypes.SMOKE, d0 + 0.27 * (double) direction1.getStepX(), d1 + 0.22, d2 + 0.27 * (double) direction1.getStepZ(), 0.0, 0.0, 0.0);
        }
    }
}

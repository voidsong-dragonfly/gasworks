package voidsong.gasworks.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import voidsong.gasworks.common.block.properties.GSProperties;
import voidsong.gasworks.common.util.BlockUtil;
import voidsong.gasworks.mixin.accessor.FlowingFluidAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UnlitTorchBlock extends BaseTorchBlock implements SimpleWaterloggedBlock {
    protected final boolean extinguishInRain;
    public static final MapCodec<UnlitTorchBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(propertiesCodec(), Codec.BOOL.fieldOf("extinguishInRain").forGetter(block -> block.extinguishInRain)).apply(instance, UnlitTorchBlock::new)
    );
    @Nonnull
    protected MapCodec<? extends BaseTorchBlock> codec() {
        return CODEC;
    }

    public UnlitTorchBlock(BlockBehaviour.Properties properties, boolean extinguishInRain) {
        super(properties);
        this.extinguishInRain = extinguishInRain;
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, false).setValue(GSProperties.SMOLDERING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, GSProperties.SMOLDERING);
    }

    @Override
    protected boolean isRandomlyTicking(@Nonnull BlockState state) {
        return state.getValue(GSProperties.SMOLDERING);
    }

    @Override
    protected void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (level.isRainingAt(pos) && level.isThundering() && extinguishInRain && state.getValue(GSProperties.SMOLDERING))
            BlockUtil.dowseTorch2(null, state, level, pos, false);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    @Nonnull
    protected BlockState updateShape(BlockState state, @Nonnull Direction facing, @Nonnull BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, neighborState, level, pos, neighborPos);
    }

    @Override
    @Nonnull
    protected FluidState getFluidState(@Nonnull BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if(!state.getValue(BlockStateProperties.WATERLOGGED) && state.getValue(GSProperties.SMOLDERING)) {
            double d0 = (double) pos.getX() + 0.5;
            double d1 = (double) pos.getY() + 0.7;
            double d2 = (double) pos.getZ() + 0.5;
            level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
        }
    }

    /*
     * The following methods are modified from SimpleWaterloggedBlock using code from FlowingFluid#spreadTo to allow
     * unlit torches to be popped off. It is functionally similar to FragileVanillaWaterloggedBlock without
     * explicitly for being used with Vanilla blocks
     */

    @Override
    public boolean placeLiquid(@Nonnull LevelAccessor level, @Nonnull BlockPos pos, BlockState state, @Nonnull FluidState fluidState) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED)) {
            if (fluidState.getType() == Fluids.WATER) {
                // This departs significantly because we have custom handling for waterlog-forcing
                BlockUtil.dowseTorch2(null, state, level, pos, true);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            // These exceptions to the placeLiquid code from SimpleWaterloggedBlock exists explicitly to allow torches
            // to be washed away, while also containing waterlogging, it is copied from FlowingFluid#spreadTo
            } else if (fluidState.getType() instanceof FlowingFluidAccessor flowingFluid) {
                flowingFluid.callBeforeDestroyingBlock(level, pos, state);
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            } else {
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Fluid fluid) {
        return true;
    }
}

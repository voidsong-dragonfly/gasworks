package voidsong.gasworks.mixin.waterlogging.basic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(WeightedPressurePlateBlock.class)
public class WeightedPressurePlateMixin  extends Block implements SimpleWaterloggedBlock {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public WeightedPressurePlateMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/WeightedPressurePlateBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
    private BlockState addWaterloggingToConstructor(BlockState defaultState) {
        return defaultState.setValue(BlockStateProperties.WATERLOGGED, false);
    }

    @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
    private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    /*
     * The methods below were copied & modified from CandleBlock, to provide waterlogging capabilities to other
     * blocks. These are generalized to remove references to lit & only add state-dependent waterlogging behavior
     */

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        BlockState toPlace = super.getStateForPlacement(context);
        return toPlace == null ? null : toPlace.setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    @Nonnull
    protected BlockState updateShape(@Nonnull BlockState updated, @Nonnull Direction direction, @Nonnull BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        if (updated.getValue(BlockStateProperties.WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(updated, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    @Nonnull
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}


package voidsong.gasworks.mixin.waterlogging;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.mixin.accessor.FlowingFluidAccessor;

import javax.annotation.Nonnull;

@Mixin(ButtonBlock.class)
public class ButtonMixin extends Block implements SimpleWaterloggedBlock {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public ButtonMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ButtonBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
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

    @Override
    public boolean placeLiquid(@Nonnull LevelAccessor level, @Nonnull BlockPos pos, BlockState state, @Nonnull FluidState fluidState) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED)) {
            if (fluidState.getType() == Fluids.WATER) {
                BlockState blockstate = state.setValue(BlockStateProperties.WATERLOGGED, true);
                level.setBlock(pos, blockstate, 3);
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

    /*
     * The method below is modified from SimpleWaterloggedBlock & LiquidBlockContainer to allow torches to still be
     * washed away by flowing water, while also containing waterlogging
     */

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Fluid fluid) {
        return true;
    }
}
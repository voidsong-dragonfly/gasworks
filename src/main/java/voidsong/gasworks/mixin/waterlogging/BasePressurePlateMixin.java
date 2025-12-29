package voidsong.gasworks.mixin.waterlogging;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
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

@Mixin(BasePressurePlateBlock.class)
public class BasePressurePlateMixin extends Block implements SimpleWaterloggedBlock {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public BasePressurePlateMixin(Properties properties) {
        super(properties);
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

    @ModifyReturnValue(method = "updateShape", at = @At(value = "RETURN"))
    private BlockState updateShape(BlockState updated, @Local(argsOnly = true) LevelAccessor level, @Local(ordinal = 0, argsOnly = true) BlockPos pos) {
        if (updated.hasProperty(BlockStateProperties.WATERLOGGED) && updated.getValue(BlockStateProperties.WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return updated;
    }

    @Override
    @Nonnull
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /*
     * We have these two mixins because WeightedPressurePlateBlock and PressurePlateBlock have different versions of
     * Block#createBlockStateDefinition, so we need to mix in to each separately
     */

    @SuppressWarnings("unused")
    @Mixin(WeightedPressurePlateBlock.class)
    public static class WeightedPressurePlateMixin {
        @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/WeightedPressurePlateBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
        private BlockState addWaterloggingToConstructor(BlockState defaultState) {
            return defaultState.setValue(BlockStateProperties.WATERLOGGED, false);
        }

        @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
        private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
            builder.add(BlockStateProperties.WATERLOGGED);
        }

    }

    @SuppressWarnings("unused")
    @Mixin(PressurePlateBlock.class)
    public static class PressurePlateMixin {
        @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/PressurePlateBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
        private BlockState addWaterloggingToConstructor(BlockState defaultState) {
            return defaultState.setValue(BlockStateProperties.WATERLOGGED, false);
        }

        @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
        private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
            builder.add(BlockStateProperties.WATERLOGGED);
        }

    }
}

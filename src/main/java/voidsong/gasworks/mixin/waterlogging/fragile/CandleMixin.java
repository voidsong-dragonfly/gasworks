package voidsong.gasworks.mixin.waterlogging.fragile;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import voidsong.gasworks.mixin.accessor.FlowingFluidAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(CandleBlock.class)
public class CandleMixin extends Block implements SimpleWaterloggedBlock {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public CandleMixin(Properties properties) {
        super(properties);
    }

    @ModifyReturnValue(method = "updateShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"))
    protected BlockState updateShape(BlockState original, @Local(argsOnly = true) Direction direction, @Local(argsOnly = true) LevelAccessor level, @Local(ordinal = 0, argsOnly = true) BlockPos pos) {
        return direction.equals(Direction.DOWN) && !original.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : original;
    }

    /*
     * The method below is modified from CandleBlock, SimpleWaterloggedBlock & LiquidBlockContainer to allow candles to
     * still be washed away by flowing water, while also containing waterlogging
     */

    @Inject(method = "placeLiquid", at = @At(value = "HEAD"), cancellable = true)
    private void placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if (!state.getValue(CandleBlock.WATERLOGGED) && fluidState.getType() != Fluids.WATER) {
            // These exceptions to the placeLiquid code from SimpleWaterloggedBlock exists explicitly to allow candles
            // to be washed away, while also containing waterlogging, it is copied from FlowingFluid#spreadTo
            if (fluidState.getType() instanceof FlowingFluidAccessor flowingFluid) {
                flowingFluid.callBeforeDestroyingBlock(level, pos, state);
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            } else {
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            }
            cir.setReturnValue(true);
        }
    }

    /*
     * The method below is modified from SimpleWaterloggedBlock & LiquidBlockContainer to allow candles to still be
     * washed away by flowing water, while also containing waterlogging
     */

    @Unique
    public boolean canPlaceLiquid(@Nullable Player player, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Fluid fluid) {
        return true;
    }
}

package voidsong.gasworks.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import voidsong.gasworks.common.block.interfaces.modifications.VanillaCapabilityBlock;
import voidsong.gasworks.common.block.interfaces.modifications.VanillaRandomTickBlock;
import voidsong.gasworks.common.block.interfaces.modifications.VanillaWaterloggedBlock;

@SuppressWarnings("unused")
public class BaseBlockInjections {
    @Mixin(Block.class)
    public static class BlockMixin {
        @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
        private BlockState addQuenchAndWaterloggingToConstructor(BlockState defaultState) {
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply()) {
                return block.gasworks$addStatesToDefaultState(defaultState);
            }
            return defaultState;
        }

        @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
        private BlockState getStateForPlacement(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
            if (original != null && this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply(false, true)) {
                return block.gasworks$modifyStateForPlacement(original, context);
            }
            return original;
        }
    }

    @Mixin(BlockBehaviour.class)
    public static abstract class BlockBehaviorMixin {
        @Shadow
        protected abstract Block asBlock();

        @Inject(method = "updateShape", at = @At(value = "RETURN"))
        private void updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
            // Waterlogging updates
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply(true, false)) {
                if (cir.getReturnValue().hasProperty(BlockStateProperties.WATERLOGGED) && cir.getReturnValue().getValue(BlockStateProperties.WATERLOGGED)) {
                    level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                }
            }
            // Capability block updates, in case we want to update caps on external block change
            if (this instanceof VanillaCapabilityBlock block && block.gasworks$shouldProcessCapabilities(this.asBlock(), true)) {
                block.gasworks$onUpdateShape(state, direction, neighborState, level, pos, neighborPos);
            }
        }

        @ModifyReturnValue(method = "getFluidState", at = @At(value = "RETURN"))
        private FluidState getFluidState(FluidState original, @Local(argsOnly = true) BlockState state) {
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply()) {
                if (original.isEmpty()) {
                    return block.gasworks$modifyGetFluidState(original, state);
                }
            }
            return original;
        }

        @ModifyReturnValue(method = "isRandomlyTicking", at = @At(value = "RETURN"))
        private boolean isRandomlyTicking(boolean original, @Local(argsOnly = true) BlockState state) {
            if (this instanceof VanillaRandomTickBlock block && block.gasworks$shouldRandomTickMixinApply()) {
                return block.gasworks$modifyIsRandomlyTicking(state, original);
            }
            return original;
        }

        @Inject(method = "randomTick", at = @At(value = "HEAD"))
        private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
            if (this instanceof VanillaRandomTickBlock block && block.gasworks$shouldRandomTickMixinApply()) {
                block.gasworks$divertRandomTick(state, level, pos, random);
            }
        }

        @Inject(method = "onPlace", at = @At(value = "HEAD"))
        private void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
            if (this instanceof VanillaCapabilityBlock block && block.gasworks$shouldProcessCapabilities(this.asBlock(), false)) {
                if (!oldState.is(this.asBlock())) level.invalidateCapabilities(pos);
            }
        }

        @Inject(method = "onRemove", at = @At(value = "HEAD"))
        private void onRemove(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
            if (this instanceof VanillaCapabilityBlock block && block.gasworks$shouldProcessCapabilities(this.asBlock(), false)) {
                if (!state.is(oldState.getBlock())) level.invalidateCapabilities(pos);
            }
        }
    }

    @Mixin(FaceAttachedHorizontalDirectionalBlock.class)
    public static class FaceAttachedHorizontalDirectionalBlockMixin {
        @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
        private BlockState getStateForPlacement(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
            if (original != null && this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply()) {
                return block.gasworks$modifyStateForPlacement(original, context);
            }
            return original;
        }

        @Inject(method = "updateShape", at = @At(value = "RETURN"))
        private void updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply()) {
                if (cir.getReturnValue().hasProperty(BlockStateProperties.WATERLOGGED) && cir.getReturnValue().getValue(BlockStateProperties.WATERLOGGED)) {
                    level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                }
            }
        }

    }
}

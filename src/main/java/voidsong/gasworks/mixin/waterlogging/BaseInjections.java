package voidsong.gasworks.mixin.waterlogging;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import voidsong.gasworks.common.block.interfaces.VanillaRandomTickBlock;
import voidsong.gasworks.common.block.interfaces.VanillaWaterloggedBlock;

@SuppressWarnings("unused")
public class BaseInjections {
    @Mixin(Block.class)
    public static class BlockMixin {
        @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
        private BlockState addQuenchAndWaterloggingToConstructor(BlockState defaultState) {
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply(this.getClass())) {
                return block.gasworks$addStatesToDefaultState(defaultState);
            }
            return defaultState;
        }

        @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
        private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply(this.getClass())) {
                block.gasworks$addStatesToBlockStateDefinition(builder);
            }
        }


        @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
        private BlockState getStateForPlacement(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply(this.getClass())) {
                return block.gasworks$modifyStateForPlacement(original, context);
            }
            return original;
        }
    }

    @Mixin(BlockBehaviour.class)
    public static class BlockBehaviorMixin {
        @Inject(method = "updateShape", at = @At(value = "RETURN"))
        private void updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply(this.getClass())) {
                if (cir.getReturnValue().hasProperty(BlockStateProperties.WATERLOGGED) && cir.getReturnValue().getValue(BlockStateProperties.WATERLOGGED)) {
                    level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                }
            }
        }

        @ModifyReturnValue(method = "getFluidState", at = @At(value = "RETURN"))
        private FluidState getFluidState(FluidState original, @Local(argsOnly = true) BlockState state) {
            if (this instanceof VanillaWaterloggedBlock block && block.gasworks$shouldWaterlogMixinApply(this.getClass())) {
                if (original.isEmpty()) {
                    return block.gasworks$modifyGetFluidState(original, state);
                }
            }
            return original;
        }

        @ModifyReturnValue(method = "isRandomlyTicking", at = @At(value = "RETURN"))
        private boolean isRandomlyTicking(boolean original, @Local(argsOnly = true) BlockState state) {
            if (this instanceof VanillaRandomTickBlock block && block.gasworks$shouldRandomTickMixinApply(this.getClass())) {
                return block.gasworks$modifyIsRandomlyTicking(state, original);
            }
            return original;
        }

        @Inject(method = "randomTick", at = @At(value = "RETURN"))
        private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
            if (this instanceof VanillaRandomTickBlock block && block.gasworks$shouldRandomTickMixinApply(this.getClass())) {
                block.gasworks$divertRandomTick(state, level, pos, random);
            }
        }
    }
}

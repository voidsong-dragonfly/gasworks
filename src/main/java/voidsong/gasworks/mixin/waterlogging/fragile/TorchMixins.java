package voidsong.gasworks.mixin.waterlogging.fragile;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.api.GSTags;
import voidsong.gasworks.common.block.interfaces.modifications.VanillaRandomTickBlock;
import voidsong.gasworks.common.util.BlockUtil;
import voidsong.gasworks.mixin.accessor.FlowingFluidAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class TorchMixins {
    @Mixin(TorchBlock.class)
    public static class TorchBlockMixin implements VanillaRandomTickBlock, LiquidBlockContainer {
        /*
         * These are to ensure we know what classes the changes should be applied to;
         */
        @Override
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        public boolean gasworks$shouldRandomTickMixinApply() {
            return this.getClass().equals(TorchBlock.class) || this.getClass().equals(WallTorchBlock.class);
        }

        @Override
        public boolean gasworks$modifyIsRandomlyTicking(BlockState state, Boolean ticking) {
            return state.is(GSTags.Blocks.DOWSE_IN_RAIN);
        }

        @Override
        public void gasworks$divertRandomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
            if (level.isRainingAt(pos))
                BlockUtil.dowseTorch2(null, state, level, pos, false);
        }

        @Unique
        @Override
        public boolean placeLiquid(@Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull FluidState fluidState) {
            if (fluidState.getType() == Fluids.WATER) {
                // Use BlockUtils#dowseTorch to handle the repetitive action
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
        }

        @Unique
        @Override
        public boolean canPlaceLiquid(@Nullable Player player, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Fluid fluid) {
            return true;
        }
    }

    @Mixin(WallTorchBlock.class)
    public static class WallTorchMixin {
        /*
         * WallTorchBlock overrides two methods in TorchBlock without calling their super; we need to re-override these
         * to be the correct values to have the correct behavior; filtering for the common ones can be seen above
         */
        @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
        private BlockState getStateForPlacement(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
            if (original != null && original.is(GSTags.Blocks.DOWSE_IN_WATER)) {
                Level level = context.getLevel();
                BlockPos pos = context.getClickedPos();
                if (level.getFluidState(pos).getType() == Fluids.WATER) {
                    level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return BlockUtil.getDowsedTorchState(original, true);
                }
            }
            return original;
        }
    }

    /*
     * The methods below are for torch-specific behavior it does not make sense to include in the generic classes,
     * mixing into IBlockExtension, Block, and BlockBehavior. We do BlockStateDefinition registry here for torches, as
     * they do not have a method we can inject into, unlike most blocks.
     */

    @Mixin(Block.class)
    public static class BlockMixin {
        @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
        private BlockState getStateForPlacement(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
            if (original != null && original.is(GSTags.Blocks.DOWSE_IN_WATER)) {
                Level level = context.getLevel();
                BlockPos pos = context.getClickedPos();
                if (level.getFluidState(pos).getType() == Fluids.WATER) {
                    level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return BlockUtil.getDowsedTorchState(original, true);
                }
            }
            return original;
        }
    }

    @Mixin(IBlockExtension.class)
    public interface BlockExtensionMixin {
        @ModifyReturnValue(method = "getLightEmission", at = @At(value = "RETURN"))
        private int getLightEmission(int original, @Local(argsOnly = true) BlockState state) {
            return state.is(GSTags.Blocks.LOW_LIGHT_TORCHES) ? 10 : original;
        }
    }

    @Mixin(BlockBehaviour.class)
    public static class BlockBehaviorMixin {
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @Inject(method = "onExplosionHit", at = @At(value = "HEAD"))
        private void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer, CallbackInfo ci) {
            if ((this.getClass().equals(TorchBlock.class) || this.getClass().equals(WallTorchBlock.class)) && explosion.canTriggerBlocks()) {
                BlockUtil.dowseTorch2(null, state, level, pos, false);
            }
        }
    }
}

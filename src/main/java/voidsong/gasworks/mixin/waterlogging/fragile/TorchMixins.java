package voidsong.gasworks.mixin.waterlogging.fragile;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.api.GSTags;
import voidsong.gasworks.common.block.interfaces.FragileVanillaWaterloggedBlock;
import voidsong.gasworks.common.block.interfaces.VanillaRandomTickBlock;
import voidsong.gasworks.common.block.properties.GSProperties;
import voidsong.gasworks.common.util.BlockUtil;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class TorchMixins {
    @Mixin(TorchBlock.class)
    public static class TorchBlockMixin implements FragileVanillaWaterloggedBlock, VanillaRandomTickBlock {
        /*
         * These are to ensure we know what classes the changes should be applied to
         */
        @Override
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        public boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz) {
            return this.getClass().equals(TorchBlock.class) || this.getClass().equals(WallTorchBlock.class);
        }

        @Override
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        public boolean gasworks$shouldRandomTickMixinApply(Class<?> clazz) {
            return this.getClass().equals(TorchBlock.class) || this.getClass().equals(WallTorchBlock.class);
        }

        /*
         * Below is the actual class method override implementation
         */
        @Override
        public BlockState gasworks$addStatesToDefaultState(BlockState state) {
            BlockState lit = state.setValue(GSProperties.LIT, true);
            return state.hasProperty(BlockStateProperties.WATERLOGGED) ? lit : lit.setValue(BlockStateProperties.WATERLOGGED, false);
        }

        @Override
        public List<Property<?>> gasworks$newStatesForStateDefinition() {
            return List.of(BlockStateProperties.WATERLOGGED, GSProperties.LIT);
        }

        @Override
        public boolean gasworks$modifyIsRandomlyTicking(BlockState state, Boolean ticking) {
            return ticking || (state.is(GSTags.BlockTags.DOWSE_IN_RAIN) && state.getValue(GSProperties.LIT));
        }

        @Override
        public void gasworks$divertRandomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
            if (level.isRainingAt(pos))
                BlockUtil.dowseTorch(null, state, level, pos, true);
        }

        @Inject(method = "animateTick", at = @At(value = "HEAD"), cancellable = true)
        private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
            if (state.getValue(BlockStateProperties.WATERLOGGED))
                ci.cancel();
            else if (!state.getValue(GSProperties.LIT)) {
                double d0 = (double) pos.getX() + 0.5;
                double d1 = (double) pos.getY() + 0.7;
                double d2 = (double) pos.getZ() + 0.5;
                level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
                ci.cancel();
            }
        }

        @Override
        public BlockState gasworks$modifyStateForPlacement(BlockState original, BlockPlaceContext context) {
            FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
            boolean waterlogged = fluid.getType() == Fluids.WATER;
            return original == null ? null : original.setValue(BlockStateProperties.WATERLOGGED, waterlogged).setValue(GSProperties.LIT, !(waterlogged && original.is(GSTags.BlockTags.DOWSE_IN_WATER)));
        }

        @Override
        public boolean gasworks$handleWaterlogPlacement(@Nonnull BlockState before, @Nonnull BlockState after, @Nonnull BlockPos pos, @Nonnull LevelAccessor level) {
            if (before.getValue(GSProperties.LIT) && after.getValue(BlockStateProperties.WATERLOGGED)) {
                BlockUtil.dowseTorch(null, after, level, pos, false);
            }
            return true;
        }
    }

    @Mixin(WallTorchBlock.class)
    public static class WallTorchMixin {
        /*
         * WallTorchBlock overrides three methods in TorchBlock without calling their super; we need to re-override these
         * to be the correct values to have the correct behavior; I have decided that this is better than trying to
         * re-apply some of the interfaces here
         */
        @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
        private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
            builder.add(GSProperties.LIT, BlockStateProperties.WATERLOGGED);
        }

        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
        private BlockState getStateForPlacement(BlockState previous, @Local(argsOnly = true) BlockPlaceContext context) {
            FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
            boolean waterlogged = fluid.getType() == Fluids.WATER;
            if (previous != null && this.getClass().equals(WallTorchBlock.class))
                return previous.setValue(BlockStateProperties.WATERLOGGED, waterlogged).setValue(GSProperties.LIT, !(waterlogged && previous.is(GSTags.BlockTags.DOWSE_IN_WATER)));
            return previous;
        }

        @ModifyReturnValue(method = "updateShape", at = @At(value = "RETURN"))
        private BlockState updateShape(BlockState updated, @Local(argsOnly = true) LevelAccessor level, @Local(ordinal = 0, argsOnly = true) BlockPos pos) {
            if (updated.hasProperty(BlockStateProperties.WATERLOGGED) && updated.getValue(BlockStateProperties.WATERLOGGED))
                level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            return updated;
        }

        @Inject(method = "animateTick", at = @At(value = "HEAD"), cancellable = true)
        private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
            if(state.getValue(BlockStateProperties.WATERLOGGED))
                ci.cancel();
            else if (!state.getValue(GSProperties.LIT)) {
                Direction direction = state.getValue(WallTorchBlock.FACING);
                double d0 = (double)pos.getX() + 0.5;
                double d1 = (double)pos.getY() + 0.7;
                double d2 = (double)pos.getZ() + 0.5;
                Direction direction1 = direction.getOpposite();
                level.addParticle(
                    ParticleTypes.SMOKE, d0 + 0.27 * (double)direction1.getStepX(), d1 + 0.22, d2 + 0.27 * (double)direction1.getStepZ(), 0.0, 0.0, 0.0
                );
                ci.cancel();
            }
        }
    }

    @Mixin(IBlockExtension.class)
    public interface BlockExtensionMixin {
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @ModifyReturnValue(method = "getLightEmission", at = @At(value = "RETURN"))
        private int getLightEmission(int original, @Local(argsOnly = true) BlockState state) {
            if (this.getClass().equals(TorchBlock.class) || this.getClass().equals(WallTorchBlock.class)) {
                return state.is(GSTags.BlockTags.LOW_LIGHT_TORCHES) ? (state.getValue(GSProperties.LIT) ? 10 : 0) : original;
            }
            return original;
        }

        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @ModifyReturnValue(method = "getToolModifiedState", at = @At(value = "RETURN"))
        private BlockState getToolModifiedState(BlockState modified, @Local(argsOnly = true) BlockState state) {
            if (this.getClass().equals(TorchBlock.class) || this.getClass().equals(WallTorchBlock.class)) {
                if (ItemAbility.getActions().contains(ItemAbilities.FIRESTARTER_LIGHT) && (!state.getValue(GSProperties.LIT) && !state.getValue(BlockStateProperties.WATERLOGGED))) {
                    return state.setValue(GSProperties.LIT, true);
                }
            }
            return modified;
        }
    }

    @Mixin(BlockBehaviour.class)
    public static class BlockBehaviorMixin {
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @Inject(method = "onExplosionHit", at = @At(value = "HEAD"))
        private void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer, CallbackInfo ci) {
            if (this.getClass().equals(TorchBlock.class) || this.getClass().equals(WallTorchBlock.class)) {
                if (explosion.canTriggerBlocks() && state.getValue(GSProperties.LIT)) {
                    BlockUtil.dowseTorch(null, state, level, pos, true);
                }
            }
        }

        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @ModifyReturnValue(method = "useWithoutItem", at = @At(value = "RETURN"))
        private InteractionResult useWithoutItem(InteractionResult original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos pos) {
            if (this.getClass().equals(TorchBlock.class) || this.getClass().equals(WallTorchBlock.class)) {boolean lit = state.getValue(GSProperties.LIT);
                boolean waterlogged = state.getValue(BlockStateProperties.WATERLOGGED);
                if (!lit && !waterlogged)
                    level.setBlockAndUpdate(pos, state.setValue(GSProperties.LIT, true));
                return lit ? InteractionResult.PASS : (waterlogged ? InteractionResult.FAIL : InteractionResult.SUCCESS);
            }
            return original;
        }
    }
}

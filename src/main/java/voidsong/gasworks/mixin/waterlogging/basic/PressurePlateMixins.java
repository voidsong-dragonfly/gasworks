package voidsong.gasworks.mixin.waterlogging.basic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import voidsong.gasworks.common.block.interfaces.VanillaWaterloggedBlock;

@SuppressWarnings("unused")
public class PressurePlateMixins {
    @Mixin(PressurePlateBlock.class)
    public static class PressurePlateMixin implements VanillaWaterloggedBlock {
        @Override
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        public boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz, boolean updateShapeOverride, boolean getStateForPlacementOverride) {
            return this.getClass().equals(PressurePlateBlock.class) && !updateShapeOverride;
        }

        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
        private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
            if(this.getClass().equals(PressurePlateBlock.class)) builder.add(BlockStateProperties.WATERLOGGED);
        }
    }

    @Mixin(WeightedPressurePlateBlock.class)
    public static class WeightedPressurePlateMixin implements VanillaWaterloggedBlock {
        @Override
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        public boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz, boolean updateShapeOverride, boolean getStateForPlacementOverride) {
            return this.getClass().equals(WeightedPressurePlateBlock.class) && !updateShapeOverride;
        }

        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
        private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
            if(this.getClass().equals(WeightedPressurePlateBlock.class)) builder.add(BlockStateProperties.WATERLOGGED);
        }
    }

    @Mixin(BasePressurePlateBlock.class)
    public static class BasePressurePlateBlockMixin {
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @Inject(method = "updateShape", at = @At(value = "RETURN"))
        private void updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
            if (this instanceof VanillaWaterloggedBlock && (this.getClass().equals(PressurePlateBlock.class) || this.getClass().equals(WeightedPressurePlateBlock.class))) {
                if (cir.getReturnValue().hasProperty(BlockStateProperties.WATERLOGGED) && cir.getReturnValue().getValue(BlockStateProperties.WATERLOGGED)) {
                    level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                }
            }
        }
    }
}

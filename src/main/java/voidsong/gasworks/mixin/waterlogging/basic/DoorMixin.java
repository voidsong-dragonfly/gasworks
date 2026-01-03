package voidsong.gasworks.mixin.waterlogging.basic;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
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
import voidsong.gasworks.common.block.interfaces.VanillaWaterloggedBlock;

@Mixin(DoorBlock.class)
public class DoorMixin implements VanillaWaterloggedBlock {
    @Override
    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    public boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz, boolean updateShapeOverride, boolean getStateForPlacementOverride) {
        return this.getClass().equals(DoorBlock.class) && !(updateShapeOverride || getStateForPlacementOverride);
    }

    @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
    private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
    private BlockState getStateForPlacement(BlockState toPlace, @Local(argsOnly = true) BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluid.getType() == Fluids.WATER;
        return toPlace == null ? null : toPlace.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    @ModifyReturnValue(method = "updateShape", at = @At(value = "RETURN"))
    private BlockState updateShape(BlockState updated, @Local(argsOnly = true) LevelAccessor level, @Local(ordinal = 0, argsOnly = true) BlockPos pos, @Local(ordinal = 0, argsOnly = true) BlockState state) {
        // Standard update code common to all waterlogged blocks
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        // Code to ensure we don't duplicate or erase water source blocks; specific to DoorBlock(s)
        if (this.getClass().equals(DoorBlock.class) && state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            if (updated.isAir())
                return state.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : updated;
            else
                return updated.setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
        }
        // Return pass-through otherwise
        return updated;
    }

    @ModifyArg(method = "setPlacedBy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), index = 1)
    private BlockState setPlacedBy(BlockState defaultState, @Local(ordinal = 0, argsOnly = true) BlockPos pos, @Local(ordinal = 0, argsOnly = true) Level level) {
        boolean waterloggedTop = level.getFluidState(pos.above()).is(Fluids.WATER);
        return defaultState.hasProperty(BlockStateProperties.WATERLOGGED) ? defaultState.setValue(BlockStateProperties.WATERLOGGED, waterloggedTop) : defaultState;
    }
}
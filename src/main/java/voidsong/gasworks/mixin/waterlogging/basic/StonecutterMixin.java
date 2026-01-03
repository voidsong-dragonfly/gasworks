package voidsong.gasworks.mixin.waterlogging.basic;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.common.block.interfaces.VanillaWaterloggedBlock;

@Mixin(StonecutterBlock.class)
public class StonecutterMixin implements VanillaWaterloggedBlock {
    @Override
    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    public boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz, boolean getShapeOverride, boolean getStateForPlacementOverride) {
        return this.getClass().equals(StonecutterBlock.class) && !getStateForPlacementOverride;
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
}
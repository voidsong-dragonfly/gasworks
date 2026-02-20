package voidsong.gasworks.mixin.waterlogging.basic;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HopperBlock;
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

@Mixin(HopperBlock.class)
public class HopperMixin implements VanillaWaterloggedBlock {
    @Override
    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    public boolean gasworks$shouldWaterlogMixinApply(boolean updateShapeOverride, boolean getStateForPlacementOverride) {
        return this.getClass().equals(HopperBlock.class) && !getStateForPlacementOverride;
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/HopperBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
    private BlockState addWaterloggingToConstructor(BlockState defaultState) {
        return gasworks$shouldWaterlogMixinApply() ? gasworks$addStatesToDefaultState(defaultState) : defaultState;
    }

    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
    private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if(this.getClass().equals(HopperBlock.class)) builder.add(BlockStateProperties.WATERLOGGED);
    }

    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
    private BlockState getStateForPlacement(BlockState place, @Local(argsOnly = true) BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluid.getType() == Fluids.WATER && place.hasProperty(BlockStateProperties.WATERLOGGED);
        return (this.getClass().equals(HopperBlock.class) && place != null) ? place.setValue(BlockStateProperties.WATERLOGGED, waterlogged) : place;
    }
}
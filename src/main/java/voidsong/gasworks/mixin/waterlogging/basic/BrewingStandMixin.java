package voidsong.gasworks.mixin.waterlogging.basic;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.common.block.interfaces.VanillaWaterloggedBlock;

@Mixin(BrewingStandBlock.class)
public class BrewingStandMixin implements VanillaWaterloggedBlock {
    @Override
    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    public boolean gasworks$shouldWaterlogMixinApply(boolean updateShapeOverride, boolean getStateForPlacementOverride) {
        return this.getClass().equals(BrewingStandBlock.class);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BrewingStandBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
    private BlockState addWaterloggingToConstructor(BlockState defaultState) {
        return gasworks$shouldWaterlogMixinApply() ? gasworks$addStatesToDefaultState(defaultState) : defaultState;
    }

    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
    private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if(this.getClass().equals(BrewingStandBlock.class)) builder.add(BlockStateProperties.WATERLOGGED);
    }
}
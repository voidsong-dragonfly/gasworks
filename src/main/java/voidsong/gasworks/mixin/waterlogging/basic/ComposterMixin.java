package voidsong.gasworks.mixin.waterlogging.basic;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.common.block.interfaces.VanillaWaterloggedBlock;

@Mixin(ComposterBlock.class)
public class ComposterMixin implements VanillaWaterloggedBlock {
    @Override
    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    public boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz, boolean updateShapeOverride, boolean getStateForPlacementOverride) {
        return this.getClass().equals(ComposterBlock.class);
    }

    @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
    private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(BlockStateProperties.WATERLOGGED);
    }
}
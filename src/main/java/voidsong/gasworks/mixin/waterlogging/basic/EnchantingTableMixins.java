package voidsong.gasworks.mixin.waterlogging.basic;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.common.block.interfaces.VanillaWaterloggedBlock;

@SuppressWarnings("unused")
public class EnchantingTableMixins {
    @Mixin(EnchantingTableBlock.class)
    public static class EnchantingTableMixin implements VanillaWaterloggedBlock {
        @Override
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        public boolean gasworks$shouldWaterlogMixinApply(Class<?> clazz, boolean getShapeOverride, boolean getStateForPlacementOverride) {
            return this.getClass().equals(EnchantingTableBlock.class);
        }
    }

    @Mixin(Block.class)
    public static class BlockMixin {
        @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
        @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
        private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
            if (this instanceof VanillaWaterloggedBlock block && this.getClass().equals(EnchantingTableBlock.class)) {
                builder.add(BlockStateProperties.WATERLOGGED);
            }
        }
    }
}
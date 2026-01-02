package voidsong.gasworks.mixin.waterlogging.fragile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.common.block.properties.GSProperties;


@Mixin(WallTorchBlock.class)
public class WallTorchMixin {
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

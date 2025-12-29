package voidsong.gasworks.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.api.GSTags;
import voidsong.gasworks.common.block.properties.GSProperties;


@Mixin(WallTorchBlock.class)
public class WallTorchMixin extends Block implements SimpleWaterloggedBlock {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public WallTorchMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/WallTorchBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
    private BlockState useCorrectHeightmapType(BlockState defaultState) {
        return defaultState.setValue(GSProperties.LIT, true).setValue(BlockStateProperties.WATERLOGGED, false);
    }

    @Inject(method = "createBlockStateDefinition", at = @At(value = "RETURN"))
    private void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(GSProperties.LIT, BlockStateProperties.WATERLOGGED);
    }

    @Unique
    @SuppressWarnings("all")
    private static boolean canBeLit(BlockState state) {
        return !state.getValue(GSProperties.LIT) && !state.getValue(BlockStateProperties.WATERLOGGED);
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

    /*
     * The methods below were copied & modified from AbstractCandleBlock, to give torches similar behaviors to candles.
     * These relate to waterlogging;  they have been modified to not overwrite but instead tack onto the default methods
     */

    @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
    private BlockState getStateForPlacement(BlockState original, @Local(argsOnly = true) BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluid.getType() == Fluids.WATER;
        return original == null ? null : original.setValue(BlockStateProperties.WATERLOGGED, waterlogged).setValue(GSProperties.LIT, !(waterlogged && original.is(GSTags.BlockTags.DOWSE_IN_WATER)));
    }

    @ModifyReturnValue(method = "updateShape", at = @At(value = "RETURN"))
    private BlockState updateShape(BlockState original, @Local(argsOnly = true) LevelAccessor level, @Local(ordinal = 0, argsOnly = true) BlockPos pos) {
        if (original.hasProperty(BlockStateProperties.WATERLOGGED) && original.getValue(BlockStateProperties.WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return original;
    }
}

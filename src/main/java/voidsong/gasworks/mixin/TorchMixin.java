package voidsong.gasworks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.api.GSTags;
import voidsong.gasworks.common.block.properties.GSProperties;
import voidsong.gasworks.common.util.BlockUtil;
import voidsong.gasworks.mixin.accessor.FlowingFluidAccessor;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

@Mixin(TorchBlock.class)
public class TorchMixin extends Block implements SimpleWaterloggedBlock {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public TorchMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void addQuenchAndWaterloggingToConstructor(SimpleParticleType flameParticle, BlockBehaviour.Properties properties, CallbackInfo ci) {
        registerDefaultState(this.stateDefinition.any().setValue(GSProperties.LIT, true).setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GSProperties.LIT, BlockStateProperties.WATERLOGGED);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.is(GSTags.BlockTags.DOWSE_IN_RAIN) && state.getValue(GSProperties.LIT);
    }

    @Override
    protected void randomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (level.isRainingAt(pos))
            BlockUtil.dowseTorch(null, state, level, pos, true);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightEmission(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return state.is(GSTags.BlockTags.LOW_LIGHT_TORCHES) ? (state.getValue(GSProperties.LIT) ? 10 : 0) : state.getLightEmission();
    }

    @Override
    public @Nullable BlockState getToolModifiedState(@Nonnull BlockState state, @Nonnull UseOnContext context, @Nonnull ItemAbility itemAbility, boolean simulate) {
        if (ItemAbility.getActions().contains(ItemAbilities.FIRESTARTER_LIGHT) && canBeLit(state))
            return state.setValue(GSProperties.LIT, true);
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    @Override
    @Nonnull
    protected InteractionResult useWithoutItem(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
        boolean lit = state.getValue(GSProperties.LIT);
        boolean waterlogged = state.getValue(BlockStateProperties.WATERLOGGED);
        if (!lit && !waterlogged)
            level.setBlockAndUpdate(pos, state.setValue(GSProperties.LIT, true));
        return lit ? InteractionResult.PASS : (waterlogged ? InteractionResult.FAIL : InteractionResult.SUCCESS);
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
            double d0 = (double)pos.getX() + 0.5;
            double d1 = (double)pos.getY() + 0.7;
            double d2 = (double)pos.getZ() + 0.5;
            level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            ci.cancel();
        }
    }

    /*
     * I'm not 100% on whether this method is effective, but it's copied from BurnableFuelBlock
     */

    @Override
    public void onCaughtFire(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        if (canBeLit(state))
            level.setBlockAndUpdate(pos, state.setValue(GSProperties.LIT, true));
        super.onCaughtFire(state, level, pos, direction, igniter);
    }

    /*
     * The methods below were copied from AbstractCandleBlock, to give torches similar behaviors to candles.
     * While torches are not the same as candles, they should be lit and unlit by the same non-player mechanisms. However,
     * torches do not have a hitbox and thus cannot be lit by on-fire projectiles. Most of these relate to waterlogging
     */

    @Override
    protected void onExplosionHit(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Explosion explosion, @Nonnull BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (explosion.canTriggerBlocks() && state.getValue(GSProperties.LIT))
            BlockUtil.dowseTorch(null, state, level, pos, true);
        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        BlockState state = super.getStateForPlacement(context);
        boolean waterlogged = fluid.getType() == Fluids.WATER;
        return state == null ? null : state.setValue(BlockStateProperties.WATERLOGGED, waterlogged).setValue(GSProperties.LIT, !(waterlogged && state.is(GSTags.BlockTags.DOWSE_IN_WATER)));
    }

    @Override
    @Nonnull
    protected BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction direction, @Nonnull BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED))
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    @Nonnull
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(@Nonnull LevelAccessor level, @Nonnull BlockPos pos, BlockState state, @Nonnull FluidState fluidState) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED)) {
            if(fluidState.getType() == Fluids.WATER) {
                BlockState blockstate = state.setValue(BlockStateProperties.WATERLOGGED, true);
                if (state.getValue(GSProperties.LIT))
                    BlockUtil.dowseTorch(null, blockstate, level, pos, false);
                else
                    level.setBlock(pos, blockstate, 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            // These exceptions to the placeLiquid code from SimpleWaterloggedBlock exists explicitly to allow torches
            // to be washed away, while also containing waterlogging, it is copied from FlowingFluid#spreadTo
            } else if (fluidState.getType() instanceof FlowingFluidAccessor flowingFluid) {
                flowingFluid.callBeforeDestroyingBlock(level, pos, state);
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            } else {
                level.setBlock(pos, fluidState.createLegacyBlock(), 3);
            }
            return true;
        } else {
            return false;
        }
    }

    /*
     * The method below is modified from SimpleWaterloggedBlock & LiquidBlockContainer to allow torches to still be
     * washed away by flowing water, while also containing waterlogging
     */

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Fluid fluid) {
        return true;
    }
}

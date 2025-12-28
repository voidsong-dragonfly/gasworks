package voidsong.gasworks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.common.block.properties.GSProperties;

import javax.annotation.Nonnull;

@Mixin(TorchBlock.class)
public class TorchMixin extends Block {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public TorchMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void addQuenchToConstructor(SimpleParticleType flameParticle, BlockBehaviour.Properties properties, CallbackInfo ci) {
        registerDefaultState(this.stateDefinition.any().setValue(GSProperties.LIT, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GSProperties.LIT);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(GSProperties.LIT) && (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH));
    }

    @Override
    protected void randomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (level.isRainingAt(pos))
            level.setBlockAndUpdate(pos, state.setValue(GSProperties.LIT, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightEmission(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)) ? (state.getValue(GSProperties.LIT) ? 10 : 1) : state.getLightEmission();
    }

    @Override
    public @Nullable BlockState getToolModifiedState(@Nonnull BlockState state, @Nonnull UseOnContext context, @Nonnull ItemAbility itemAbility, boolean simulate) {
        return ItemAbility.getActions().contains(ItemAbilities.FIRESTARTER_LIGHT) ? state.setValue(GSProperties.LIT, true) : super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    @Override
    @Nonnull
    protected InteractionResult useWithoutItem(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
        boolean lit = state.getValue(GSProperties.LIT);
        if (!lit)
            level.setBlockAndUpdate(pos, state.setValue(GSProperties.LIT, true));
        return lit ? InteractionResult.PASS : InteractionResult.SUCCESS;
    }

    @Override
    public void onCaughtFire(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        if (!state.getValue(GSProperties.LIT))
            level.setBlockAndUpdate(pos, state.setValue(GSProperties.LIT, true));
        super.onCaughtFire(state, level, pos, direction, igniter);
    }
}

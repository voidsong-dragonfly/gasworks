package voidsong.gasworks.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseTorchBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import voidsong.gasworks.common.block.interfaces.FragileWaterloggedBlock;
import voidsong.gasworks.common.block.properties.GSProperties;
import voidsong.gasworks.common.util.BlockUtil;

import javax.annotation.Nonnull;

public class UnlitTorchBlock extends BaseTorchBlock implements FragileWaterloggedBlock {
    protected final boolean extinguishInRain;
    public static final MapCodec<UnlitTorchBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(propertiesCodec(), Codec.BOOL.fieldOf("extinguishInRain").forGetter(block -> block.extinguishInRain)).apply(instance, UnlitTorchBlock::new)
    );
    @Nonnull
    protected MapCodec<? extends BaseTorchBlock> codec() {
        return CODEC;
    }

    public UnlitTorchBlock(BlockBehaviour.Properties properties, boolean extinguishInRain) {
        super(properties);
        this.extinguishInRain = extinguishInRain;
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, false).setValue(GSProperties.SMOLDERING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, GSProperties.SMOLDERING);
    }

    @Override
    protected boolean isRandomlyTicking(@Nonnull BlockState state) {
        return state.getValue(GSProperties.SMOLDERING);
    }

    @Override
    protected void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (level.isRainingAt(pos) && level.isThundering() && extinguishInRain && state.getValue(GSProperties.SMOLDERING))
            BlockUtil.dowseTorch2(null, state, level, pos, false);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    @Nonnull
    protected BlockState updateShape(BlockState state, @Nonnull Direction facing, @Nonnull BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, neighborState, level, pos, neighborPos);
    }

    @Override
    @Nonnull
    protected FluidState getFluidState(@Nonnull BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    @Nonnull
    protected ItemInteractionResult useItemOn(ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty() && state.getValue(GSProperties.SMOLDERING) && !state.getValue(BlockStateProperties.WATERLOGGED)) {
            if (!level.isClientSide()) {
                if (level.random.nextInt(4) == 0) {
                    level.setBlockAndUpdate(pos, BlockUtil.getLitTorchState(state));
                    level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.0625F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                } else {
                    level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockState getToolModifiedState(@Nonnull BlockState state, @Nonnull UseOnContext context, @Nonnull ItemAbility itemAbility, boolean simulate) {
        if (ItemAbility.getActions().contains(ItemAbilities.FIRESTARTER_LIGHT) && !state.getValue(BlockStateProperties.WATERLOGGED)) {
            return BlockUtil.getLitTorchState(state);
        }
        return null;
    }

    @Override
    public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if(!state.getValue(BlockStateProperties.WATERLOGGED) && state.getValue(GSProperties.SMOLDERING)) {
            double d0 = (double) pos.getX() + 0.5;
            double d1 = (double) pos.getY() + 0.7;
            double d2 = (double) pos.getZ() + 0.5;
            level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
        }
    }
}

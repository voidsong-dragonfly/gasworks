package voidsong.gasworks.mixin.tweak;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import voidsong.gasworks.common.block.properties.GSProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(CampfireBlock.class)
public class CampfireMixin extends Block {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public CampfireMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(CampfireBlock.LIT) && state.is(Blocks.CAMPFIRE);
    }

    @Override
    protected void randomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (level.isRainingAt(pos.above()) && random.nextInt(2) == 0) {
            if (!level.isClientSide())
                level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
            CampfireBlock.dowse(null, level, pos, state);
            level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
        }
    }

    /*
     * I'm not 100% on whether this method is effective, but it's copied from BurnableFuelBlock
     */

    @Override
    public void onCaughtFire(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        if (!state.getValue(GSProperties.LIT))
            level.setBlockAndUpdate(pos, state.setValue(GSProperties.LIT, true));
        super.onCaughtFire(state, level, pos, direction, igniter);
    }
}

package voidsong.gasworks.mixin.tweak;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import voidsong.gasworks.common.block.interfaces.modifications.VanillaRandomTickBlock;

import javax.annotation.Nonnull;

@Mixin(CampfireBlock.class)
public class CampfireMixin extends Block implements VanillaRandomTickBlock {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param properties ignored & should not be used!
     */
    public CampfireMixin(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    public boolean gasworks$shouldRandomTickMixinApply() {
        return this.getClass().equals(CampfireBlock.class);
    }

    @Override
    public boolean gasworks$modifyIsRandomlyTicking(BlockState state, Boolean ticking) {
        return ticking || state.getValue(CampfireBlock.LIT) && state.is(Blocks.CAMPFIRE);
    }

    @Override
    public void gasworks$divertRandomTick(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        if (level.isRainingAt(pos.above()) && random.nextInt(2) == 0) {
            if (!level.isClientSide())
                level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
            CampfireBlock.dowse(null, level, pos, state);
            level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
        }
    }

    @ModifyReturnValue(method = "getStateForPlacement", at = @At(value = "RETURN"))
    private BlockState getStateForPlacement(BlockState place) {
        return place.setValue(CampfireBlock.LIT, false);
    }
}

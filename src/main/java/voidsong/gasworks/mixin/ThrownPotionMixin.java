package voidsong.gasworks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import voidsong.gasworks.common.util.BlockUtil;

@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin extends ThrowableItemProjectile {
    /**
     * This constructor is the default & will be ignored, it exists so we can extend Block
     * @param entityType ignored & should not be used!
     * @param level ignored & should not be used!
     */
    protected ThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "dowseFire", at = @At(value = "RETURN"))
    private void addTorchesToDowseFire(BlockPos pos, CallbackInfo ci, @Local(name = "blockstate") BlockState state) {
        BlockUtil.dowseTorch(this.getOwner(), state, level(), pos);
    }
}

package voidsong.gasworks.mixin.waterlogging;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import voidsong.gasworks.common.block.interfaces.FragileWaterloggedBlock;

@Mixin(FlowingFluid.class)
public class FlowingFluidInjections {
    @Definition(id = "LiquidBlockContainer", type = LiquidBlockContainer.class)
    @Expression("? instanceof LiquidBlockContainer")
    @WrapOperation(method = "spreadTo", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean allowFragileReplacement(Object object, Operation<Boolean> original, @Local(argsOnly = true)FluidState fluidState) {
        return original.call(object) && !((object instanceof FragileWaterloggedBlock || object instanceof TorchBlock) && (fluidState.getType() != Fluids.WATER));
    }
}

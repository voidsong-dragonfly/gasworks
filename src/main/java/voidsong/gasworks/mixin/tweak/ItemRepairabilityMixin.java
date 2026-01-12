package voidsong.gasworks.mixin.tweak;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import voidsong.gasworks.api.GSTags;

@Mixin(Item.class)
public class ItemRepairabilityMixin {
    /*
     * Remove crafting repair of selected items, used to make Vanilla tools match Gasworks DualDurabilityItem(s)
     */
    @ModifyReturnValue(method = "isRepairable", at = @At(value = "RETURN"))
    private boolean isRepairable(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (stack.is(GSTags.Items.BLOCK_CRAFTING_REPAIR)) {
            return false;
        }
        return original;
    }

}

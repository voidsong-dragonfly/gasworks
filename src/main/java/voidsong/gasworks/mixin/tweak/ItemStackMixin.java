package voidsong.gasworks.mixin.tweak;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public class ItemStackMixin {
    /*
     * Increase potion stack size to 16 to encourage their use by making them not a massive pain to store en masse
     * and not a huge drain on inventory space for use in combat
     */

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potion", ordinal = 0)))
    private static int potionStackSize(int original) {
        return 16;
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=splash_potion", ordinal = 0)))
    private static int splashPotionStackSize(int original) {
        return 16;
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=lingering_potion", ordinal = 0)))
    private static int lingeringPotionStackSize(int original) {
        return 16;
    }

    /*
     * Increase enchanted book stack size to 16 to make enchanting less of a pain to work with, and to encourage storing
     * enchanted books rather than tossing them
     */

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=enchanted_book", ordinal = 0)))
    private static int enchantedBookStackSize(int original) {
        return 16;
    }
}

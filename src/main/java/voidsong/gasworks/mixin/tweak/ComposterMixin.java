package voidsong.gasworks.mixin.tweak;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import voidsong.gasworks.common.registry.GSItems;

@SuppressWarnings("unused")
@Mixin(ComposterBlock.class)
public abstract class ComposterMixin {

    @Mixin(ComposterBlock.OutputContainer.class)
    abstract static class OutputContainer extends SimpleContainer implements WorldlyContainer {
        @ModifyArg(method = "canTakeItemThroughFace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"), index = 0)
        private Item isCorrectItem(Item original) {
            return original.equals(Items.BONE_MEAL) ? GSItems.COMPOST_PILE.get() : original;
        }
    }

    @ModifyArg(method = "extractProduce", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V"), index = 0)
    private static ItemLike extractCorrectItem(ItemLike original) {
        return original.equals(Items.BONE_MEAL) ? GSItems.COMPOST_PILE.get() : original;
    }

    @ModifyArg(method = "getContainer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V"), index = 0)
    private ItemLike containerCorrectItem(ItemLike original) {
        return original.equals(Items.BONE_MEAL) ? GSItems.COMPOST_PILE.get() : original;
    }
}


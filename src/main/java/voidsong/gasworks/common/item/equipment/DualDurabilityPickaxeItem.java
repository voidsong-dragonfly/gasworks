package voidsong.gasworks.common.item.equipment;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import voidsong.gasworks.api.durability.ToolMaterial;

import javax.annotation.Nonnull;

public class DualDurabilityPickaxeItem extends DualDurabilityToolItem {
    public DualDurabilityPickaxeItem(ToolMaterial material, Item.Properties properties) {
        super(material, properties, BlockTags.MINEABLE_WITH_PICKAXE, 1.0F, true, -2.8F);
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility);
    }
}

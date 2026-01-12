package voidsong.gasworks.common.item.equipment;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import voidsong.gasworks.api.durability.ToolMaterial;

import javax.annotation.Nonnull;

public class DualDurabilityShovelItem extends DualDurabilityToolItem {
    public DualDurabilityShovelItem(ToolMaterial material, Properties properties) {
        super(material, properties, BlockTags.MINEABLE_WITH_SHOVEL, 1.5F, true, -3.0F);
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility);
    }
}

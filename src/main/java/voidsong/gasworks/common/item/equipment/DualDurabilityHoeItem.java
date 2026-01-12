package voidsong.gasworks.common.item.equipment;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import voidsong.gasworks.api.durability.ToolMaterial;
import voidsong.gasworks.api.durability.ToolMaterials;

import javax.annotation.Nonnull;

public class DualDurabilityHoeItem extends DualDurabilityToolItem {
    public DualDurabilityHoeItem(ToolMaterials material, Properties properties) {
        super(material, properties, BlockTags.MINEABLE_WITH_HOE, 1.0F, false, getAttackSpeedByMaterial(material));
    }

    public DualDurabilityHoeItem(ToolMaterial material, Properties properties, float toolAttackSpeed) {
        super(material, properties, BlockTags.MINEABLE_WITH_HOE, 1.0F, false, toolAttackSpeed);
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private static float getAttackSpeedByMaterial(ToolMaterials material) {
        return switch (material) {
            case CRUDE, COPPER -> -2.0F;
            case IRON, CASE_HARDENED_IRON -> -1.0F;
            case STEEL, CASE_HARDENED_STEEL -> 0.0F;
            case HSNA -> 0.0F;
        };
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility);
    }
}

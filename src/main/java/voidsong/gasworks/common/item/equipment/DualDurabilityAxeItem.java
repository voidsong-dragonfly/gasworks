package voidsong.gasworks.common.item.equipment;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import voidsong.gasworks.api.durability.ToolMaterial;
import voidsong.gasworks.api.durability.ToolMaterials;

import javax.annotation.Nonnull;

public class DualDurabilityAxeItem extends DualDurabilityToolItem {
    public DualDurabilityAxeItem(ToolMaterials material, Properties properties) {
        super(material, properties, BlockTags.MINEABLE_WITH_AXE, getAttackDamageByMaterial(material), false, getAttackSpeedByMaterial(material));
    }

    public DualDurabilityAxeItem(ToolMaterial material, Properties properties, float toolAttackDamage, float toolAttackSpeed) {
        super(material, properties, BlockTags.MINEABLE_WITH_AXE, toolAttackDamage, false, toolAttackSpeed);
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private static float getAttackSpeedByMaterial(ToolMaterials material) {
        return switch (material) {
            case CRUDE, COPPER -> -3.2F;
            case IRON, CASE_HARDENED_IRON -> -3.1F;
            case STEEL, CASE_HARDENED_STEEL -> -3.0F;
            case HSNA -> -3.0F;
        };
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private static float getAttackDamageByMaterial(ToolMaterials material) {
        return switch (material) {
            case CRUDE, COPPER -> 7.0F;
            case IRON, CASE_HARDENED_IRON -> 9.0F;
            case STEEL, CASE_HARDENED_STEEL -> 9.0F;
            case HSNA -> 10.0F;
        };
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
    }
}

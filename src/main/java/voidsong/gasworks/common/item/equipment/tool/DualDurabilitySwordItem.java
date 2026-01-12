package voidsong.gasworks.common.item.equipment.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import voidsong.gasworks.api.durability.ToolMaterial;

import javax.annotation.Nonnull;

public class DualDurabilitySwordItem extends DualDurabilityToolItem {
    public DualDurabilitySwordItem(ToolMaterial material, Item.Properties properties) {
        super(material, properties, SwordItem.createToolProperties(), 3, true, -2.4F);
    }

    @Override
    public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    protected boolean isWeapon() {
        return true;
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }
}

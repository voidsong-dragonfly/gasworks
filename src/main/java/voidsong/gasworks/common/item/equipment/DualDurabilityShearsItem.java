package voidsong.gasworks.common.item.equipment;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import voidsong.gasworks.api.durability.ToolMaterials;

import javax.annotation.Nonnull;

public class DualDurabilityShearsItem extends DualDurabilityItem {
    public DualDurabilityShearsItem(Item.Properties properties) {
        super(properties.component(DataComponents.TOOL, ShearsItem.createToolProperties()),
                ToolMaterials.IRON.getMajorDurability(), ToolMaterials.IRON.getMinorDurability());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, @Nonnull BlockState state) {
        // If we have a tool that applies to this block, we check based on minor durability
        if (stack.get(DataComponents.TOOL) instanceof Tool tool && tool.getMiningSpeed(state) != tool.defaultMiningSpeed()) {
            return ToolMaterials.IRON.getDurabilityModifiedMiningSpeed(tool.getMiningSpeed(state), getWear(stack));
        }
        // Otherwise we return the default for not having a usable tool for this block
        return 1.0f;
    }

    @Override
    public boolean mineBlock(@Nonnull ItemStack stack, Level level, @Nonnull BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity entityLiving) {
        if (!level.isClientSide && !state.is(BlockTags.FIRE)) {
            stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
        }
        return state.is(BlockTags.LEAVES)
                || state.is(Blocks.COBWEB) || state.is(Blocks.TRIPWIRE)
                || state.is(Blocks.SHORT_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.DEAD_BUSH)
                || state.is(Blocks.HANGING_ROOTS) || state.is(Blocks.VINE)
                || state.is(BlockTags.WOOL);
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SHEARS_ACTIONS.contains(itemAbility);
    }
}

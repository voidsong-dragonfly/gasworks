package voidsong.gasworks.common.item.equipment;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import voidsong.gasworks.common.registry.GSDataComponents;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class DualDurabilityItem extends Item {
    public DualDurabilityItem(Item.Properties properties, int majorDurability, int minorDurability) {
        super(properties.durability(majorDurability)
                .component(GSDataComponents.WEAR, 0)
                .component(GSDataComponents.MAX_WEAR, minorDurability));
    }

    /*
     * Methods for handling durability display bars, these are modified from Item to take into account both the base
     * bar width for damage and the bar fill/color from wear. These pair with a mixin in GuiGraphics to display properly
     * rather than displaying a full-width bar the entire time
     */

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        float base = getBaseBarWidth(stack);
        return Math.round(base - getWear(stack)*base/getMaxWear(stack));
    }

    public int getBaseBarWidth(ItemStack stack) {
        return Math.round(13.0F * (stack.getDamageValue()/(float)this.getMaxDamage(stack)));
    }

    /*
     * Methods for handling durability. Major durability uses [...damage...] methods, but minor durability has methods
     * added for handling, using the [...wear...] keyword as well. Additionally, we modify minor durability when we
     * modify major durability.
     * We also disable repairability in the crafting table and Grindstone, as custom items should be repaired using
     * Gasworks repair methods and not the simple variants.
     * These are copied from IItemExtension!
     */

    @Override
    public boolean isRepairable(@Nonnull ItemStack stack) {
        return false;
    }

    // We set wear alongside damage to prevent other methods from modifying it
    public <T extends LivingEntity> int damageItem(@Nonnull ItemStack stack, int amount, @Nullable T entity, @Nonnull Consumer<Item> onBroken) {
        setWear(stack, getWear(stack) + amount);
        return amount;
    }

    /**
     * Return the wear present on this stack.
     * @param stack  {@link ItemStack} that is worn
     * @return the wear value
     */
    public int getWear(ItemStack stack) {
        return Mth.clamp(stack.getOrDefault(GSDataComponents.WEAR, 0), 0, getMaxWear(stack));
    }

    /**
     * Return maximum wear for this stack
     * @param stack {@link ItemStack} to check max wear on
     * @return the maximum wear allowed
     */
    public int getMaxWear(ItemStack stack) {
        return stack.getOrDefault(GSDataComponents.MAX_WEAR, 0);
    }

    /**
     * Return if this stack is worn.
     * @param stack {@link ItemStack} to check the status of
     * @return boolean for the stack's worn state
     */
    public boolean isWorn(ItemStack stack) {
        return getWear(stack) > 0;
    }

    /**
     * Set the wear for this stack. Note, this method is responsible for zero checking.
     * @param stack  {@link ItemStack} to set wear on
     * @param damage int damage value to set
     */
    public void setWear(ItemStack stack, int damage) {
        stack.set(GSDataComponents.WEAR, Mth.clamp(damage, 0, getMaxWear(stack)));
    }

    /*
     * Methods for handling repairability; these are modelled off of ItemStack#isValidRepairItem.
     * DualDurabilityItem(s) return false for those as the anvil does not repair items in the way we want
     */

    /**
     * Used to check if a stack can be minorly repaired by this item
     * @param material {@link ItemStack} to use to repair the tool
     * @param repair {@link ItemStack} tool to be repaired
     * @return boolean whether the item can be repaired
     */
    public boolean isValidMinorRepairItem(@Nonnull ItemStack material, @Nonnull ItemStack repair) {
        return false;
    }

    /**
     * Used to check if a stack can be majorly repaired by this item
     * @param material {@link ItemStack} to use to repair the tool
     * @param repair {@link ItemStack} tool to be repaired
     * @return boolean whether the item can be repaired
     */
    public boolean isValidMajorRepairItem(@Nonnull ItemStack material, @Nonnull ItemStack repair) {
        return false;
    }
}

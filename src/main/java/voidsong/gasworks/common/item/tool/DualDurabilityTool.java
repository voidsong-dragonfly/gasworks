package voidsong.gasworks.common.item.tool;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import voidsong.gasworks.api.tool.Material;
import voidsong.gasworks.common.registry.GSDataComponents;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class DualDurabilityTool extends Item {
    private final Material material;

    public DualDurabilityTool(Material material, Item.Properties properties, TagKey<Block> blocks, float baseToolTypeAttackDamage, boolean addMaterialDamage, float baseToolTypeAttackSpeed) {
        super(properties.durability(material.getMajorDurability())
                .component(GSDataComponents.WEAR, 0)
                .component(GSDataComponents.MAX_WEAR, material.getMinorDurability())
                .component(DataComponents.TOOL, material.createToolProperties(blocks))
                .attributes(createAttributes(material, baseToolTypeAttackDamage, addMaterialDamage, baseToolTypeAttackSpeed)));
        this.material = material;
    }

    public DualDurabilityTool(Material material, Item.Properties properties, Tool tool, float baseToolTypeAttackDamage, boolean addMaterialDamage, float baseToolTypeAttackSpeed) {
        super(properties.durability(material.getMajorDurability())
                .component(GSDataComponents.WEAR, 0)
                .component(GSDataComponents.MAX_WEAR, material.getMinorDurability())
                .component(DataComponents.TOOL, tool)
                .attributes(createAttributes(material, baseToolTypeAttackDamage, addMaterialDamage, baseToolTypeAttackSpeed)));
        this.material = material;
    }

    /*
     * This method is copied from DiggerItem and SwordItem and modified to be private. It expects to be passed information
     * from the constructor rather than passing in the property attributes in the registration; the most idiomatic method
     * to use it is to have each tool type specify both of these without needing to pass them back to the registration.
     */

    private static ItemAttributeModifiers createAttributes(Tier tier, float baseAttackDamage, boolean addMaterialDamage, float baseAttackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (baseAttackDamage + (addMaterialDamage ? tier.getAttackDamageBonus() : 0)), AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
                )
                .add(Attributes.ATTACK_SPEED,
                    new AttributeModifier(BASE_ATTACK_SPEED_ID, baseAttackSpeed, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
                ).build();
    }

    /**
     * Provides the material this tool is made out of, analogous to {@link net.minecraft.world.item.Tier} for Vanilla tools
     * @return {@link Material} the tool is made out of
     */
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public int getEnchantmentValue(@Nonnull ItemStack stack) {
        return this.material.getEnchantmentValue();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, @Nonnull BlockState state) {
        // If we have a tool that applies to this block, we check based on minor durability
        if (stack.get(DataComponents.TOOL) instanceof Tool tool && tool.getMiningSpeed(state) != tool.defaultMiningSpeed()) {
            return material.getDurabilityModifiedMiningSpeed(tool.getMiningSpeed(state), 0);
        }
        // Otherwise we return the default for not having a usable tool for this block
        return 1.0f;
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
     * The following methods are condensed from DiggerItem and SwordItem as there's really no need for that much
     * differentiation when we can have the item class specify whether it's a weapon or not;  it's just duplicated work
     * and code for little reason that I can see
     */

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        stack.hurtAndBreak(isWeapon() ? 1 : 2, attacker, EquipmentSlot.MAINHAND);
    }

    protected boolean isWeapon() {
        return false;
    }

    /*
     * Methods for handling durability. Major durability uses [...damage...] methods, but minor durability has methods
     * added for handling, using the [...wear...] keyword as well. Additionally, we modify minor durability when we
     * modify major durability.
     * These are copied from IItemExtension!
     */

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
        return this.material.getMinorRepairIngredient().test(repair);
    }

    /**
     * Used to check if a stack can be majorly repaired by this item
     * @param material {@link ItemStack} to use to repair the tool
     * @param repair {@link ItemStack} tool to be repaired
     * @return boolean whether the item can be repaired
     */
    public boolean isValidMajorRepairItem(@Nonnull ItemStack material, @Nonnull ItemStack repair) {
        return this.material.getMajorRepairIngredient().test(repair);
    }
}

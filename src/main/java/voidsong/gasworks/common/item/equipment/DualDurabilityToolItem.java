package voidsong.gasworks.common.item.equipment;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
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
import voidsong.gasworks.api.durability.ToolMaterial;

import javax.annotation.Nonnull;

public abstract class DualDurabilityToolItem extends DualDurabilityItem {
    private final ToolMaterial material;

    public DualDurabilityToolItem(ToolMaterial material, Item.Properties properties, TagKey<Block> blocks, float baseToolTypeAttackDamage, boolean addMaterialDamage, float baseToolTypeAttackSpeed) {
        super(properties.component(DataComponents.TOOL, material.createToolProperties(blocks))
                .attributes(createAttributes(material, baseToolTypeAttackDamage, addMaterialDamage, baseToolTypeAttackSpeed)),
                material.getMajorDurability(), material.getMinorDurability());
        this.material = material;
    }

    public DualDurabilityToolItem(ToolMaterial material, Item.Properties properties, Tool tool, float baseToolTypeAttackDamage, boolean addMaterialDamage, float baseToolTypeAttackSpeed) {
        super(properties.component(DataComponents.TOOL, tool)
                .attributes(createAttributes(material, baseToolTypeAttackDamage, addMaterialDamage, baseToolTypeAttackSpeed)),
                material.getMajorDurability(), material.getMinorDurability());
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
     * @return {@link ToolMaterial} the tool is made out of
     */
    public ToolMaterial getMaterial() {
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
            return material.getDurabilityModifiedMiningSpeed(tool.getMiningSpeed(state), getWear(stack));
        }
        // Otherwise we return the default for not having a usable tool for this block
        return 1.0f;
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

    @Override
    public boolean isValidMinorRepairItem(@Nonnull ItemStack material, @Nonnull ItemStack repair) {
        return this.material.getMinorRepairIngredient().test(repair);
    }

    @Override
    public boolean isValidMajorRepairItem(@Nonnull ItemStack material, @Nonnull ItemStack repair) {
        return this.material.getMajorRepairIngredient().test(repair);
    }
}

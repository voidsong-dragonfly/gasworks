package voidsong.gasworks.api.durability;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import voidsong.gasworks.api.GSTags;
import voidsong.gasworks.api.MaterialTags;

import javax.annotation.Nonnull;

public enum ToolMaterials implements ToolMaterial {
    CRUDE(GSTags.BlockTags.INCORRECT_FOR_CRUDE_TOOL, GSTags.BlockTags.NEEDS_CRUDE_TOOL,
            200, 200, 4.0F, 0.0F, 5,
            Ingredient.EMPTY, Ingredient.EMPTY),
    COPPER(GSTags.BlockTags.INCORRECT_FOR_COPPER_TOOL, GSTags.BlockTags.NEEDS_COPPER_TOOL,
            250, 4*250, 6.0F, 1.0F, 15,
            MaterialTags.ItemTags.NUGGET_COPPER, Tags.Items.INGOTS_COPPER),
    CASE_HARDENED_IRON(GSTags.BlockTags.INCORRECT_FOR_STEEL_TOOL, GSTags.BlockTags.NEEDS_STEEL_TOOL,
            500, 4*500, 12.0F, 2.0F, 10,
            Ingredient.of(Tags.Items.NUGGETS_IRON), Ingredient.EMPTY),
    IRON(GSTags.BlockTags.INCORRECT_FOR_IRON_TOOL, GSTags.BlockTags.NEEDS_IRON_TOOL,
            500, 5*500, 8.0F, 2.0F, 15,
            Tags.Items.NUGGETS_IRON, Tags.Items.INGOTS_IRON,
            ToolMaterials.CASE_HARDENED_IRON),
    CASE_HARDENED_STEEL(GSTags.BlockTags.INCORRECT_FOR_STEEL_TOOL, GSTags.BlockTags.NEEDS_STEEL_TOOL,
            1000, 4*1000, 20.0F, 3.0F, 10,
            Ingredient.of(MaterialTags.ItemTags.NUGGET_STEEL), Ingredient.EMPTY),
    STEEL(GSTags.BlockTags.INCORRECT_FOR_STEEL_TOOL, GSTags.BlockTags.NEEDS_STEEL_TOOL,
            1000, 5*1000, 12.0F, 3.0F, 15,
            MaterialTags.ItemTags.NUGGET_STEEL, MaterialTags.ItemTags.INGOT_STEEL,
            ToolMaterials.CASE_HARDENED_STEEL),
    HSNA(GSTags.BlockTags.INCORRECT_FOR_HSNA_TOOL, GSTags.BlockTags.NEEDS_HSNA_TOOL,
            2000, 4*2000, 30.0F, 4.0F, 20,
            MaterialTags.ItemTags.NUGGET_HSNA, MaterialTags.ItemTags.INGOT_HSNA);

    private final TagKey<Block> incorrectBlocksForDrops;
    private final TagKey<Block> correctBlockForDrops;
    private final int minorDurability;
    private final int majorDurability;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Ingredient minorRepairIngredient;
    private final Ingredient majorRepairIngredient;
    private final ToolMaterial caseHardeningTransform;

    ToolMaterials(TagKey<Block> incorrectBlockForDrops, TagKey<Block> correctBlockForDrops,
                  int minorDurability, int majorDurability,
                  float speed,
                  float damage,
                  int enchantmentValue,
                  TagKey<Item> minorRepairIngredient, TagKey<Item> majorRepairIngredient,
                  ToolMaterial caseHardeningTransform) {
        this.incorrectBlocksForDrops = incorrectBlockForDrops;
        this.correctBlockForDrops = correctBlockForDrops;
        this.minorDurability = minorDurability;
        this.majorDurability = majorDurability;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.minorRepairIngredient = Ingredient.of(minorRepairIngredient);
        this.majorRepairIngredient = Ingredient.of(majorRepairIngredient);
        this.caseHardeningTransform = caseHardeningTransform;
    }

    ToolMaterials(TagKey<Block> incorrectBlockForDrops, TagKey<Block> correctBlockForDrops,
                  int minorDurability, int majorDurability,
                  float speed,
                  float damage,
                  int enchantmentValue,
                  Ingredient minorRepairIngredient, Ingredient majorRepairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlockForDrops;
        this.correctBlockForDrops = correctBlockForDrops;
        this.minorDurability = minorDurability;
        this.majorDurability = majorDurability;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.minorRepairIngredient = minorRepairIngredient;
        this.majorRepairIngredient = majorRepairIngredient;
        this.caseHardeningTransform = null;
    }

    ToolMaterials(TagKey<Block> incorrectBlockForDrops, TagKey<Block> correctBlockForDrops,
                  int minorDurability, int majorDurability,
                  float speed,
                  float damage,
                  int enchantmentValue,
                  TagKey<Item> minorRepairIngredient, TagKey<Item> majorRepairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlockForDrops;
        this.correctBlockForDrops = correctBlockForDrops;
        this.minorDurability = minorDurability;
        this.majorDurability = majorDurability;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.minorRepairIngredient = Ingredient.of(minorRepairIngredient);
        this.majorRepairIngredient = Ingredient.of(majorRepairIngredient);
        this.caseHardeningTransform = null;
    }

    @Override
    public int getMinorDurability() {
        return this.minorDurability;
    }

    @Override
    public int getMajorDurability() {
        return this.majorDurability;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    @Nonnull
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    @Nonnull
    public TagKey<Block> getTag() {
        return this.correctBlockForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    @Nonnull
    public Ingredient getMinorRepairIngredient() {
        return this.minorRepairIngredient;
    }

    @Override
    @Nonnull
    public Ingredient getMajorRepairIngredient() {
        return this.majorRepairIngredient;
    }
    @Override
    public ToolMaterial caseHardenedVariant() {
        return caseHardeningTransform;
    }
}

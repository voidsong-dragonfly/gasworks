package voidsong.gasworks.api.tool;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Material extends Tier {
    /**
     * Part of the dual-durability system for Gasworks tools, use instead of {@link Tier#getUses()}
     * @return Int minor (decreases tool stats) durability for this tool material
     */
    int getMinorDurability();

    /**
     * Part of the dual-durability system for Gasworks tools, use instead of {@link Tier#getUses()}
     * @return Int major (until shattering) durability for this tool material
     */
    int getMajorDurability();

    /**
     * Part of the dual-durability system for Gasworks tools, use instead of {@link Tier#getRepairIngredient()}
     * @return Minor repair {@link Ingredient} for this tool material
     */
    @Nonnull Ingredient getMinorRepairIngredient();

    /**
     * Part of the dual-durability system for Gasworks tools, use instead of {@link Tier#getRepairIngredient()}
     * @return Major repair {@link Ingredient} for this tool material
     */
    @Nonnull Ingredient getMajorRepairIngredient();

    /**
     * Get the result of putting this material in a case-hardening furnace; expected to return null for non-ferrous
     * materials or already case-hardened materials
     * @return Nullable {@link Material} result of case-hardening this material
     */
    @Nullable Material caseHardenedVariant();

    /**
     * Get the mining speed for this material, modified by the minor durability left on the item. The default function
     * uses <a href="https://www.desmos.com/calculator/ncwxcjfp0v">this</a> curve to specify the reduction in mining
     * speed for using a non-fully-repaired tool
     * @return the modified base mining speed for the given minor durability
     */
    default float getDurabilityModifiedMiningSpeed(float baseSpeed, int minorDurabilityLeft) {
        float edgeFraction = minorDurabilityLeft/(float)getMinorDurability();
        return (int)(baseSpeed*Math.pow(Math.cos((0.5*Math.PI)*edgeFraction), 0.1));
    }

    /**
     * Should not be used for Gasworks materials! Use {@link Material#getMinorDurability()} or
     * {@link Material#getMajorDurability()} instead, depending on context
     * @return int simple maximum durability for this tool material
     */
    @Deprecated
    @Override
    default int getUses() {
        return getMajorDurability();
    }

    /**
     * Should not be used for Gasworks materials! Use {@link Material#getMinorRepairIngredient()} or
     * {@link Material#getMajorRepairIngredient()} instead, depending on context
     * @return Base case {@link Ingredient} for repairing this tool material
     */
    @Deprecated
    @Nonnull
    @Override
    default Ingredient getRepairIngredient() {
        return getMajorRepairIngredient();
    }
}

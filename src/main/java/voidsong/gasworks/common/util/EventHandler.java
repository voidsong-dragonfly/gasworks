package voidsong.gasworks.common.util;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.FarmBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Set;

public class EventHandler {
    @SubscribeEvent
    public static void preventCactusItemDestroy(EntityInvulnerabilityCheckEvent event) {
        if (event.getEntity() instanceof ItemEntity && event.getSource().is(DamageTypes.CACTUS)) {
            event.setInvulnerable(true);
        }
    }

    @SubscribeEvent
    public static void preventFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        // if the fall distance is short & the farmland is hydrated, we don't trample
        if (event.getFallDistance() < 2 && event.getState().getValue(FarmBlock.MOISTURE) > 0)
            event.setCanceled(true);
        // If the mob has feather falling, no trampling regardless
        else if (event.getEntity() instanceof LivingEntity living) {
            Set<Holder<Enchantment>> enchantments = living.getItemBySlot(EquipmentSlot.FEET).getTagEnchantments().keySet();
            for (Holder<Enchantment> enchantment : enchantments)
                if (enchantment.is(Enchantments.FEATHER_FALLING)) {
                    event.setCanceled(true);
                    return;
                }
        }
    }
}

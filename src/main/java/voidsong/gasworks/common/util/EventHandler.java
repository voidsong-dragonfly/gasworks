package voidsong.gasworks.common.util;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;

public class EventHandler {
    @SubscribeEvent
    public static void preventCactusItemDestroy(EntityInvulnerabilityCheckEvent event) {
        if (event.getEntity() instanceof ItemEntity && event.getSource().is(DamageTypes.CACTUS)) {
            event.setInvulnerable(true);
        }
    }
}

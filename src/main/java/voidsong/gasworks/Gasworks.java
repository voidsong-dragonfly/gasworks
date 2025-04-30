package voidsong.gasworks;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Gasworks.MODID)
public class Gasworks {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "gasworks";

    public Gasworks(IEventBus modEventBus) {
        // Register mod content
        //BLOCKS.register(modEventBus);
        //ITEMS.register(modEventBus);
        //CREATIVE_MODE_TABS.register(modEventBus);
    }
}

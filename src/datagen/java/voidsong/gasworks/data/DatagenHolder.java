package voidsong.gasworks.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = "gasworks")
public class DatagenHolder {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if(event.includeServer()) {
            BlockTagsProvider blocks = generator.addProvider(true, new GasworksBlockTagsProvider(output, lookupProvider, existingFileHelper));
            generator.addProvider(true, new GasworksItemTagsProvider(output, blocks.contentsGetter(), lookupProvider, existingFileHelper));
            generator.addProvider(true, new GasworksLootTableProvider(output, lookupProvider));
            generator.addProvider(true, new GasworksRecipeProvider(output, lookupProvider));
            generator.addProvider(true, new GasworksDataMapProvider(output, lookupProvider));
        }
        if(event.includeClient()) {
            generator.addProvider(true, new GasworksBlockstateProvider(output, existingFileHelper));
            generator.addProvider(true, new GasworksItemModelProvider(output, existingFileHelper));
        }
    }
}

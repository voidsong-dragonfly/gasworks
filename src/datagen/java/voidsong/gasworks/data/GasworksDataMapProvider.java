package voidsong.gasworks.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import voidsong.gasworks.common.registry.GSItems;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class GasworksDataMapProvider extends DataMapProvider {
    public GasworksDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(@Nonnull HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(GSItems.COKE, new FurnaceFuel(1600), false)
            .add(GSItems.COAL_PILE, new FurnaceFuel(12800), false);
    }
}
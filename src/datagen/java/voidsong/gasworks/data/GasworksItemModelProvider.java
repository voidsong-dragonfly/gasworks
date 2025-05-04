package voidsong.gasworks.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.registry.Items;

public class GasworksItemModelProvider extends ItemModelProvider {
    public GasworksItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Gasworks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    }
}

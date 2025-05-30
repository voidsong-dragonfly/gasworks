package voidsong.gasworks.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.registry.GSItems;

public class GasworksItemModelProvider extends ItemModelProvider {
    public GasworksItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Gasworks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(GSItems.COKE.asItem(), rl("coke"));
        basicItem(GSItems.ASH.asItem(), rl("ash"));
        basicItem(GSItems.COMPOST.asItem(), rl("compost"));
        basicItem(GSItems.FIRECLAY_BALL.asItem(), rl("fireclay_ball"));
        basicItem(GSItems.FIREBRICK.asItem(), rl("firebrick"));
        basicItem(GSItems.TRADESWOMANS_JOURNAL.asItem(), rl("tradeswomans_journal"));
    }

    /*
     * Library functions and other functions not directly related to models; none of these should be accessed outside this class
     */

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(Gasworks.MOD_ID, "item/" + path);
    }

    private ItemModelBuilder basicItem(Item item, ResourceLocation texture) {
        return getBuilder(item.toString())
            .parent(new ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", texture);
    }
}

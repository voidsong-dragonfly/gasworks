package voidsong.gasworks.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.registry.GSItems;
import voidsong.gasworks.common.registry.GSTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class GasworksItemTagsProvider extends ItemTagsProvider {
    public GasworksItemTagsProvider(PackOutput output, CompletableFuture<TagLookup<Block>>  blocks, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blocks, Gasworks.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider lookupProvider) {
        tag(GSTags.ItemTags.COAL_COKE)
            .add(GSItems.COKE.asItem());
        tag(GSTags.ItemTags.DUSTS)
            .add(GSItems.ASH.asItem());
        tag(GSTags.ItemTags.ASH_DUST)
            .add(GSItems.ASH.asItem());
        tag(GSTags.ItemTags.ASH)
            .add(GSItems.ASH.asItem());
        tag(ItemTags.LOGS_THAT_BURN)
            .add(GSItems.OAK_LOG_PILE.get(), GSItems.SPRUCE_LOG_PILE.get(), GSItems.BIRCH_LOG_PILE.get(),
                GSItems.JUNGLE_LOG_PILE.get(), GSItems.ACACIA_LOG_PILE.get(), GSItems.DARK_OAK_LOG_PILE.get(),
                GSItems.CHERRY_LOG_PILE.get(), GSItems.MANGROVE_LOG_PILE.get(), GSItems.BAMBOO_LOG_PILE.get());

    }
}

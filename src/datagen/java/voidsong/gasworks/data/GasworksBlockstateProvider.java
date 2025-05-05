package voidsong.gasworks.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.common.registry.GSBlocks;

public class GasworksBlockstateProvider extends ExtendedBlockstateProvider {
    public GasworksBlockstateProvider(PackOutput output, ExistingFileHelper exHelper) {
        super(output, exHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        logPileBlock(GSBlocks.OAK_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_oak_log"));
        logPileBlock(GSBlocks.SPRUCE_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_spruce_log"));
        logPileBlock(GSBlocks.BIRCH_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_birch_log"));
        logPileBlock(GSBlocks.JUNGLE_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_jungle_log"));
        logPileBlock(GSBlocks.ACACIA_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_acacia_log"));
        logPileBlock(GSBlocks.DARK_OAK_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_dark_oak_log"));
        logPileBlock(GSBlocks.CHERRY_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_cherry_log"));
        logPileBlock(GSBlocks.MANGROVE_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_mangrove_log"));
        logPileBlock(GSBlocks.BAMBOO_LOG_PILE.get(), ResourceLocation.withDefaultNamespace("block/stripped_bamboo_block"));
    }
}

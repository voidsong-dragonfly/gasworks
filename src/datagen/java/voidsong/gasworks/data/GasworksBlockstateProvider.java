package voidsong.gasworks.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.common.block.PyrolyticAshBlock;
import voidsong.gasworks.common.registry.GSBlocks;

import java.util.List;

public class GasworksBlockstateProvider extends ExtendedBlockstateProvider {
    public GasworksBlockstateProvider(PackOutput output, ExistingFileHelper exHelper) {
        super(output, exHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        /*
         * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
         */
        //Log stacks for fuel
        logPileBlock(GSBlocks.OAK_LOG_PILE.get(), rlMC("stripped_oak_log"));
        logPileBlock(GSBlocks.SPRUCE_LOG_PILE.get(), rlMC("stripped_spruce_log"));
        logPileBlock(GSBlocks.BIRCH_LOG_PILE.get(), rlMC("stripped_birch_log"));
        logPileBlock(GSBlocks.JUNGLE_LOG_PILE.get(), rlMC("stripped_jungle_log"));
        logPileBlock(GSBlocks.ACACIA_LOG_PILE.get(), rlMC("stripped_acacia_log"));
        logPileBlock(GSBlocks.DARK_OAK_LOG_PILE.get(), rlMC("stripped_dark_oak_log"));
        logPileBlock(GSBlocks.CHERRY_LOG_PILE.get(), rlMC("stripped_cherry_log"));
        logPileBlock(GSBlocks.MANGROVE_LOG_PILE.get(), rlMC("stripped_mangrove_log"));
        logPileBlock(GSBlocks.BAMBOO_LOG_PILE.get(), rlMC("stripped_bamboo_block"));
        //Coal stacks for fuel
        horizontalRandomCubeAllAndItem(GSBlocks.COAL_PILE.get(), null, rl("coal_pile"));
        horizontalRandomCubeAllAndItem(GSBlocks.CHARCOAL_PILE.get(), null, rl("charcoal_pile"));
        //Brick piles for firing
        brickPileCubeAll(GSBlocks.BRICK_CLAMP.get(), rl("unfired_clay_bricks"), rl("fired_clay_bricks"));
        //Resulting ash
        ashPileCubeAll(GSBlocks.PYROLYTIC_ASH.get(), List.of(PyrolyticAshBlock.ASH_TYPE), rl("charcoal_ash"), rl("coke_ash"));
    }
}

package voidsong.gasworks.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.ClampBlock;
import voidsong.gasworks.common.block.CompostBlock;
import voidsong.gasworks.common.block.PyrolyticAshBlock;
import voidsong.gasworks.common.registry.GSBlocks;

import java.util.List;

public class GasworksBlockstateProvider extends ExtendedBlockstateProvider {
    public GasworksBlockstateProvider(PackOutput output, ExistingFileHelper exHelper) {
        super(output, Gasworks.MOD_ID, exHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        /*
         * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
         */
        // Log stacks for fuel
        logPileBlock(GSBlocks.OAK_LOG_PILE.get(), rlMC("stripped_oak_log"));
        logPileBlock(GSBlocks.SPRUCE_LOG_PILE.get(), rlMC("stripped_spruce_log"));
        logPileBlock(GSBlocks.BIRCH_LOG_PILE.get(), rlMC("stripped_birch_log"));
        logPileBlock(GSBlocks.JUNGLE_LOG_PILE.get(), rlMC("stripped_jungle_log"));
        logPileBlock(GSBlocks.ACACIA_LOG_PILE.get(), rlMC("stripped_acacia_log"));
        logPileBlock(GSBlocks.DARK_OAK_LOG_PILE.get(), rlMC("stripped_dark_oak_log"));
        logPileBlock(GSBlocks.CHERRY_LOG_PILE.get(), rlMC("stripped_cherry_log"));
        logPileBlock(GSBlocks.MANGROVE_LOG_PILE.get(), rlMC("stripped_mangrove_log"));
        logPileBlock(GSBlocks.BAMBOO_LOG_PILE.get(), rlMC("stripped_bamboo_block"));
        // Coal stacks for fuel
        horizontalRandomCubeAllAndItem(GSBlocks.COAL_PILE.get(), null, rl("coal_pile"));
        horizontalRandomCubeAllAndItem(GSBlocks.CHARCOAL_PILE.get(), null, rl("charcoal_pile"));
        // Resulting ash
        ashPileCubeAll(GSBlocks.PYROLYTIC_ASH.get(), List.of(PyrolyticAshBlock.ASH_TYPE), rl("ash/charcoal_ash"), rl("ash/coke_ash"));
        // Brick piles for firing
        partialBlockAndItem(GSBlocks.UNFIRED_BRICK_CLAMP.get(),
            models().withExistingParent(getName(GSBlocks.UNFIRED_BRICK_CLAMP.get()), rl("brick_pile"))
                .texture("brick", rl("bricks/unfired_0"))
                .texture("plate", rl("stone_plate")),
            state -> models().withExistingParent(getName(GSBlocks.UNFIRED_BRICK_CLAMP.get())+"_"+state.getSetStates().get(ClampBlock.DISPLAY_AGE), rl("brick_pile"))
                .texture("brick", rl("bricks/unfired"+"_"+state.getSetStates().get(ClampBlock.DISPLAY_AGE)))
                .texture("plate", rl("stone_plate")),
            List.of(ClampBlock.DISPLAY_AGE));
        simpleBlockAndItem(GSBlocks.FIRED_BRICK_CLAMP.get(), models()
            .withExistingParent(getName(GSBlocks.FIRED_BRICK_CLAMP.get()), rl("brick_pile"))
            .texture("brick", rl("bricks/fired"))
            .texture("plate", rl("stone_plate")));
        partialBlockAndItem(GSBlocks.UNFIRED_FIREBRICK_CLAMP.get(),
            models().withExistingParent(getName(GSBlocks.UNFIRED_FIREBRICK_CLAMP.get()), rl("brick_pile"))
                .texture("brick", rl("firebricks/unfired_0"))
                .texture("plate", rl("stone_plate")),
            state -> models().withExistingParent(getName(GSBlocks.UNFIRED_FIREBRICK_CLAMP.get())+"_"+state.getSetStates().get(ClampBlock.DISPLAY_AGE), rl("brick_pile"))
                .texture("brick", rl("firebricks/unfired"+"_"+state.getSetStates().get(ClampBlock.DISPLAY_AGE)))
                .texture("plate", rl("stone_plate")),
            List.of(ClampBlock.DISPLAY_AGE));
        simpleBlockAndItem(GSBlocks.FIRED_FIREBRICK_CLAMP.get(), models()
            .withExistingParent(getName(GSBlocks.FIRED_FIREBRICK_CLAMP.get()), rl("brick_pile"))
            .texture("brick", rl("firebricks/fired"))
            .texture("plate", rl("stone_plate")));
        // Compost piles for fertilizer
        horizontalRandomBlockAndItem(GSBlocks.COMPOST_PILE.get(),
            models().cubeAll(getName(GSBlocks.COMPOST_PILE.get())+"_0", rl("compost/compost_pile_0")),
            state -> models().cubeAll(getName(GSBlocks.COMPOST_PILE.get())+"_"+state.getSetStates().get(CompostBlock.AGE),
                rl("compost/compost_pile_"+state.getSetStates().get(CompostBlock.AGE))), List.of(CompostBlock.AGE));
        /*
         * Building blocks, including various 'functional' blocks
         */
        cubeAll(GSBlocks.FIRECLAY.get(), rl("fireclay"));
        multiEightCubeAll(GSBlocks.FIREBRICKS.get(), rl("firebricks/firebricks"));
        stairsMultiEightAll(GSBlocks.FIREBRICK_STAIRS.get(), rl("firebricks/firebricks"));
        slabMultiEightAll(GSBlocks.FIREBRICK_SLAB.get(), rl("firebricks/firebricks"));
        wallMultiEight(GSBlocks.FIREBRICK_WALL.get(), rl("firebricks/firebricks"), rl("firebricks/firebricks_wall"), rl("firebricks/firebricks_top"), false);
    }
}

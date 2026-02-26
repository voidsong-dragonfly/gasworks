package voidsong.gasworks.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.CandelabraBlock;
import voidsong.gasworks.common.block.ClampBlock;
import voidsong.gasworks.common.block.CompostBlock;
import voidsong.gasworks.common.block.PyrolyticAshBlock;
import voidsong.gasworks.common.block.SillBlock;
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
        // Compost piles for fertilizer
        horizontalRandomBlockAndItem(GSBlocks.COMPOST_PILE.get(),
            models().cubeAll(getName(GSBlocks.COMPOST_PILE.get())+"_0", rl("compost/compost_pile_0")),
            state -> models().cubeAll(getName(GSBlocks.COMPOST_PILE.get())+"_"+state.getSetStates().get(CompostBlock.AGE),
                rl("compost/compost_pile_"+state.getSetStates().get(CompostBlock.AGE))), List.of(CompostBlock.AGE));
        /*
         * Building blocks, including various 'quoin/functional' blocks
         */
        // Normal brick quoins & specialty blocks
        for(DeferredBlock<SillBlock> block : GSBlocks.BRICK_SILLS) {
            SillBlock sill = block.get();
            sillMultiEight(sill, rl("bricks/bricks"), getName(sill).substring("brick_sill_".length()));
        }
        for(DeferredBlock<HorizontalDirectionalBlock> block : GSBlocks.BRICK_QUOINS) {
            HorizontalDirectionalBlock quoin = block.get();
            quoinMultiEight(quoin, rl("bricks/bricks"), getName(quoin).substring("brick_quoin_".length()));
        }
        // Framed Glass
        cubeAll(GSBlocks.FRAMED_GLASS.get(), rl("framed_glass/framed_glass"), RenderType.CUTOUT_MIPPED);
        paneBlockWithRenderType(GSBlocks.FRAMED_GLASS_PANE.get(), rl("framed_glass/framed_glass"), rl("framed_glass/framed_glass_pane_top"), "cutout_mipped");
        itemModels().getBuilder(BuiltInRegistries.BLOCK.getKey(GSBlocks.FRAMED_GLASS_PANE.get()).getPath())
            .parent(new ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", rl("framed_glass/framed_glass"));
        for(DeferredBlock<StainedGlassBlock> block : GSBlocks.STAINED_FRAMED_GLASS)
            cubeAll(block.get(), rl("framed_glass/" + block.get().getColor()+ "_stained_framed_glass"), RenderType.TRANSLUCENT);
        for(DeferredBlock<StainedGlassPaneBlock> block : GSBlocks.STAINED_FRAMED_GLASS_PANES) {
            paneBlockWithRenderType(block.get(), rl("framed_glass/" + block.get().getColor() + "_stained_framed_glass"), rl("framed_glass/framed_glass_pane_top"), "translucent");
            itemModels().getBuilder(BuiltInRegistries.BLOCK.getKey(block.get()).getPath())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .renderType("translucent")
                .texture("layer0", rl("framed_glass/" + block.get().getColor() + "_stained_framed_glass"));
        }
        // Candelabras
        candelabraBlockAndItem(GSBlocks.CANDELABRA.get(), null, 0,0);
        for(Pair<DyeColor, DeferredBlock<CandelabraBlock>> pair : GSBlocks.CANDELABRAS) {
            CandelabraBlock block = pair.getSecond().get();
            candelabraBlockAndItem(block, pair.getFirst(), 0,0);
        }
        // Unlit torches, soul & otherwise
        simpleBlockAndItem(GSBlocks.UNLIT_TORCH.get(), models()
            .withExistingParent(getName(GSBlocks.UNLIT_TORCH.get()), rlMC("template_torch"))
            .texture("torch", rl("torch/unlit")).renderType("cutout"));
        rotatedBlock(GSBlocks.UNLIT_WALL_TORCH.get(), models()
            .withExistingParent(getName(GSBlocks.UNLIT_WALL_TORCH.get()), rlMC("template_torch_wall"))
            .texture("torch", rl("torch/unlit")).renderType("cutout"),
            HorizontalDirectionalBlock.FACING, List.of(), 0, 90);
        simpleBlockAndItem(GSBlocks.UNLIT_SOUL_TORCH.get(), models()
            .withExistingParent(getName(GSBlocks.UNLIT_SOUL_TORCH.get()), rlMC("template_torch"))
            .texture("torch", rl("torch/unlit_soul")).renderType("cutout"));
        rotatedBlock(GSBlocks.UNLIT_SOUL_WALL_TORCH.get(), models()
            .withExistingParent(getName(GSBlocks.UNLIT_SOUL_WALL_TORCH.get()), rlMC("template_torch_wall"))
            .texture("torch", rl("torch/unlit_soul")).renderType("cutout"),
            HorizontalDirectionalBlock.FACING, List.of(), 0, 90);
    }
}

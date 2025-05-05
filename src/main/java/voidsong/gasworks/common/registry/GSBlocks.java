package voidsong.gasworks.common.registry;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.PyrolizingBlock;

public class GSBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Gasworks.MOD_ID);

    private static final BlockBehaviour.Properties LOG_PILE_PROPERTIES = BlockBehaviour.Properties.of()
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.0F)
        .sound(SoundType.WOOD)
        .ignitedByLava()
        .randomTicks()
        .noOcclusion();

    public static final DeferredBlock<RotatedPillarBlock> OAK_LOG_PILE = BLOCKS.registerBlock("oak_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.mapColor(MapColor.WOOD));
    public static final DeferredBlock<RotatedPillarBlock> SPRUCE_LOG_PILE = BLOCKS.registerBlock("spruce_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.mapColor(MapColor.PODZOL));
    public static final DeferredBlock<RotatedPillarBlock> BIRCH_LOG_PILE = BLOCKS.registerBlock("birch_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.mapColor(MapColor.SAND));
    public static final DeferredBlock<RotatedPillarBlock> JUNGLE_LOG_PILE = BLOCKS.registerBlock("jungle_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.mapColor(MapColor.DIRT));
    public static final DeferredBlock<RotatedPillarBlock> ACACIA_LOG_PILE = BLOCKS.registerBlock("acacia_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_ORANGE));
    public static final DeferredBlock<RotatedPillarBlock> DARK_OAK_LOG_PILE = BLOCKS.registerBlock("dark_oak_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_BROWN));
    public static final DeferredBlock<RotatedPillarBlock> CHERRY_LOG_PILE = BLOCKS.registerBlock("cherry_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.mapColor(MapColor.TERRACOTTA_PINK));
    public static final DeferredBlock<RotatedPillarBlock> MANGROVE_LOG_PILE = BLOCKS.registerBlock("mangrove_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_RED));
    public static final DeferredBlock<RotatedPillarBlock> BAMBOO_LOG_PILE = BLOCKS.registerBlock("bamboo_log_pile", PyrolizingBlock::new, LOG_PILE_PROPERTIES.sound(SoundType.BAMBOO_WOOD).mapColor(MapColor.COLOR_YELLOW));
}

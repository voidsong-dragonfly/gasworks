package voidsong.gasworks.common.registry;

import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.PyrolizingBlock;
import voidsong.gasworks.common.block.PyrolyticAshBlock;
import voidsong.gasworks.common.block.properties.AshType;

public class GSBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Gasworks.MOD_ID);

    private static final BlockBehaviour.Properties LOG_PILE_PROPERTIES = BlockBehaviour.Properties.of()
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.0F)
        .sound(SoundType.WOOD)
        .ignitedByLava()
        .randomTicks()
        .noOcclusion();

    /*
     * Beehive oven & charcoal heap blocks, incl. fuels & ash
     */
    //Log stacks for fuel
    public static final DeferredBlock<RotatedPillarBlock> OAK_LOG_PILE = BLOCKS.register("oak_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.WOOD)));
    public static final DeferredBlock<RotatedPillarBlock> SPRUCE_LOG_PILE = BLOCKS.register("spruce_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.PODZOL)));
    public static final DeferredBlock<RotatedPillarBlock> BIRCH_LOG_PILE = BLOCKS.register("birch_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.SAND)));
    public static final DeferredBlock<RotatedPillarBlock> JUNGLE_LOG_PILE = BLOCKS.register("jungle_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.DIRT)));
    public static final DeferredBlock<RotatedPillarBlock> ACACIA_LOG_PILE = BLOCKS.register("acacia_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_ORANGE)));
    public static final DeferredBlock<RotatedPillarBlock> DARK_OAK_LOG_PILE = BLOCKS.register("dark_oak_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_BROWN)));
    public static final DeferredBlock<RotatedPillarBlock> CHERRY_LOG_PILE = BLOCKS.register("cherry_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.TERRACOTTA_PINK)));
    public static final DeferredBlock<RotatedPillarBlock> MANGROVE_LOG_PILE = BLOCKS.register("mangrove_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_RED)));
    public static final DeferredBlock<RotatedPillarBlock> BAMBOO_LOG_PILE = BLOCKS.register("bamboo_log_pile", () -> new PyrolizingBlock(LOG_PILE_PROPERTIES.sound(SoundType.BAMBOO_WOOD).mapColor(MapColor.COLOR_YELLOW)));
    //Coal stacks for fuel
    public static final DeferredBlock<RotatedPillarBlock> COAL_PILE = BLOCKS.register("coal_pile", () -> new PyrolizingBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.COLOR_BLACK)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .ignitedByLava()
        .randomTicks()
        .noOcclusion()
        .requiresCorrectToolForDrops()
        .strength(5.0F, 6.0F), AshType.COKE, GSTags.BlockTags.HIGH_TEMPERATURE_PYROLIZING_WALLS));
    //Resulting ash
    public static final DeferredBlock<Block> PYROLYTIC_ASH = BLOCKS.register("pyrolytic_ash",
        () -> new PyrolyticAshBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
}

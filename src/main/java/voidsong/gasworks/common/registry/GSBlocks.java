package voidsong.gasworks.common.registry;

import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.BurnableFuelBlock;
import voidsong.gasworks.common.block.ClampBlock;
import voidsong.gasworks.common.block.PyrolyticAshBlock;
import voidsong.gasworks.common.block.BrickStackBlock;
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
     * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
     */
    //Log stacks for fuel
    public static final DeferredBlock<BurnableFuelBlock> OAK_LOG_PILE = BLOCKS.register("oak_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.WOOD), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> SPRUCE_LOG_PILE = BLOCKS.register("spruce_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.PODZOL), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> BIRCH_LOG_PILE = BLOCKS.register("birch_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.SAND), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> JUNGLE_LOG_PILE = BLOCKS.register("jungle_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.DIRT), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> ACACIA_LOG_PILE = BLOCKS.register("acacia_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_ORANGE), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> DARK_OAK_LOG_PILE = BLOCKS.register("dark_oak_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_BROWN), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> CHERRY_LOG_PILE = BLOCKS.register("cherry_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.TERRACOTTA_PINK), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> MANGROVE_LOG_PILE = BLOCKS.register("mangrove_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_RED), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> BAMBOO_LOG_PILE = BLOCKS.register("bamboo_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.sound(SoundType.BAMBOO_WOOD).mapColor(MapColor.COLOR_YELLOW), AshType.CHARCOAL, 1));
    //Coal stacks for fuel
    public static final DeferredBlock<BurnableFuelBlock> COAL_PILE = BLOCKS.register("coal_pile", () -> new BurnableFuelBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.COLOR_BLACK)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .ignitedByLava()
        .randomTicks()
        .noOcclusion()
        .requiresCorrectToolForDrops()
        .strength(2.0F, 6.0F), AshType.COKE, 8));
    public static final DeferredBlock<BurnableFuelBlock> CHARCOAL_PILE = BLOCKS.register("charcoal_pile", () -> new BurnableFuelBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.TERRACOTTA_BROWN)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .ignitedByLava()
        .randomTicks()
        .noOcclusion()
        .requiresCorrectToolForDrops()
        .strength(1.625F, 4.5F), AshType.NONE, 8));
    //Brick piles for firing
    public static final DeferredBlock<BrickStackBlock> FIRED_BRICK_CLAMP = BLOCKS.registerBlock("fired_brick_clamp", BrickStackBlock::new, BlockBehaviour.Properties.of()
        .mapColor(MapColor.COLOR_RED)
        .sound(SoundType.NETHER_BRICKS)
        .noOcclusion()
        .strength(0.5f, 0.25f));
    public static final DeferredBlock<ClampBlock> UNFIRED_BRICK_CLAMP = BLOCKS.register("unfired_brick_clamp", () -> new ClampBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.CLAY)
        .sound(SoundType.GRAVEL)
        .noOcclusion()
        .strength(0.5f, 0.25f), FIRED_BRICK_CLAMP.get().defaultBlockState().setValue(BrickStackBlock.FIRED, true)));
    //Resulting ash
    public static final DeferredBlock<Block> PYROLYTIC_ASH = BLOCKS.register("pyrolytic_ash",
        () -> new PyrolyticAshBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
}

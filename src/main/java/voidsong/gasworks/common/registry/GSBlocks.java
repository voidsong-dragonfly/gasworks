package voidsong.gasworks.common.registry;

import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.*;
import voidsong.gasworks.common.block.properties.AshType;

import java.util.ArrayList;
import java.util.List;

public class GSBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Gasworks.MOD_ID);

    private static final BlockBehaviour.Properties LOG_PILE_PROPERTIES = BlockBehaviour.Properties.of()
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.0F)
        .sound(SoundType.WOOD)
        .ignitedByLava()
        .randomTicks()
        .noOcclusion();
    private static final BlockBehaviour.Properties FIREBRICKS_PROPERTIES = BlockBehaviour.Properties.of()
        .mapColor(MapColor.TERRACOTTA_WHITE)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .strength(2.5F, 8.0F);
    private static final BlockBehaviour.Properties BRICKS_PROPERTIES = BlockBehaviour.Properties.of()
        .mapColor(MapColor.COLOR_RED)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .strength(2.0F, 6.0F);
    public static final BlockBehaviour.Properties FRAMED_GLASS_PROPERTIES = BlockBehaviour.Properties.of()
        .instrument(NoteBlockInstrument.HAT)
        .strength(0.75F)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .isValidSpawn((state, getter, pos, type) -> false)
        .isRedstoneConductor((state, getter, pos) -> false)
        .isSuffocating((state, getter, pos) -> false)
        .isViewBlocking((state, getter, pos) -> false);

    /*
     * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
     */
    // Log stacks for fuel
    public static final DeferredBlock<BurnableFuelBlock> OAK_LOG_PILE = BLOCKS.register("oak_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.WOOD), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> SPRUCE_LOG_PILE = BLOCKS.register("spruce_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.PODZOL), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> BIRCH_LOG_PILE = BLOCKS.register("birch_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.SAND), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> JUNGLE_LOG_PILE = BLOCKS.register("jungle_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.DIRT), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> ACACIA_LOG_PILE = BLOCKS.register("acacia_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_ORANGE), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> DARK_OAK_LOG_PILE = BLOCKS.register("dark_oak_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_BROWN), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> CHERRY_LOG_PILE = BLOCKS.register("cherry_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.TERRACOTTA_PINK), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> MANGROVE_LOG_PILE = BLOCKS.register("mangrove_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.mapColor(MapColor.COLOR_RED), AshType.CHARCOAL, 1));
    public static final DeferredBlock<BurnableFuelBlock> BAMBOO_LOG_PILE = BLOCKS.register("bamboo_log_pile", () -> new BurnableFuelBlock(LOG_PILE_PROPERTIES.sound(SoundType.BAMBOO_WOOD).mapColor(MapColor.COLOR_YELLOW), AshType.CHARCOAL, 1));
    // Coal stacks for fuel
    public static final DeferredBlock<BurnableFuelBlock> COAL_PILE = BLOCKS.register("coal_pile", () -> new BurnableFuelBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.COLOR_BLACK)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .ignitedByLava()
        .randomTicks()
        .noOcclusion()
        .requiresCorrectToolForDrops()
        .strength(2.0F, 6.0F), AshType.COKE, 6));
    public static final DeferredBlock<BurnableFuelBlock> CHARCOAL_PILE = BLOCKS.register("charcoal_pile", () -> new BurnableFuelBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.TERRACOTTA_BROWN)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .ignitedByLava()
        .randomTicks()
        .noOcclusion()
        .requiresCorrectToolForDrops()
        .strength(1.625F, 4.5F), AshType.NONE, 6));
    // Resulting ash
    public static final DeferredBlock<Block> PYROLYTIC_ASH = BLOCKS.register("pyrolytic_ash",
        () -> new PyrolyticAshBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
    // Brick piles for firing
    public static final DeferredBlock<BrickStackBlock> FIRED_BRICK_CLAMP = BLOCKS.registerBlock("fired_brick_clamp", BrickStackBlock::new, BlockBehaviour.Properties.of()
        .mapColor(MapColor.COLOR_RED)
        .sound(SoundType.NETHER_BRICKS)
        .noOcclusion()
        .strength(0.5f, 0.25f));
    public static final DeferredBlock<ClampBlock> UNFIRED_BRICK_CLAMP = BLOCKS.register("unfired_brick_clamp", () -> new ClampBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.TERRACOTTA_RED)
        .sound(SoundType.GRAVEL)
        .noOcclusion()
        .strength(0.5f, 0.25f), FIRED_BRICK_CLAMP.get().defaultBlockState().setValue(BrickStackBlock.FIRED, true)));
    public static final DeferredBlock<BrickStackBlock> FIRED_FIREBRICK_CLAMP = BLOCKS.registerBlock("fired_firebrick_clamp", BrickStackBlock::new, BlockBehaviour.Properties.of()
        .mapColor(MapColor.COLOR_RED)
        .sound(SoundType.NETHER_BRICKS)
        .noOcclusion()
        .strength(0.625f, 0.375f));
    public static final DeferredBlock<ClampBlock> UNFIRED_FIREBRICK_CLAMP = BLOCKS.register("unfired_firebrick_clamp", () -> new ClampBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
        .sound(SoundType.GRAVEL)
        .noOcclusion()
        .strength(0.625f, 0.375f), FIRED_FIREBRICK_CLAMP.get().defaultBlockState().setValue(BrickStackBlock.FIRED, true)));
    // Compost piles for fertilizer
    public static final DeferredBlock<CompostBlock> COMPOST_PILE = BLOCKS.register("compost_pile", () -> new CompostBlock(BlockBehaviour.Properties.of()
        .mapColor(MapColor.PODZOL)
        .sound(SoundType.GRAVEL)
        .ignitedByLava()
        .randomTicks()
        .strength(0.5F)));
    /*
     * Building blocks, including various 'functional' blocks
     */
    // Stone types for use in building blocks
    public static List<String> stones = List.of("andesite", "deepslate", "diorite", "granite", "polished_blackstone", "stone", "tuff");
    // Fireclay blocks of various types
    public static DeferredBlock<Block> FIRECLAY = BLOCKS.registerSimpleBlock("fireclay", BlockBehaviour.Properties.of()
        .mapColor(MapColor.CLAY)
        .instrument(NoteBlockInstrument.FLUTE)
        .strength(0.75F)
        .sound(SoundType.GRAVEL));
    public static DeferredBlock<Block> FIREBRICKS = BLOCKS.registerSimpleBlock("firebricks", FIREBRICKS_PROPERTIES);
    public static DeferredBlock<StairBlock> FIREBRICK_STAIRS = BLOCKS.register("firebrick_stairs", () -> new StairBlock(FIREBRICKS.get().defaultBlockState(), FIREBRICKS_PROPERTIES));
    public static DeferredBlock<SlabBlock> FIREBRICK_SLAB = BLOCKS.registerBlock("firebrick_slab",  SlabBlock::new, FIREBRICKS_PROPERTIES);
    public static DeferredBlock<WallBlock> FIREBRICK_WALL = BLOCKS.registerBlock("firebrick_wall",  WallBlock::new, FIREBRICKS_PROPERTIES);
    public static List<DeferredBlock<SillBlock>> FIREBRICK_SILLS = createSills("firebrick", FIREBRICKS_PROPERTIES);
    public static List<DeferredBlock<HorizontalDirectionalBlock>> FIREBRICK_QUOINS = createQuoins("firebrick", FIREBRICKS_PROPERTIES);
    // Normal brick quoins & specialty blocks
    public static List<DeferredBlock<SillBlock>> BRICK_SILLS = createSills("brick", BRICKS_PROPERTIES);
    public static List<DeferredBlock<HorizontalDirectionalBlock>> BRICK_QUOINS = createQuoins("brick", BRICKS_PROPERTIES);
    // Framed glass
    public static DeferredBlock<TransparentBlock> FRAMED_GLASS = BLOCKS.registerBlock("framed_glass", TransparentBlock::new, FRAMED_GLASS_PROPERTIES);
    public static DeferredBlock<IronBarsBlock> FRAMED_GLASS_PANE = BLOCKS.registerBlock("framed_glass_pane", IronBarsBlock::new, FRAMED_GLASS_PROPERTIES);
    public static List<DeferredBlock<StainedGlassBlock>> STAINED_FRAMED_GLASS = createStainedGlasses();
    public static List<DeferredBlock<StainedGlassPaneBlock>> STAINED_FRAMED_GLASS_PANES = createStainedGlassPanes();

    /*
     * Utility function for dyed blocks, etc
     */
    // Stained glasses
    public static List<DeferredBlock<StainedGlassBlock>> createStainedGlasses() {
        List<DeferredBlock<StainedGlassBlock>> blocks = new ArrayList<>();
        for(DyeColor color : DyeColor.values()) {
            DeferredBlock<StainedGlassBlock> block = BLOCKS.register(color.getName()+"_stained_framed_glass", () -> (new StainedGlassBlock(color, FRAMED_GLASS_PROPERTIES)));
            blocks.add(block);
        }
        return blocks;
    }
    public static List<DeferredBlock<StainedGlassPaneBlock>> createStainedGlassPanes() {
        List<DeferredBlock<StainedGlassPaneBlock>> blocks = new ArrayList<>();
        for(DyeColor color : DyeColor.values()) {
            DeferredBlock<StainedGlassPaneBlock> block = BLOCKS.register(color.getName()+"_stained_framed_glass_pane", () -> (new StainedGlassPaneBlock(color, FRAMED_GLASS_PROPERTIES)));
            blocks.add(block);
        }
        return blocks;
    }
    // Stone-variant blocks
    public static List<DeferredBlock<HorizontalDirectionalBlock>> createQuoins(String base, BlockBehaviour.Properties properties) {
        List<DeferredBlock<HorizontalDirectionalBlock>> blocks = new ArrayList<>();
        for (String type : stones) {
            DeferredBlock<HorizontalDirectionalBlock> block = BLOCKS.registerBlock(base + "_quoin_" + type, QuoinBlock::new, properties);
            blocks.add(block);
        }
        return blocks;
    }
    public static List<DeferredBlock<SillBlock>> createSills(String base, BlockBehaviour.Properties properties) {
        List<DeferredBlock<SillBlock>> blocks = new ArrayList<>();
        for (String type : stones) {
            DeferredBlock<SillBlock> block = BLOCKS.registerBlock(base + "_sill_" + type, SillBlock::new, properties);
            blocks.add(block);
        }
        return blocks;
    }
}

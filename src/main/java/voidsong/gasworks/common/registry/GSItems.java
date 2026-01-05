package voidsong.gasworks.common.registry;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.block.CandelabraBlock;
import voidsong.gasworks.common.block.SillBlock;
import voidsong.gasworks.common.item.BarometerItem;
import voidsong.gasworks.common.item.FireDrillItem;
import voidsong.gasworks.common.item.TradeswomansJournalItem;

import java.util.ArrayList;
import java.util.List;

public class GSItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gasworks.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gasworks.MOD_ID);

    /*
     * In-world processes, incl. beehive oven, brick clamp, & fuels/ash
     */
    // Log stacks for fuel
    public static final DeferredItem<BlockItem> OAK_LOG_PILE = ITEMS.registerSimpleBlockItem("oak_log_pile", GSBlocks.OAK_LOG_PILE);
    public static final DeferredItem<BlockItem> SPRUCE_LOG_PILE = ITEMS.registerSimpleBlockItem("spruce_log_pile", GSBlocks.SPRUCE_LOG_PILE);
    public static final DeferredItem<BlockItem> BIRCH_LOG_PILE = ITEMS.registerSimpleBlockItem("birch_log_pile", GSBlocks.BIRCH_LOG_PILE);
    public static final DeferredItem<BlockItem> JUNGLE_LOG_PILE = ITEMS.registerSimpleBlockItem("jungle_log_pile", GSBlocks.JUNGLE_LOG_PILE);
    public static final DeferredItem<BlockItem> ACACIA_LOG_PILE = ITEMS.registerSimpleBlockItem("acacia_log_pile", GSBlocks.ACACIA_LOG_PILE);
    public static final DeferredItem<BlockItem> DARK_OAK_LOG_PILE = ITEMS.registerSimpleBlockItem("dark_oak_log_pile", GSBlocks.DARK_OAK_LOG_PILE);
    public static final DeferredItem<BlockItem> CHERRY_LOG_PILE = ITEMS.registerSimpleBlockItem("cherry_log_pile", GSBlocks.CHERRY_LOG_PILE);
    public static final DeferredItem<BlockItem> MANGROVE_LOG_PILE = ITEMS.registerSimpleBlockItem("mangrove_log_pile", GSBlocks.MANGROVE_LOG_PILE);
    public static final DeferredItem<BlockItem> BAMBOO_LOG_PILE = ITEMS.registerSimpleBlockItem("bamboo_log_pile", GSBlocks.BAMBOO_LOG_PILE);
    // Coal stacks for fuel
    public static final DeferredItem<BlockItem> COAL_PILE = ITEMS.registerSimpleBlockItem("coal_pile", GSBlocks.COAL_PILE);
    public static final DeferredItem<BlockItem> CHARCOAL_PILE = ITEMS.registerSimpleBlockItem("charcoal_pile", GSBlocks.CHARCOAL_PILE);
    // Coal coke & products
    public static final DeferredItem<Item> COKE = ITEMS.registerSimpleItem("coke");
    public static final DeferredItem<Item> ASH = ITEMS.registerSimpleItem("ash");
    // Brick piles for firing
    public static final DeferredItem<BlockItem> UNFIRED_BRICK_CLAMP = ITEMS.registerSimpleBlockItem("unfired_brick_clamp", GSBlocks.UNFIRED_BRICK_CLAMP);
    public static final DeferredItem<BlockItem> FIRED_BRICK_CLAMP = ITEMS.registerSimpleBlockItem("fired_brick_clamp", GSBlocks.FIRED_BRICK_CLAMP);
    public static final DeferredItem<BlockItem> UNFIRED_FIREBRICK_CLAMP = ITEMS.registerSimpleBlockItem("unfired_firebrick_clamp", GSBlocks.UNFIRED_FIREBRICK_CLAMP);
    public static final DeferredItem<BlockItem> FIRED_FIREBRICK_CLAMP = ITEMS.registerSimpleBlockItem("fired_firebrick_clamp", GSBlocks.FIRED_FIREBRICK_CLAMP);
    // Compost piles for fertilizer
    public static final DeferredItem<BlockItem> COMPOST_PILE = ITEMS.registerSimpleBlockItem("compost_pile", GSBlocks.COMPOST_PILE);
    public static final DeferredItem<Item> COMPOST = ITEMS.registerItem("compost", BoneMealItem::new);
    /*
     * Building blocks, including various 'functional' blocks
     */
    // Fireclay blocks of various types
    public static final DeferredItem<Item> FIRECLAY_BALL = ITEMS.registerSimpleItem("fireclay_ball");
    public static final DeferredItem<Item> FIREBRICK = ITEMS.registerSimpleItem("firebrick");
    public static final DeferredItem<BlockItem> FIRECLAY = ITEMS.registerSimpleBlockItem("fireclay", GSBlocks.FIRECLAY);
    public static final DeferredItem<BlockItem> FIREBRICKS = ITEMS.registerSimpleBlockItem("firebricks", GSBlocks.FIREBRICKS);
    public static final DeferredItem<BlockItem> FIREBRICK_STAIRS = ITEMS.registerSimpleBlockItem("firebrick_stairs", GSBlocks.FIREBRICK_STAIRS);
    public static final DeferredItem<BlockItem> FIREBRICK_SLAB = ITEMS.registerSimpleBlockItem("firebrick_slab", GSBlocks.FIREBRICK_SLAB);
    public static final DeferredItem<BlockItem> FIREBRICK_WALL = ITEMS.registerSimpleBlockItem("firebrick_wall", GSBlocks.FIREBRICK_WALL);
    public static final List<DeferredItem<BlockItem>> FIREBRICK_SILLS = createSills(GSBlocks.FIREBRICK_SILLS);
    public static final List<DeferredItem<BlockItem>> FIREBRICK_QUOINS= createQuoins(GSBlocks.FIREBRICK_QUOINS);
    // Normal brick quoins & specialty blocks
    public static final List<DeferredItem<BlockItem>> BRICK_SILLS = createSills(GSBlocks.BRICK_SILLS);
    public static final List<DeferredItem<BlockItem>> BRICK_QUOINS= createQuoins(GSBlocks.BRICK_QUOINS);
    // Framed glass
    public static final DeferredItem<BlockItem> FRAMED_GLASS = ITEMS.registerSimpleBlockItem("framed_glass", GSBlocks.FRAMED_GLASS);
    public static final DeferredItem<BlockItem> FRAMED_GLASS_PANE = ITEMS.registerSimpleBlockItem("framed_glass_pane", GSBlocks.FRAMED_GLASS_PANE);
    public static List<DeferredItem<BlockItem>> STAINED_FRAMED_GLASS = createStainedGlasses();
    public static List<DeferredItem<BlockItem>> STAINED_FRAMED_GLASS_PANES = createStainedGlassPanes();
    // Candelabra
    public static DeferredItem<BlockItem> CANDELABRA = ITEMS.registerSimpleBlockItem("candelabra", GSBlocks.CANDELABRA);
    public static List<DeferredItem<BlockItem>> CANDELABRAS = createCandelabras();
    /*
     * Tool items & other useful items
     */
    public static final DeferredItem<Item> TRADESWOMANS_JOURNAL = ITEMS.registerItem("tradeswomans_journal", TradeswomansJournalItem::new, new Properties().stacksTo(1));
    public static final DeferredItem<Item> FIRE_DRILL = ITEMS.registerItem("fire_drill", FireDrillItem::new, new Properties().stacksTo(1));
    public static final DeferredItem<Item> BAROMETER = ITEMS.registerItem("barometer", BarometerItem::new, new Properties().stacksTo(1));

    // Creates a creative tab for the mod & adds all Natural Philosophy items to the tab
    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("gasworks_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.gasworks"))
        .icon(() -> new ItemStack(TRADESWOMANS_JOURNAL.asItem()))
        .displayItems((parameters, output) -> {
            // Resource items
            output.accept(COKE);
            output.accept(ASH);
            output.accept(COMPOST);
            output.accept(FIRECLAY_BALL);
            output.accept(FIREBRICK);
            // Blocks with specialty items
            output.accept(GSItems.CANDELABRA);
            output.acceptAll(CANDELABRAS.stream().map(item -> new ItemStack(item.get())).toList());
            // Tool items
            output.accept(TRADESWOMANS_JOURNAL);
            output.accept(FIRE_DRILL);
            output.accept(BAROMETER);
            // Blocks
            output.accept(OAK_LOG_PILE);
            output.accept(SPRUCE_LOG_PILE);
            output.accept(BIRCH_LOG_PILE);
            output.accept(JUNGLE_LOG_PILE);
            output.accept(ACACIA_LOG_PILE);
            output.accept(DARK_OAK_LOG_PILE);
            output.accept(CHERRY_LOG_PILE);
            output.accept(MANGROVE_LOG_PILE);
            output.accept(BAMBOO_LOG_PILE);
            output.accept(COAL_PILE);
            output.accept(CHARCOAL_PILE);
            output.accept(COMPOST_PILE);
            output.accept(UNFIRED_FIREBRICK_CLAMP);
            output.accept(FIRED_FIREBRICK_CLAMP);
            output.accept(UNFIRED_BRICK_CLAMP);
            output.accept(FIRED_BRICK_CLAMP);
            output.accept(FIRECLAY);
            output.accept(FIREBRICKS);
            output.accept(FIREBRICK_STAIRS);
            output.accept(FIREBRICK_SLAB);
            output.accept(FIREBRICK_WALL);
            output.acceptAll(FIREBRICK_SILLS.stream().map(item -> new ItemStack(item.get())).toList());
            output.acceptAll(FIREBRICK_QUOINS.stream().map(item -> new ItemStack(item.get())).toList());
            output.acceptAll(BRICK_SILLS.stream().map(item -> new ItemStack(item.get())).toList());
            output.acceptAll(BRICK_QUOINS.stream().map(item -> new ItemStack(item.get())).toList());
            output.accept(GSItems.FRAMED_GLASS);
            output.accept(GSItems.FRAMED_GLASS_PANE);
            output.acceptAll(STAINED_FRAMED_GLASS.stream().map(item -> new ItemStack(item.get())).toList());
            output.acceptAll(STAINED_FRAMED_GLASS_PANES.stream().map(item -> new ItemStack(item.get())).toList());
        }).build());

    /*
     * Utility function for dyed blocks, etc
     */
    // Stained glasses
    public static List<DeferredItem<BlockItem>> createStainedGlasses() {
        List<DeferredItem<BlockItem>> items = new ArrayList<>();
        for(DeferredBlock<StainedGlassBlock> block : GSBlocks.STAINED_FRAMED_GLASS) {
            DeferredItem<BlockItem> item = ITEMS.registerSimpleBlockItem(block.getRegisteredName().split(":")[1], block);
            items.add(item);
        }
        return items;
    }
    public static List<DeferredItem<BlockItem>> createStainedGlassPanes() {
        List<DeferredItem<BlockItem>> items = new ArrayList<>();
        for(DeferredBlock<StainedGlassPaneBlock> block : GSBlocks.STAINED_FRAMED_GLASS_PANES) {
            DeferredItem<BlockItem> item = ITEMS.registerSimpleBlockItem(block.getRegisteredName().split(":")[1], block);
            items.add(item);
        }
        return items;
    }
    // Candelabra
    public static List<DeferredItem<BlockItem>> createCandelabras() {
        List<DeferredItem<BlockItem>> items = new ArrayList<>();
        for(Pair<DyeColor, DeferredBlock<CandelabraBlock>> pair : GSBlocks.CANDELABRAS) {
            DeferredBlock<CandelabraBlock> block = pair.getSecond();
            DeferredItem<BlockItem> item = ITEMS.registerSimpleBlockItem(block.getRegisteredName().split(":")[1], block);
            items.add(item);
        }
        return items;
    }
    // Stone-variant blocks
    public static List<DeferredItem<BlockItem>> createQuoins(List<DeferredBlock<HorizontalDirectionalBlock>> list) {
        List<DeferredItem<BlockItem>> items = new ArrayList<>();
        for(DeferredBlock<HorizontalDirectionalBlock> block : list) {
            DeferredItem<BlockItem> item = ITEMS.registerSimpleBlockItem(block.getRegisteredName().split(":")[1], block);
            items.add(item);
        }
        return items;
    }
    public static List<DeferredItem<BlockItem>> createSills(List<DeferredBlock<SillBlock>> list) {
        List<DeferredItem<BlockItem>> items = new ArrayList<>();
        for(DeferredBlock<SillBlock> block : list) {
            DeferredItem<BlockItem> item = ITEMS.registerSimpleBlockItem(block.getRegisteredName().split(":")[1], block);
            items.add(item);
        }
        return items;
    }
}

package voidsong.gasworks.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;
import voidsong.gasworks.common.item.TradeswomansJournalItem;

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
    public static final DeferredItem<BlockItem> FIREBRICK_QUOIN_ANDESITE = ITEMS.registerSimpleBlockItem("firebrick_quoin_andesite", GSBlocks.FIREBRICK_QUOIN_ANDESITE);
    public static final DeferredItem<BlockItem> FIREBRICK_QUOIN_DEEPSLATE = ITEMS.registerSimpleBlockItem("firebrick_quoin_deepslate", GSBlocks.FIREBRICK_QUOIN_DEEPSLATE);
    public static final DeferredItem<BlockItem> FIREBRICK_QUOIN_DIORITE = ITEMS.registerSimpleBlockItem("firebrick_quoin_diorite", GSBlocks.FIREBRICK_QUOIN_DIORITE);
    public static final DeferredItem<BlockItem> FIREBRICK_QUOIN_GRANITE = ITEMS.registerSimpleBlockItem("firebrick_quoin_granite", GSBlocks.FIREBRICK_QUOIN_GRANITE);
    public static final DeferredItem<BlockItem> FIREBRICK_QUOIN_POLISHED_BLACKSTONE = ITEMS.registerSimpleBlockItem("firebrick_quoin_polished_blackstone", GSBlocks.FIREBRICK_QUOIN_POLISHED_BLACKSTONE);
    public static final DeferredItem<BlockItem> FIREBRICK_QUOIN_STONE = ITEMS.registerSimpleBlockItem("firebrick_quoin_stone", GSBlocks.FIREBRICK_QUOIN_STONE);
    public static final DeferredItem<BlockItem> FIREBRICK_QUOIN_TUFF = ITEMS.registerSimpleBlockItem("firebrick_quoin_tuff", GSBlocks.FIREBRICK_QUOIN_TUFF);
    // Normal brick quoins & specialty blocks
    public static final DeferredItem<BlockItem> BRICK_QUOIN_ANDESITE = ITEMS.registerSimpleBlockItem("brick_quoin_andesite", GSBlocks.BRICK_QUOIN_ANDESITE);
    public static final DeferredItem<BlockItem> BRICK_QUOIN_DEEPSLATE = ITEMS.registerSimpleBlockItem("brick_quoin_deepslate", GSBlocks.BRICK_QUOIN_DEEPSLATE);
    public static final DeferredItem<BlockItem> BRICK_QUOIN_DIORITE = ITEMS.registerSimpleBlockItem("brick_quoin_diorite", GSBlocks.BRICK_QUOIN_DIORITE);
    public static final DeferredItem<BlockItem> BRICK_QUOIN_GRANITE = ITEMS.registerSimpleBlockItem("brick_quoin_granite", GSBlocks.BRICK_QUOIN_GRANITE);
    public static final DeferredItem<BlockItem> BRICK_QUOIN_POLISHED_BLACKSTONE = ITEMS.registerSimpleBlockItem("brick_quoin_polished_blackstone", GSBlocks.BRICK_QUOIN_POLISHED_BLACKSTONE);
    public static final DeferredItem<BlockItem> BRICK_QUOIN_STONE = ITEMS.registerSimpleBlockItem("brick_quoin_stone", GSBlocks.BRICK_QUOIN_STONE);
    public static final DeferredItem<BlockItem> BRICK_QUOIN_TUFF = ITEMS.registerSimpleBlockItem("brick_quoin_tuff", GSBlocks.BRICK_QUOIN_TUFF);
    /*
     * Tool items & other useful items
     */
    public static final DeferredItem<Item> TRADESWOMANS_JOURNAL = ITEMS.registerItem("tradeswomans_journal", TradeswomansJournalItem::new, new Properties().stacksTo(1));

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
            // Tool items
            output.accept(TRADESWOMANS_JOURNAL);
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
            output.accept(FIREBRICK_QUOIN_ANDESITE);
            output.accept(FIREBRICK_QUOIN_DEEPSLATE);
            output.accept(FIREBRICK_QUOIN_DIORITE);
            output.accept(FIREBRICK_QUOIN_GRANITE);
            output.accept(FIREBRICK_QUOIN_POLISHED_BLACKSTONE);
            output.accept(FIREBRICK_QUOIN_STONE);
            output.accept(FIREBRICK_QUOIN_TUFF);
            output.accept(BRICK_QUOIN_ANDESITE);
            output.accept(BRICK_QUOIN_DEEPSLATE);
            output.accept(BRICK_QUOIN_DIORITE);
            output.accept(BRICK_QUOIN_GRANITE);
            output.accept(BRICK_QUOIN_POLISHED_BLACKSTONE);
            output.accept(BRICK_QUOIN_STONE);
            output.accept(BRICK_QUOIN_TUFF);
        }).build());
}

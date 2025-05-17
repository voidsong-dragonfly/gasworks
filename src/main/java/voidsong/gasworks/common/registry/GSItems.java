package voidsong.gasworks.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
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

    // Brick piles for firing
    public static final DeferredItem<BlockItem> UNFIRED_BRICK_CLAMP = ITEMS.registerSimpleBlockItem("unfired_brick_clamp", GSBlocks.UNFIRED_BRICK_CLAMP);
    public static final DeferredItem<BlockItem> FIRED_BRICK_CLAMP = ITEMS.registerSimpleBlockItem("fired_brick_clamp", GSBlocks.FIRED_BRICK_CLAMP);
    // Coal coke & products
    public static final DeferredItem<Item> COKE = ITEMS.registerSimpleItem("coke");
    public static final DeferredItem<Item> ASH = ITEMS.registerSimpleItem("ash");

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
            output.accept(OAK_LOG_PILE.get());
            output.accept(SPRUCE_LOG_PILE.get());
            output.accept(BIRCH_LOG_PILE.get());
            output.accept(JUNGLE_LOG_PILE.get());
            output.accept(ACACIA_LOG_PILE.get());
            output.accept(DARK_OAK_LOG_PILE.get());
            output.accept(CHERRY_LOG_PILE.get());
            output.accept(MANGROVE_LOG_PILE.get());
            output.accept(BAMBOO_LOG_PILE.get());
            output.accept(COAL_PILE.get());
            output.accept(CHARCOAL_PILE.get());
            output.accept(UNFIRED_BRICK_CLAMP.get());
            output.accept(FIRED_BRICK_CLAMP.get());
            output.accept(COKE.get());
            output.accept(ASH.get());
            output.accept(TRADESWOMANS_JOURNAL.get());
        }).build());
}

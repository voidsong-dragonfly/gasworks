package voidsong.gasworks.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;

public class GSItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gasworks.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gasworks.MOD_ID);

    public static final DeferredItem<BlockItem> OAK_LOG_PILE = ITEMS.registerSimpleBlockItem("oak_log_pile", GSBlocks.OAK_LOG_PILE);
    public static final DeferredItem<BlockItem> SPRUCE_LOG_PILE = ITEMS.registerSimpleBlockItem("spruce_log_pile", GSBlocks.SPRUCE_LOG_PILE);
    public static final DeferredItem<BlockItem> BIRCH_LOG_PILE = ITEMS.registerSimpleBlockItem("birch_log_pile", GSBlocks.BIRCH_LOG_PILE);
    public static final DeferredItem<BlockItem> JUNGLE_LOG_PILE = ITEMS.registerSimpleBlockItem("jungle_log_pile", GSBlocks.JUNGLE_LOG_PILE);
    public static final DeferredItem<BlockItem> ACACIA_LOG_PILE = ITEMS.registerSimpleBlockItem("acacia_log_pile", GSBlocks.ACACIA_LOG_PILE);
    public static final DeferredItem<BlockItem> DARK_OAK_LOG_PILE = ITEMS.registerSimpleBlockItem("dark_oak_log_pile", GSBlocks.DARK_OAK_LOG_PILE);
    public static final DeferredItem<BlockItem> CHERRY_LOG_PILE = ITEMS.registerSimpleBlockItem("cherry_log_pile", GSBlocks.CHERRY_LOG_PILE);
    public static final DeferredItem<BlockItem> MANGROVE_LOG_PILE = ITEMS.registerSimpleBlockItem("mangrove_log_pile", GSBlocks.MANGROVE_LOG_PILE);
    public static final DeferredItem<BlockItem> BAMBOO_LOG_PILE = ITEMS.registerSimpleBlockItem("bamboo_log_pile", GSBlocks.BAMBOO_LOG_PILE);

    // Creates a creative tab for the mod & adds all Natural Philosophy items to the tab
    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("gasworks_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.gasworks"))
        .icon(() -> new ItemStack(OAK_LOG_PILE.asItem()))
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
        }).build());
}

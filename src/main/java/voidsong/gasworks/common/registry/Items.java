package voidsong.gasworks.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import voidsong.gasworks.Gasworks;

public class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gasworks.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gasworks.MOD_ID);

    // Creates a creative tab for the mod & adds all Natural Philosophy items to the tab
    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("gasworks_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.gasworks"))
        .icon(() -> new ItemStack(net.minecraft.world.item.Items.IRON_BLOCK.asItem()))
        .displayItems((parameters, output) -> {
        }).build());
}

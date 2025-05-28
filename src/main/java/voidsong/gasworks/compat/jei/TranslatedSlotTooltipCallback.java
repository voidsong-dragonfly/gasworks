package voidsong.gasworks.compat.jei;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nonnull;

public class TranslatedSlotTooltipCallback implements IRecipeSlotRichTooltipCallback {
    private final MutableComponent component;

    public TranslatedSlotTooltipCallback(MutableComponent component) {
        this.component = component;
    }

    @Override
    public void onRichTooltip(@Nonnull IRecipeSlotView recipeSlotView, @Nonnull ITooltipBuilder tooltip) {
        tooltip.add(component.withStyle(ChatFormatting.GOLD));
    }
}

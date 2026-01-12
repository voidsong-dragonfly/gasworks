package voidsong.gasworks.common.item.utility;

import malte0811.dualcodecs.DualCodec;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeCodecs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import voidsong.gasworks.client.TextUtils;
import voidsong.gasworks.common.registry.GSDataComponents;
import voidsong.gasworks.common.registry.GSItems;

import javax.annotation.Nonnull;
import java.util.List;

public class TradeswomansJournalItem extends Item {
    public TradeswomansJournalItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack, @Nonnull LivingEntity entity) {
        return 50;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand usedHand) {
        // Grab the stack we're supposed to be working with
        ItemStack stack = player.getItemInHand(usedHand);
        // Try some fake-player detection
        if(player instanceof FakePlayer)
            return InteractionResultHolder.pass(stack);
        // Grab the stored experience for checks
        StoredExperience bookExperience = getExperienceStored(stack);
        // Fail the check if we have too much experience or no experience to learn
        if(player.experienceLevel == 0 && player.experienceProgress == 0 && !bookExperience.set) {
            player.displayClientMessage(Component.translatable("message.gasworks.no_experience"), true);
            return InteractionResultHolder.fail(stack);
        }
        if(bookExperience.set && (player.experienceLevel > bookExperience.level || (player.experienceLevel == bookExperience.level && player.experienceProgress > bookExperience.progress))) {
            player.displayClientMessage(Component.translatable("message.gasworks.too_much_experience"), true);
            return InteractionResultHolder.fail(stack);
        }
        // Start interaction
        player.startUsingItem(usedHand);
        return InteractionResultHolder.success(stack);
    }

    @Override
    @Nonnull
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity entityLiving) {
        // Can't do anything if this isn't a player, try some fake-player prevention
        if(!(entityLiving instanceof ServerPlayer player)||entityLiving instanceof FakePlayer)
            return stack;
        // Store book experience
        StoredExperience bookExperience = getExperienceStored(stack);
        // Store the player's experience
        int playerLevel = player.experienceLevel;
        float playerProgress = player.experienceProgress;
        // Set the player's experience & add a 1s cooldown on getting experience
        player.setExperiencePoints(0);
        player.setExperienceLevels(bookExperience.level);
        player.setExperiencePoints((int)(bookExperience.progress*player.getXpNeededForNextLevel()));
        player.getCooldowns().addCooldown(GSItems.TRADESWOMANS_JOURNAL.get(), 20);
        // Set the book's experience
        stack.set(GSDataComponents.EXPERIENCE_DATA, new StoredExperience(playerLevel, playerProgress, playerLevel != 0 || playerProgress != 0));
        // Return
        return stack;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext ctx, List<Component> list, @Nonnull TooltipFlag flag) {
        StoredExperience bookExperience = getExperienceStored(stack);
        float experience = bookExperience.level + bookExperience.progress;
        list.add(TextUtils.applyFormat(Component.translatable("tooltip.gasworks.experience"), ChatFormatting.GRAY)
            .append(" ")
            .append(TextUtils.applyFormat(
                Component.translatable("tooltip.gasworks.experience_levels", ((int)(experience*100.0))/100.0f), ChatFormatting.GREEN
            )));
    }

    public static StoredExperience getExperienceStored(ItemStack journal) {
        return journal.getOrDefault(GSDataComponents.EXPERIENCE_DATA, new StoredExperience(0, 0, false));
    }

    public record StoredExperience(int level, float progress, boolean set) {
        public static final DualCodec<RegistryFriendlyByteBuf, StoredExperience> CODECS = DualCompositeCodecs.composite(
            DualCodecs.INT.fieldOf("level"), StoredExperience::level,
            DualCodecs.FLOAT.fieldOf("progress"), StoredExperience::progress,
            DualCodecs.BOOL.fieldOf("set"), StoredExperience::set,
            StoredExperience::new
        );
    }
}

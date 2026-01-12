package voidsong.gasworks.common.item.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.FakePlayer;
import voidsong.gasworks.api.GSTags;

import javax.annotation.Nonnull;

public class FireDrillItem extends Item {
    public FireDrillItem(Properties properties) {
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
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        // Get the player
        Player player = context.getPlayer();
        // Try some fake-player detection
        if(player == null || player instanceof FakePlayer)
            return InteractionResult.PASS;
        // Start interaction
        player.startUsingItem(context.getHand());
        return InteractionResult.CONSUME;
    }

    @Override
    public void onUseTick(@Nonnull Level level, @Nonnull LivingEntity entity, @Nonnull ItemStack stack, int remainingUseDuration) {
        // If the using entity isn't a fake & still has time to use, continue processing
        if (remainingUseDuration >= 0 && entity instanceof Player player && !player.isFakePlayer() && !(player instanceof FakePlayer)) {
            // Grab the block hit result at the player's view vector
            HitResult hit = ProjectileUtil.getHitResultOnViewVector(
                player, e -> !e.isSpectator() && e.isPickable(), player.blockInteractionRange()
            );
            // If it's actually a block, continue to use the item
            if (hit instanceof BlockHitResult context && hit.getType().equals(HitResult.Type.BLOCK)) {
                // Cancel early if we still need to use the item more
                if (remainingUseDuration > 1) return;
                // Otherwise, get the context variables we need
                BlockPos pos = context.getBlockPos();
                BlockState state = level.getBlockState(pos);
                // Get the lit state for this state
                BlockState litState = state.getToolModifiedState(new UseOnContext(player, player.getUsedItemHand(), context), ItemAbilities.FIRESTARTER_LIGHT, false);
                // Check if we can light the block in front of us & if the block has a lit state
                if (litState != null && state.is(GSTags.BlockTags.CONTAINS_TINDER)) {
                    // Play sound and set for lighting
                    level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                    level.setBlockAndUpdate(pos, litState);
                    // Release item use
                    entity.releaseUsingItem();
                }
                // Consume the item
                player.setItemInHand(player.getUsedItemHand(), ItemStack.EMPTY);
                player.causeFoodExhaustion(2f);
            }
        }
        // Release the item use now that we're done using
        entity.releaseUsingItem();
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, ItemAbility itemAbility) {
        return itemAbility.equals(ItemAbilities.FIRESTARTER_LIGHT);
    }
}

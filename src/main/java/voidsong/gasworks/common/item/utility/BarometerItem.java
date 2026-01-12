package voidsong.gasworks.common.item.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.Nonnull;

public class BarometerItem extends Item {
    private final int MAXIMUM_TEMPERATURE_CELSIUS = 100;

    public BarometerItem(Properties properties) {
        super(properties);
    }


    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand usedHand) {
        // Grab the stack we're supposed to be working with & the position
        BlockPos pos = player.getOnPos().offset(0, 2, 0);
        ItemStack stack = player.getItemInHand(usedHand);
        // Try some fake-player detection
        if(player.isFakePlayer() || player instanceof FakePlayer)
            return InteractionResultHolder.pass(stack);
        // Check conditions for what kind of information the kit can gather
        boolean fluidlogged = !level.getFluidState(pos).isEmpty();
        boolean disallowedFluid = fluidlogged && level.getFluidState(pos).getFluidType().getTemperature() > MAXIMUM_TEMPERATURE_CELSIUS + 273;
        boolean wall = !(level.getBlockState(pos).canBeReplaced() || level.getBlockState(pos).getCollisionShape(level, pos).isEmpty());
        // Check fluid depth for temperate fluids
        if (fluidlogged && !disallowedFluid) {
            // Depth marker & fluid type to check against
            int depth = 1;
            FluidType type = level.getFluidState(pos).getFluidType();
            // First check
            FluidState check = level.getFluidState(pos.above());
            boolean allowed = !check.isEmpty() && check.getFluidType().equals(type);
            // Loop to find fluid level top
            while (allowed) {
                depth += 1;
                check = level.getFluidState(pos.offset(0, depth, 0));
                allowed = !check.isEmpty() && check.getFluidType().equals(type);
            }
            // Check to see if we can sense depth here; we need 1 block of air above to form a surface and not be
            // determining things from cave depth, which is "unreliable" because I can't easily search my way to the top
            BlockState above = level.getBlockState(pos.offset(0, depth + 1, 0));
            boolean freeSurface = above.isAir() || above.canBeReplaced() || above.getCollisionShape(level, pos.offset(0, depth + 1, 0)).isEmpty();
            // Check if the feet are waterlogged
            boolean feet = level.getFluidState(pos.below()).getFluidType().equals(type);
            // Display depth
            if (freeSurface) {
                player.displayClientMessage(Component.translatable("message.gasworks.fluid_depth", feet ? depth + 1 : depth), true);
                return InteractionResultHolder.success(stack);
            }
        // Otherwise, check altitude if in air
        } else if (!wall && !disallowedFluid) {
            int altitude = pos.getY() - level.getSeaLevel();
            if (altitude >= 0)
                player.displayClientMessage(Component.translatable("message.gasworks.altitude_above", altitude), true);
            else
                player.displayClientMessage(Component.translatable("message.gasworks.altitude_below", -altitude), true);
            return InteractionResultHolder.success(stack);
        }
        // If we can't get altitude or fluid depth, return unreliable pressure & fail
        player.displayClientMessage(Component.translatable("message.gasworks.unknown_altitude"), true);
        return InteractionResultHolder.fail(stack);
    }
}

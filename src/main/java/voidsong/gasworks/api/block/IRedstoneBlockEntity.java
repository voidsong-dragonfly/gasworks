package voidsong.gasworks.api.block;

import net.minecraft.core.Direction;

public interface IRedstoneBlockEntity {
    default boolean canConnectRedstone(Direction side) {
        return false;
    }

    default int getWeakRedstoneOutput(Direction side) {
        return 0;
    }

    default int getStrongRedstoneOutput(Direction side) {
        return 0;
    }
}

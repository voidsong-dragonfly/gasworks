package voidsong.gasworks.api.block;

import net.minecraft.core.Direction;

public interface IReceivesIntermittentRotation {
    void onIntermittentRotate();
    default boolean acceptsFromSide(Direction side) {
        return side.equals(Direction.DOWN);
    }
}

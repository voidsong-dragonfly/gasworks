package voidsong.gasworks.common.block.properties;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class GSProperties {
    // Generic properties; facing, multiblock, etc
    public static final DirectionProperty FACING_ALL = DirectionProperty.create("facing",Direction.values());
    public static final DirectionProperty FACING_HORIZONTAL = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final DirectionProperty FACING_TOP_DOWN = DirectionProperty.create("facing", Direction.UP, Direction.DOWN);

    public static final BooleanProperty MULTIBLOCK_SLAVE = BooleanProperty.create("multiblock_slave");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");
    public static final BooleanProperty MIRRORED = BooleanProperty.create("mirrored");

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    // Specialty properties used in specific blocks
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty SMOLDERING = BooleanProperty.create("smoldering");
}

package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import org.antlr.v4.runtime.misc.Triple;
import voidsong.gasworks.common.block.properties.GSProperties;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class GearedTurntableBlock extends Block {
    public static final EnumProperty<Rotation> ROTATION_TYPE = EnumProperty.create("rotation", Rotation.class);

    public GearedTurntableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(GSProperties.FACING_TOP_DOWN, Direction.UP)
                .setValue(ROTATION_TYPE, Rotation.NONE)
                .setValue(BlockStateProperties.POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ROTATION_TYPE, GSProperties.FACING_TOP_DOWN, BlockStateProperties.POWERED);
    }

    @Override
    @Nonnull
    protected ItemInteractionResult useItemOn(ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) {
            Rotation type = switch(state.getValue(ROTATION_TYPE)) {
                case NONE -> Rotation.CLOCKWISE_90;
                case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
                case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_180;
                case CLOCKWISE_180 -> Rotation.NONE;
            };
            level.playSound(player, pos, type.equals(Rotation.NONE) ? SoundEvents.STONE_BUTTON_CLICK_OFF : SoundEvents.STONE_BUTTON_CLICK_ON, SoundSource.BLOCKS);
            level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
            level.setBlockAndUpdate(pos, state.setValue(ROTATION_TYPE, type));
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return defaultBlockState().setValue(GSProperties.FACING_TOP_DOWN, context.getNearestLookingVerticalDirection().getOpposite());
    }

    @Override
    protected void onPlace(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel serverlevel) {
            this.checkAndRotate(state, serverlevel, pos);
        }
    }

    @Override
    protected void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighborPos, boolean movedByPiston) {
        if (level instanceof ServerLevel server) {
            this.checkAndRotate(state, server, pos);
        }
    }

    public void checkAndRotate(BlockState state, ServerLevel level, BlockPos pos) {
        boolean redstone = level.hasNeighborSignal(pos);
        // Check if we have changed state  and if we can even rotate
        if (redstone != state.getValue(BlockStateProperties.POWERED)) {
            // Get rotation
            Rotation rotation = state.getValue(ROTATION_TYPE);
            // If we currently have power, aren't set to no-rotation, rotate
            if (redstone && !rotation.equals(Rotation.NONE)) {
                MutableBlockPos facingPos = pos.relative(state.getValue(GSProperties.FACING_TOP_DOWN)).mutable();
                BlockState facingState = level.getBlockState(facingPos);
                // Rotate the state above and save it until we make changes
                BlockState rotatedState = facingState.rotate(level,facingPos, state.getValue(ROTATION_TYPE));
                // Check if external block rotational is possible, and what states we have to care about rotating
                Map<Direction, Triple<PushReaction, BlockPos, BlockState>> test = new HashMap<>();
                Direction start = Direction.NORTH;
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    // Move the position to the exterior one & get the state
                    facingPos.move(direction);
                    BlockState adjacentState = level.getBlockState(facingPos);
                    PushReaction reaction = adjacentState.getPistonPushReaction();
                    // TODO: Ladder Block, Vines Block, WallSign Block, possibly FireBlock as a stretch goal, Coral Fans, Torches
                    // FaceAttachedDirectionalBlock handling
                    if (adjacentState.getBlock() instanceof FaceAttachedHorizontalDirectionalBlock && adjacentState.getValue(FaceAttachedHorizontalDirectionalBlock.FACING).equals(direction)) {
                        test.put(direction, new Triple<>(PushReaction.NORMAL, facingPos.immutable(), adjacentState));
                    // MultifaceBlock handling
                    } else if (adjacentState.getBlock() instanceof MultifaceBlock && adjacentState.getValue(MultifaceBlock.getFaceProperty(direction))) {
                        test.put(direction, new Triple<>(PushReaction.NORMAL, facingPos.immutable(), adjacentState));
                    // If the block is empty, pass ignore
                    } else if (adjacentState.isEmpty()) {
                        test.put(direction, new Triple<>(PushReaction.IGNORE, facingPos.immutable(), adjacentState));
                    // If it's marked as absolute immobile, exit early
                    } else if (blocksTurntableFaceRotation(adjacentState, reaction)) {
                        test.put(direction, new Triple<>(PushReaction.BLOCK, facingPos.immutable(), adjacentState));
                        start = direction;
                    // Otherwise, the rest can pass through (ignore/destroy)
                    } else {
                        test.put(direction, new Triple<>(reaction, facingPos.immutable(), adjacentState));
                    }
                    // Move the position back to the center
                    facingPos.move(direction.getOpposite());
                }
                // Place the rotated state without updates other than to send to the client
                level.setBlock(facingPos, rotatedState, 2);
                // Iterate rotation direction over the states as necessary
                Direction lead = rotation.rotate(start);
                // We handle flip-flops specially
                if (rotation.equals(Rotation.CLOCKWISE_180)) {

                // Otherwise, we can use a more standardized rotation handler
                } else {
                    Triple<PushReaction, BlockPos, BlockState> rotateIntoResult = test.get(lead);
                    Triple<PushReaction, BlockPos, BlockState> currentResult;
                    // We run until we go to the block across from the start, which is the one that leads
                    for (int place = 0; place < 4; place++) {
                        // Grab the current rotation results
                        Direction current = rotation.rotate(lead).getOpposite();
                        currentResult = test.get(current);
                        // Do resource pop/etc if the block can be popped off, otherwise mark as immobile and move on
                        if (currentResult.a.equals(PushReaction.NORMAL)) {
                            // Pop off blocks as necessary, for destroy and place results; mark as immobile
                            if (rotateIntoResult.a.equals(PushReaction.BLOCK)) {
                                // We check the actual block result, not the "rotation" result, destroy gets popped
                                if (currentResult.c.getPistonPushReaction().equals(PushReaction.DESTROY)) {
                                    level.destroyBlock(currentResult.b, true, null, 0);
                                // Anything that's not IGNORE gets marked as immobile
                                } else if (!currentResult.c.getPistonPushReaction().equals(PushReaction.IGNORE)) {
                                    currentResult = new Triple<>(PushReaction.BLOCK, currentResult.b, currentResult.c);
                                }
                            // If we rotate a block into a destroyable block, pop that block
                            } else if (rotateIntoResult.a.equals(PushReaction.DESTROY)) {
                                level.destroyBlock(rotateIntoResult.b, true, null, 0);
                            }
                            // Do the actual rotation into the new place, and set the current block to air
                            if (!rotateIntoResult.a.equals(PushReaction.BLOCK)) {
                                // Set rotated block
                                // TODO: handle waterlogging properly, instead of moving waterlogged states
                                level.setBlockAndUpdate(rotateIntoResult.b, getRotatedState(level, currentResult.b, currentResult.c, rotation));
                                // Set current block to air
                                level.removeBlock(currentResult.b, true);
                            }
                        }
                        // Move one step backwards and go back to the start of the loop
                        lead = current;
                        rotateIntoResult = currentResult;
                    }
                }
                // Place the rotated state one final time, with updates; this will update the nearby states as well
                level.setBlockAndUpdate(facingPos, rotatedState);
            }
            // Update the powered property as necessary at the end
            level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, redstone));
        }
    }

    public static boolean blocksTurntableFaceRotation(BlockState state, PushReaction reaction) {
        return state.is(Tags.Blocks.RELOCATION_NOT_SUPPORTED) || reaction.equals(PushReaction.BLOCK) || reaction.equals(PushReaction.PUSH_ONLY) || reaction.equals(PushReaction.NORMAL);
    }

    public static BlockState getRotatedState(LevelAccessor level, BlockPos pos, BlockState before, Rotation rotation) {
        Block block = before.getBlock();
        // TODO: what we need to do for placement depends upon block class
        if (block instanceof MultifaceBlock) {

        }
        // If none of the above apply, we apply a simple rotation
        return before.rotate(level, pos, rotation);
    }
}

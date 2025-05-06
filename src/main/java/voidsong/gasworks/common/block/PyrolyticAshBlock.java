package voidsong.gasworks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import voidsong.gasworks.common.block.properties.AshType;

import javax.annotation.Nonnull;

public class PyrolyticAshBlock extends ColoredFallingBlock {
    public static final EnumProperty<AshType> ASH_TYPE = EnumProperty.create("type", AshType.class);

    public PyrolyticAshBlock(ColorRGBA color, BlockBehaviour.Properties properties) {
        super(color, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ASH_TYPE, AshType.CHARCOAL));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ASH_TYPE);
    }

    @Override
    public int getFireSpreadSpeed(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return 3;
    }

    @Override
    public int getFlammability(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction direction) {
        return 3;
    }
}

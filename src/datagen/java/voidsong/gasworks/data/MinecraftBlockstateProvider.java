package voidsong.gasworks.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MinecraftBlockstateProvider extends ExtendedBlockstateProvider {
    public MinecraftBlockstateProvider(PackOutput output, ExistingFileHelper exHelper) {
        super(output, "minecraft", exHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        multiEightCubeAll(Blocks.BRICKS, rl("bricks/bricks"));
        stairsMultiEightAll((StairBlock)Blocks.BRICK_STAIRS, rl("bricks/bricks"));
        slabMultiEightAll((SlabBlock)Blocks.BRICK_SLAB, rl("bricks/bricks"));
        wallMultiEight((WallBlock)Blocks.BRICK_WALL, rl("bricks/bricks"), rl("bricks/bricks_wall"), rl("bricks/bricks_top"), false);
    }
}

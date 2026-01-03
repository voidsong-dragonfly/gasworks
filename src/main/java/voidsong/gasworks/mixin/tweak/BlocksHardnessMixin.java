package voidsong.gasworks.mixin.tweak;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksHardnessMixin {

    /*
     * Methods for modifying the yellow sandstone blockset; sandstone is increased from 0.8/0.8 & slabs are reduced
     * from 2.0/6.0 to 1.25/4.2 to match terracotta and other "weak" stones. Granite/etc have constant properties across the
     * blockset, & thus we strive for consistency here as well
     */

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=sandstone", ordinal = 0)))
    private static BlockBehaviour.Properties changeSandstoneStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chiseled_sandstone", ordinal = 0)))
    private static BlockBehaviour.Properties changeChiseledSandstoneStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cut_sandstone", ordinal = 0)))
    private static BlockBehaviour.Properties changeCutSandstoneStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=smooth_sandstone", ordinal = 0)))
    private static BlockBehaviour.Properties changeSmoothSandstoneStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SlabBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=sandstone_slab", ordinal = 0)))
    private static BlockBehaviour.Properties changeSandstoneSlabStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SlabBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cut_sandstone_slab", ordinal = 0)))
    private static BlockBehaviour.Properties changeCutSandstoneSlabStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    /*
     * Methods for modifying the red sandstone blockset; red sandstone is given consistency with yellow sandstone
     */

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=red_sandstone", ordinal = 0)))
    private static BlockBehaviour.Properties changeRedSandstoneStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chiseled_red_sandstone", ordinal = 0)))
    private static BlockBehaviour.Properties changeChiseledRedSandstoneStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cut_red_sandstone", ordinal = 0)))
    private static BlockBehaviour.Properties changeCutRedSandstoneStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=smooth_red_sandstone", ordinal = 0)))
    private static BlockBehaviour.Properties changeSmoothRedSandstoneStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SlabBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=red_sandstone_slab", ordinal = 0)))
    private static BlockBehaviour.Properties changeRedSandstoneSlabStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SlabBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cut_red_sandstone_slab", ordinal = 0)))
    private static BlockBehaviour.Properties changeCutRedSandstoneSlabStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    /*
     * Methods for modifying other stones without direct blocksets that should be part of the less-sturdy stones;
     * for now this is dripstone and calcite
     */

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=calcite", ordinal = 0)))
    private static BlockBehaviour.Properties changeCalciteStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=dripstone_block", ordinal = 0)))
    private static BlockBehaviour.Properties changeDripstoneBlockStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.25f, 4.2f);
    }

    /*
     * Methods for modifying the quartz blockset; quartz is increased from 0.8/0.8 & smooth/slabs are reduced
     * from 2.0/6.0 to 1.5/1.5 to match amethyst as another form of quartz. Granite/etc have constant properties across
     * the blockset, & thus we strive for consistency here as well
     */

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "floatValue=0.8", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=quartz_block", ordinal = 0)))
    private static float changeQuartzStrength(float oldStrength) {
        return 1.5f;
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "floatValue=0.8", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chiseled_quartz_block", ordinal = 0)))
    private static float changeChiseledQuartzStrength(float oldStrength) {
        return 1.5f;
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "floatValue=0.8", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=quartz_pillar", ordinal = 0)))
    private static float changeQuartzPillarStrength(float oldStrength) {
        return 1.5f;
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=smooth_quartz", ordinal = 0)))
    private static BlockBehaviour.Properties changeSmoothQuartzStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.5f);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SlabBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=quartz_slab", ordinal = 0)))
    private static BlockBehaviour.Properties changeQuartzSlabStrength(BlockBehaviour.Properties properties) {
        return properties.strength(1.5f);
    }

    /*
     * Methods for modifying the ice blockset. Ice is 0.5/0.5, blue ice is 2.8/2.8. Packed ice is modified to be in the
     * middle of the two, while frosted ice is dropped down to that of glass
     */

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "floatValue=0.5", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=frosted_ice", ordinal = 0)))
    private static float changeFrostedIceStrength(float oldStrength) {
        return 0.3f;
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "floatValue=0.5", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=packed_ice", ordinal = 0)))
    private static float changePackedIceStrength(float oldStrength) {
        return 1.5f;
    }

    /*
     * Methods for adjusting the blast resistance of a few blocks with noticeably low blast resistance compared to
     * the expected value, such as Beacons (3.0, vs obs/enchanting tables at 1200), or Lapis Blocks (3.0, vs all other
     * resource blocks at 6.0)
     */

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=lapis_block", ordinal = 0)))
    private static BlockBehaviour.Properties changeLapisBlockBlastResistance(BlockBehaviour.Properties properties) {
        return properties.explosionResistance(6.0F);
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BeaconBlock;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beacon", ordinal = 0)))
    private static BlockBehaviour.Properties changeBeaconBlastResistance(BlockBehaviour.Properties properties) {
        return properties.explosionResistance(1200F);
    }

    /*
     * Methods for adjusting strength/blast resistance of the eclectic blocks outside of a defined blockset, incl.
     * Rooted Dirt (0.5 to 0.6, other roots are 0.7), Dried Kelp (BR 2.5 to 1.0, hay bales are 0.5 & kelp is sturdier),
     * Daylight Detectors (0.2 to 1.5, same as bookshelves), Tinted Glass (0.3 to 0.4, to be > glass)
     *
     */

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "floatValue=0.5", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=rooted_dirt", ordinal = 0)))
    private static float changeRootedDirtStrength(float oldStrength) {
        return 0.6f;
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;<init>(Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)V", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=dried_kelp_block", ordinal = 0)))
    private static BlockBehaviour.Properties changeDriedKelpBlastResistance(BlockBehaviour.Properties properties) {
        return properties.explosionResistance(1.0F);
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "floatValue=0.2", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=daylight_detector", ordinal = 0)))
    private static float changeDaylightDetectorStrength(float oldStrength) {
        return 1.5f;
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "floatValue=0.3", ordinal = 0),
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=tinted_glass", ordinal = 0)))
    private static float changeTintedGlassStrength(float oldStrength) {
        return 0.4f;
    }
}

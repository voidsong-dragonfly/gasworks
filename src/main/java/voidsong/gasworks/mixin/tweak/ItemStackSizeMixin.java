package voidsong.gasworks.mixin.tweak;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public class ItemStackSizeMixin {
    /*
     * Increase potion stack size to 16 to encourage their use by making them not a massive pain to store en masse
     * and not a huge drain on inventory space for use in combat
     */

    @ModifyExpressionValue(method = "<clinit>", at = {
                     @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "base"),
                     @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "splash"),
                     @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "lingering")},
            slice = {@Slice(from = @At(value = "CONSTANT", args = "stringValue=potion", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=glass_bottle", ordinal = 0), id="base"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=splash_potion", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=spectral_arrow", ordinal = 0), id="splash"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=lingering_potion", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=shield", ordinal = 0), id="lingering")})
    private static int potionStackSize(int original) {
        return 16;
    }

    /*
     * Increase enchanted book stack size to 16 to make enchanting less of a pain to work with, and to encourage storing
     * enchanted books rather than tossing them
     */

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=enchanted_book", ordinal = 0),
                             to = @At(value = "CONSTANT", args = "stringValue=nether_brick", ordinal = 0)))
    private static int enchantedBookStackSize(int original) {
        return 16;
    }

    /*
     * Increase record stack size to make keeping them less of a pain when out exploring
     */

    @ModifyExpressionValue(method = "<clinit>", at = {
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "13"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "cat"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "blocks"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "chirp"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "creator"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "creator_music_box"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "far"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "mall"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "mellohi"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "stal"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "strad"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "ward"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "11"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "wait"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "otherside"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "relic"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "5"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "pigstep"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "precipice")},
            slice = {@Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_13", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_cat", ordinal = 0), id="13"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_cat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_blocks", ordinal = 0), id="cat"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_blocks", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_chirp", ordinal = 0), id="blocks"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_chirp", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_creator", ordinal = 0), id="chirp"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_creator", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_creator_music_box", ordinal = 0), id="creator"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_creator_music_box", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_far", ordinal = 0), id="creator_music_box"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_far", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_mall", ordinal = 0), id="far"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_mall", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_mellohi", ordinal = 0), id="mall"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_mellohi", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_stal", ordinal = 0), id="mellohi"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_stal", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_strad", ordinal = 0), id="stal"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_strad", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_ward", ordinal = 0), id="strad"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_ward", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_11", ordinal = 0), id="ward"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_11", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_wait", ordinal = 0), id="11"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_wait", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_otherside", ordinal = 0), id="wait"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_otherside", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_relic", ordinal = 0), id="otherside"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_relic", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_5", ordinal = 0), id="relic"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_5", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_pigstep", ordinal = 0), id="5"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_pigstep", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=music_disc_precipice", ordinal = 0), id="pigstep"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=music_disc_precipice", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=disc_fragment_5", ordinal = 0), id="precipice")})
    private static int musicDiscStackSize(int original) {
        return 16;
    }

    /*
     * Increase Minecart, Boat, & Saddle stack size to make working/transport with them less of a pain
     */

    @ModifyExpressionValue(method = "<clinit>", at = {
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "base"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "furnace"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "tnt"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "hopper")},
            slice = {@Slice(from = @At(value = "CONSTANT", args = "stringValue=minecart", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=chest_minecart", ordinal = 0), id="base"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=chest_minecart", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=furnace_minecart", ordinal = 0), id="chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=furnace_minecart", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=tnt_minecart", ordinal = 0), id="furnace"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=tnt_minecart", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=hopper_minecart", ordinal = 0), id="tnt"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=hopper_minecart", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=carrot_on_a_stick", ordinal = 0), id="hopper")})
    private static int minecartStackSize(int original) {
        return 16;
    }

    @ModifyExpressionValue(method = "<clinit>", at = {
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "oak"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "oak_chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "spruce"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "spruce_chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "birch"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "birch_chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "jungle"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "jungle_chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "acacia"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "acacia_chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "cherry"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "cherry_chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "dark_oak"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "dark_oak_chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "mangrove"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "mangrove_chest"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 0, slice = "bamboo"),
            @At(value = "CONSTANT", args = "intValue=1", ordinal = 1, slice = "bamboo_chest")},
            slice = {@Slice(from = @At(value = "CONSTANT", args = "stringValue=oak_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=oak_chest_boat", ordinal = 0), id="oak"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=oak_chest_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=spruce_boat", ordinal = 0), id="oak_chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=spruce_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=spruce_chest_boat", ordinal = 0), id="spruce"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=spruce_chest_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=birch_boat", ordinal = 0), id="spruce_chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=birch_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=birch_chest_boat", ordinal = 0), id="birch"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=birch_chest_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=jungle_boat", ordinal = 0), id="birch_chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=jungle_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=jungle_chest_boat", ordinal = 0), id="jungle"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=jungle_chest_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=acacia_boat", ordinal = 0), id="jungle_chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=acacia_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=acacia_chest_boat", ordinal = 0), id="acacia"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=acacia_chest_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=cherry_boat", ordinal = 0), id="acacia_chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=cherry_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=cherry_chest_boat", ordinal = 0), id="cherry"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=cherry_chest_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=dark_oak_boat", ordinal = 0), id="cherry_chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=dark_oak_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=dark_oak_chest_boat", ordinal = 0), id="dark_oak"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=dark_oak_chest_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=mangrove_boat", ordinal = 0), id="dark_oak_chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=mangrove_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=mangrove_chest_boat", ordinal = 0), id="mangrove"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=mangrove_chest_boat", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=bamboo_raft", ordinal = 0), id="mangrove_chest"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=bamboo_raft", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=bamboo_chest_raft", ordinal = 0), id="bamboo"),
                     @Slice(from = @At(value = "CONSTANT", args = "stringValue=bamboo_chest_raft", ordinal = 0),
                              to = @At(value = "CONSTANT", args = "stringValue=turtle_helmet", ordinal = 0), id="bamboo_chest")})
    private static int boatStackSize(int original) {
        return 16;
    }

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=saddle", ordinal = 0),
                             to = @At(value = "CONSTANT", args = "stringValue=minecart", ordinal = 0)))
    private static int saddleStackSize(int original) {
        return 16;
    }
}

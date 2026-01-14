package voidsong.gasworks.api.workshop;

import io.netty.buffer.ByteBuf;
import malte0811.dualcodecs.*;
import net.minecraft.resources.ResourceLocation;
import voidsong.gasworks.api.utils.GSDualCodecs;

/**
 * A task that a Workshop system can perform or can be expected to perform. Will correspond to a machine.
 * An example would be the Vanilla Grindstone, which provides {@link PrecisionLevel#CRUDE} grinding capabilities.
 * @param type {@link ResourceLocation} of the expected action, such as `gasworks:grind`
 * @param precision {@link PrecisionLevel} precision provide or expect
 * @param soulforge boolean for requiring or providing soul-fire capabilities. Used for HSNA.
 */
public record WorkshopAction(ResourceLocation type, PrecisionLevel precision, boolean soulforge) {
    public static final DualCodec<ByteBuf, WorkshopAction> CODECS = DualCompositeCodecs.composite(
            DualCodecs.RESOURCE_LOCATION.fieldOf("type"), WorkshopAction::type,
            GSDualCodecs.forEnum(PrecisionLevel.values()).fieldOf("precision"), WorkshopAction::precision,
            DualCodecs.BOOL.fieldOf("soulforge"), WorkshopAction::soulforge,
            WorkshopAction::new
    );

    /**
     * The level of precision that a WorkshopAction either requires (in a recipe) or provides (from a machine).
     * For workshop machines which ignore traditional precision, use {@link PrecisionLevel#IGNORE}
     */
    public enum PrecisionLevel {
        CRUDE, BASIC, PRECISION, MICROMETER, IGNORE;

        private boolean precisionMatchesOrExceeds(PrecisionLevel precision) {
            return this.ordinal() >= precision.ordinal();
        }
    }

    /**
     * Checks if this action can perform the attempted action passed in.
     * Expected use case is for the Workbench in question to store the maximum of all accessible WorkshopActions within
     * range, and to check the recipe's attempted actions against that master list.
     * @param attempted The {@link WorkshopAction} that we want to be able to perform
     * @return boolean whether this action can perform the attempted action
     */
    public boolean canPerform(WorkshopAction attempted) {
        return this.type.equals(attempted.type) && precision.precisionMatchesOrExceeds(attempted.precision) && (!attempted.soulforge || this.soulforge);
    }
}

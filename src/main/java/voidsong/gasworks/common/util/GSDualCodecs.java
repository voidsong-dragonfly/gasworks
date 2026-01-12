package voidsong.gasworks.common.util;

import io.netty.buffer.ByteBuf;
import malte0811.dualcodecs.DualCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

public class GSDualCodecs {
    public static final DualCodec<ByteBuf, Integer> POSITIVE_INT = new DualCodec<>(ExtraCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT);
    public static final DualCodec<ByteBuf, Integer> NON_NEGATIVE_INT = new DualCodec<>(ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
}

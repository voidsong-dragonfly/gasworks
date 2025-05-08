package voidsong.gasworks.common.block.properties;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;

public enum AshType implements StringRepresentable {
    NONE("none"),
    COKE("coke"),
    CHARCOAL("charcoal");

    private final String name;

    AshType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.name;
    }
}
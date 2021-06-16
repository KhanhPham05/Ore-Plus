package com.khanhpham.api;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum FuelStates implements IStringSerializable {
    FULL("full"),
    MEDIUM("medium"),
    LOW("low"),
    WEAK("weak");

    String suffix;

    FuelStates(String suffix) {
        this.suffix = suffix;
    }

    @Nonnull
    @Override
    public String getSerializedName() {
        return suffix;
    }
}

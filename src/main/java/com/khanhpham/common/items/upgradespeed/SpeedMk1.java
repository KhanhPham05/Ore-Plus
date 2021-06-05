package com.khanhpham.common.items.upgradespeed;

import com.khanhpham.common.items.AbstractSpeedUpgrade;
import net.minecraft.item.ItemStack;

public class SpeedMk1 extends AbstractSpeedUpgrade {
    @Override
    public int setEffect() {
        return 2;
    }

    @Override
    public byte tier() {
        return 1;
    }

    @Override
    public int speedEffect() {
        return this.setEffect();
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(this);
    }
}

package com.khanhpham.common.items;

import com.khanhpham.OrePlusLT;
import com.khanhpham.api.ISpeedUpgrade;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

public abstract class AbstractSpeedUpgrade extends Item implements ISpeedUpgrade {
    public AbstractSpeedUpgrade() {
        super(new Item.Properties()
                .setNoRepair()
                .stacksTo(1)
                .tab(OrePlusLT.RAW_ORES)
                .rarity(Rarity.UNCOMMON)
        );
    }

    public abstract int setEffect();

    @Override
    public byte tier() {
        return 1;
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(this);
    }

    @Override
    public int speedEffect() {
        if (setEffect() <= 0) throw new IllegalStateException("speedEffect can not be 0 or smaller");
        return setEffect();
    }
}

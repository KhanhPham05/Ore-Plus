package com.khanhpham.common.items;

import com.khanhpham.OrePlusLT;
import com.khanhpham.api.IEnricherElement;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class EnricherElement extends Item implements IEnricherElement {
    public EnricherElement() {
        super(new Item.Properties()
                .tab(OrePlusLT.RAW_ORES)
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .setNoRepair()
        );
    }

    private static final int fuel1 = 12;

    public static int getFuel1() {
        return fuel1;
    }
}

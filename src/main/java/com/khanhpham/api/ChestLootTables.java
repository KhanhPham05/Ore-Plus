package com.khanhpham.api;

import com.google.common.collect.Sets;
import com.khanhpham.RawOres;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class ChestLootTables {
    private static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();

    public static final ResourceLocation ENRICHER_CHEST = register("enricher_chest");

    static ResourceLocation register(String name) {
        return new ResourceLocation(RawOres.MODID, name);
    }
}

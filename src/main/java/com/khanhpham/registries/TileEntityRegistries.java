package com.khanhpham.registries;

import com.khanhpham.RawOres;
import com.khanhpham.registries.machine.oreenricher.EnricherTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistries {
    public static final DeferredRegister<TileEntityType<?>> TILE_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RawOres.MODID);

    public static final RegistryObject<TileEntityType<EnricherTile>> ENRICHER_TILE = TILE_TYPES.register("enricher", () -> TileEntityType.Builder.of(EnricherTile::new, BlockRegistries.ORE_ENRICHER.get()).build(null));
}

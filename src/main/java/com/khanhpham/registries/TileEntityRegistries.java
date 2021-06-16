package com.khanhpham.registries;

import com.khanhpham.OrePlusLT;
import com.khanhpham.common.machine.oreenricher.EnricherTile;
import com.khanhpham.common.machine.oreprocessor.ProcessorTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistries {
    public static final DeferredRegister<TileEntityType<?>> TILE_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, OrePlusLT.MODID);

    public static final RegistryObject<TileEntityType<EnricherTile>> ENRICHER_TILE = TILE_TYPES.register("enricher", () -> TileEntityType.Builder.of(EnricherTile::new, BlockRegistries.ORE_ENRICHER.get()).build(null));
    public static final RegistryObject<TileEntityType<ProcessorTile>> PROCESSOR_TILE = TILE_TYPES.register("processor", () -> TileEntityType.Builder.of(ProcessorTile::new, BlockRegistries.ORE_PROCESSOR.get()).build(null));
}
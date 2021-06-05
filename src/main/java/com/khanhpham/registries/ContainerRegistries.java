package com.khanhpham.registries;

import com.khanhpham.RawOres;
import com.khanhpham.common.machine.oreenricher.EnricherContainer;
import com.khanhpham.common.machine.oreprocessor.ProcessorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistries {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, RawOres.MODID);

    public static final RegistryObject<ContainerType<EnricherContainer>> ENRICHER = CONTAINERS.register("enricher", () -> IForgeContainerType.create(EnricherContainer::new));
    public static final RegistryObject<ContainerType<ProcessorContainer>> PROCESSOR = CONTAINERS.register("processor", () -> IForgeContainerType.create(ProcessorContainer::new));
}

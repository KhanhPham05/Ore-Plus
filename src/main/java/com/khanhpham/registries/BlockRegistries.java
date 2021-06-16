package com.khanhpham.registries;

import com.khanhpham.OrePlusLT;
import com.khanhpham.common.block.GeneralOre;
import com.khanhpham.common.machine.oreenricher.OreEnricher;
import com.khanhpham.common.machine.oreprocessor.OreProcessor;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlockRegistries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OrePlusLT.MODID);

    /**
     * @see Blocks#IRON_ORE
     */
    public static final RegistryObject<GeneralOre> RICH_IRON_ORE = create("rich_iron_ore", () -> new GeneralOre(AbstractBlock.Properties.of(Material.STONE).strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2).requiresCorrectToolForDrops()));

    public static final RegistryObject<OreEnricher> ORE_ENRICHER;
    public static final RegistryObject<OreProcessor> ORE_PROCESSOR;
    public static final RegistryObject<GeneralOre> ELEMENT_ORE = create("enriching_element_ore", () -> new GeneralOre(AbstractBlock.Properties.of(Material.STONE).strength(3.2F).harvestTool(ToolType.PICKAXE).harvestLevel(2).requiresCorrectToolForDrops()));


    static {
        ORE_ENRICHER = registerSpecial("ore_enricher", OreEnricher::new);
        ORE_PROCESSOR = registerSpecial("ore_processor", OreProcessor::new);
    }

    private static <T extends GeneralOre> RegistryObject<T> create(String name, Supplier<T> oreSupplier) {
        RegistryObject<T> BLOCK = BLOCKS.register(name, oreSupplier);
        ItemRegistries.ITEMS.register(name, () -> new BlockItem(BLOCK.get(), new Item.Properties().tab(OrePlusLT.RAW_ORES)));
        return BLOCK;
    }

    private static <T extends Block> RegistryObject<T> registerSpecial(String name, Supplier<T> blockSupplier) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        ItemRegistries.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(OrePlusLT.RAW_ORES).stacksTo(16).setNoRepair()));
        return block;
    }
}

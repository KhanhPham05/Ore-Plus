package com.khanhpham.registries;

import com.khanhpham.RawOres;
import com.khanhpham.registries.machine.oreenricher.OreEnricher;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RawOres.MODID);

    public static final RegistryObject<Block> RAW_IRON_BLOCK = create("raw_iron_block",1);
    public static final RegistryObject<Block> RICH_IRON_ORE = create("rich_iron_ore",1);
    public static final RegistryObject<Block> RAW_GOLD_BLOCK = create("raw_gold_block", 2);
    public static final RegistryObject<Block> RICH_GOLD_ORE = create("rich_gold_ore", 2);
    public static final RegistryObject<Block> RICH_COAL_ORE = create("rich_coal_ore", 1);

    public static final RegistryObject<OreEnricher> ORE_ENRICHER;

    private static RegistryObject<Block> create(String name, int level) {
        RegistryObject<Block> BLOCK = BLOCKS.register(name, () -> new Block(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.STONE).strength(3).harvestTool(ToolType.PICKAXE).harvestLevel(level).requiresCorrectToolForDrops()));
        ItemRegistries.ITEMS.register(name, () -> new BlockItem(BLOCK.get(), new Item.Properties().tab(RawOres.RAW_ORES)));
        return BLOCK;
    }

    static {
        ORE_ENRICHER = BLOCKS.register("ore_enricher", OreEnricher::new);
        ItemRegistries.ITEMS.register("ore_enricher",() -> new BlockItem(ORE_ENRICHER.get(), new Item.Properties().tab(RawOres.RAW_ORES).stacksTo(1).setNoRepair()));
    }
}

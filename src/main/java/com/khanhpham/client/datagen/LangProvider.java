package com.khanhpham.client.datagen;

import com.khanhpham.RawOres;
import com.khanhpham.registries.BlockRegistries;
import com.khanhpham.registries.ItemRegistries;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;
public class LangProvider extends LanguageProvider {
    public LangProvider(DataGenerator gen) {
        super(gen, RawOres.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        block(BlockRegistries.RAW_GOLD_BLOCK, "Raw Gold Block");
        block(BlockRegistries.ORE_ENRICHER, "Ore Enricher");
        block(BlockRegistries.RAW_IRON_BLOCK, "Raw Iron Block");
        block(BlockRegistries.RICH_COAL_ORE, "Rich Coal Ore");
        block(BlockRegistries.RICH_GOLD_ORE, "Rich Gold Ore");
        block(BlockRegistries.RICH_IRON_ORE, "Rich Iron Ore");
        add("container.rawores.enricher", "Ore Enricher");
        add("jei.raw_ores.ore_enriching", "Ore Enriching");
        item(ItemRegistries.ENRICHING_ELEMENT, "Enriching Element");
        item(ItemRegistries.RICH_COAL, "Rich Coal");
        item(ItemRegistries.RAW_DIAMOND, "Raw Diamond");
        item(ItemRegistries.IRON_RAW_ORE, "Raw Iron Ore");
        add("gui.rawores.enricher.fuel_left", "Fuel: %s");
    }

    private <T extends Item> void item(RegistryObject<T> entry, String name) {
        add(entry.get(), name);
    }

    private <T extends Block> void block(RegistryObject<T> entry, String name) {
        add(entry.get(), name);
    }
}

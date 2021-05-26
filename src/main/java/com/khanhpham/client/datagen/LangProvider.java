package com.khanhpham.client.datagen;

import com.khanhpham.RawOres;
import com.khanhpham.registries.BlockRegistries;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;
public class LangProvider extends LanguageProvider {
    public LangProvider(DataGenerator gen) {
        super(gen, RawOres.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(BlockRegistries.RAW_GOLD_BLOCK, "Raw Gold Block");
        add(BlockRegistries.ORE_ENRICHER, "Ore Enricher");
        add(BlockRegistries.RAW_IRON_BLOCK, "Raw Iron Block");
        add(BlockRegistries.RICH_COAL_ORE, "Rich Coal Ore");
        add(BlockRegistries.RICH_GOLD_ORE, "Rich Gold Ore");
        add(BlockRegistries.RICH_IRON_ORE, "Rich iron Ore");
        add("container.rawores.enricher", "Ore Enricher");
        add("jei.raw_ores.ore_enriching", "Ore Enriching");

    }

    @SuppressWarnings("unchecked")
    void add(RegistryObject<? extends Block> entry, String name) {
        add(entry.get(), name);
    }
}

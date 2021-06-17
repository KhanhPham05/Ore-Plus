package com.khanhpham.client.datagen;

import com.khanhpham.OrePlusLT;
import com.khanhpham.common.LangKeys;
import com.khanhpham.registries.BlockRegistries;
import com.khanhpham.registries.EnchantRegistries;
import com.khanhpham.registries.ItemRegistries;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;

public class LangProvider extends LanguageProvider {
    public LangProvider(DataGenerator gen) {
        super(gen, OrePlusLT.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        block(BlockRegistries.ORE_ENRICHER, "Ore Enricher");
        block(BlockRegistries.RICH_IRON_ORE, "Enriched Iron Ore");
        add(LangKeys.ENRICHER_JEI, "Ore Enriching");
        item(ItemRegistries.ENRICHING_ELEMENT, "Enriching Element");
        item(ItemRegistries.IRON_RAW_ORE, "Raw Iron Ore");
        add(LangKeys.PROCESSOR_SCREEN, "Ore Processor");
        item(ItemRegistries.SPEED_MK_I, "Speed Upgrade MK 1");
        add(LangKeys.ENRICHER_SCREEN, "Ore Enricher");
        add(LangKeys.PROCESSOR_DESC, "A fuel-based machine for duplicate ore processing from rich ore");
        add(LangKeys.ENRICHER_DESC, "A fuel-based machine use Enriching Element to turn normal ore to enriched ore");
        add(LangKeys.PROCESSOR_DESC_LINE_2, "fuel can be any burnable items in furnace");
        add(LangKeys.PROCESSOR_JEI, "Ore Processing");
        block(BlockRegistries.ELEMENT_ORE, "Element Ore");
        block(BlockRegistries.ORE_PROCESSOR, "Ore Processor");
        add(EnchantRegistries.RICH_MINING.get(), "Rich Mining");
    }

    private <T extends Item> void item(RegistryObject<T> entry, String name) {
        add(entry.get(), name);
    }

    private <T extends Block> void block(RegistryObject<T> entry, String name) {
        add(entry.get(), name);
    }

    private void add(ITextComponent key, String lang) {
        super.add(key.getString(), lang);
    }
}

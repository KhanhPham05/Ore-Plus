package com.khanhpham.utils;

import com.khanhpham.RawOres;
import com.khanhpham.registries.recipe.OreEnriching;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @see OreEnriching
 */
public enum LangEntry {
    ORE_ENRICHING_CATEGORY("jei", "ore_enriching");

    private final ITextComponent lang;

    LangEntry(String local, String path) {
        lang = new TranslationTextComponent(local + RawOres.MODID + path);
    }

    public ITextComponent getTranslationKey() {
        return lang;
    }
}

package com.khanhpham.common;

import com.khanhpham.OrePlusLT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class LangKeys {
    public static final ITextComponent ENRICHER_SCREEN = text("container", "enricher");
    public static final ITextComponent PROCESSOR_SCREEN = text("container", "processor");
    public static final ITextComponent PROCESSOR_DESC = text("desc", "ore_processor");
    public static final ITextComponent ENRICHER_DESC = text("desc", "ore_enricher");
    public static final ITextComponent PROCESSOR_DESC_LINE_2 = text("desc", "processor_line_2");
    public static final ITextComponent ENRICHER_JEI = text("jei", "ore_enriching");
    public static final ITextComponent PROCESSOR_JEI = text("jei", "ore_processing");

    //Method extracted
    private static TranslationTextComponent text(String s, String s2) {
        return new TranslationTextComponent(s  + "." + OrePlusLT.MODID + "." + s2);
    }
}

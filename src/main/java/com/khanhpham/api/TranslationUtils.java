package com.khanhpham.api;

import com.khanhpham.RawOres;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Borrowed from Silent Mechanism on gitHub
 * @author SilentChaos512
 */
public class TranslationUtils {
    private static final String FORMAT = "%,d";

    public static IFormattableTextComponent fuelRemains(int fuelLeft) {
        String s = String.format(FORMAT, fuelLeft);
        return translate("gui","enricher.fuel_left", s);
    }

    public static IFormattableTextComponent translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, RawOres.MODID, suffix);
        return new TranslationTextComponent(key, params);
    }
}

package com.khanhpham.client.datagen.recipe;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.khanhpham.RawOres;
import com.khanhpham.client.datagen.tags.ModTags;
import com.khanhpham.registries.BlockRegistries;
import com.khanhpham.registries.ItemRegistries;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @see net.minecraft.tags.ITag.INamedTag
 * @see ItemTags
 */
public class ModRecipes extends RecipeProvider {
    public ModRecipes(DataGenerator data) {
        super(data);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        blasting(consumer, ItemRegistries.IRON_RAW_ORE.get(), Items.IRON_INGOT, "has_iron_raw", "iron_ore_from_raw");
        ShapedRecipeBuilder.shaped(BlockRegistries.ORE_ENRICHER.get())
                .pattern("IRI")
                .pattern("RFR")
                .pattern("IRI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('F', Blocks.FURNACE)
                .unlockedBy("has_furnace", InventoryChangeTrigger.Instance.hasItems(Blocks.FURNACE))
                .save(consumer, rl("craft_enricher"));
        OreEnrichingRecipeBuilder.build(Ingredient.of(Blocks.IRON_ORE), BlockRegistries.RICH_IRON_ORE.get())
                .unlockedBy("has_iron_ore", has(Blocks.IRON_ORE))
                .save(consumer, rl("enrich_ore_iron"));
    }

    private void blasting(Consumer<IFinishedRecipe> consumer, Item input, Item result, String criteria, String saveName) {
        CookingRecipeBuilder.blasting(Ingredient.of(input), result, 0.7f, 100).unlockedBy(criteria, has(ModTags.IRON)).save(consumer, rl(saveName));
    }

    private ResourceLocation rl(String name) {
        return new ResourceLocation(RawOres.MODID, name);
    }
}

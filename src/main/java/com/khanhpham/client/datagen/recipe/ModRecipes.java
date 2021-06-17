package com.khanhpham.client.datagen.recipe;

import com.khanhpham.OrePlusLT;
import com.khanhpham.client.datagen.tags.ModTags;
import com.khanhpham.registries.BlockRegistries;
import com.khanhpham.registries.ItemRegistries;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

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
        craftingShaped(consumer);
        OreEnrichingRecipeBuilder.build(Ingredient.of(Blocks.IRON_ORE), BlockRegistries.RICH_IRON_ORE.get())
                .unlockedBy("has_iron_ore", has(Blocks.IRON_ORE))
                .save(consumer, rl("enrich_ore_iron"));
        OreProcessingBuilder.build(Ingredient.of(BlockRegistries.RICH_IRON_ORE.get()), Items.IRON_INGOT, 2)
                .unlockedBy("has_rich_ore", has(BlockRegistries.RICH_IRON_ORE.get()))
                .save(consumer, rl("rich_iron_ore_to_ingots"));
    }

    private Ingredient ingredient(IItemProvider item) {
        return Ingredient.of(item);
    }

    private void craftingShaped(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(BlockRegistries.ORE_ENRICHER.get()).pattern("IRI").pattern("RFR").pattern("IRI").define('I', Tags.Items.INGOTS_IRON).define('R', Tags.Items.DUSTS_REDSTONE).define('F', Blocks.FURNACE).unlockedBy("has_furnace", InventoryChangeTrigger.Instance.hasItems(Blocks.FURNACE)).save(consumer, rl("craft_enricher"));
        ShapedRecipeBuilder.shaped(ItemRegistries.SPEED_MK_I.get()).pattern(" R ").pattern("RSR").pattern(" R ").define('R', Tags.Items.DUSTS_REDSTONE).define('S', Items.SUGAR).unlockedBy("has_redstone_and_sugar", InventoryChangeTrigger.Instance.hasItems(Items.SUGAR, Items.REDSTONE)).save(consumer, rl("craft_speed_lvl_1"));
        ShapedRecipeBuilder.shaped(BlockRegistries.ORE_PROCESSOR.get()).pattern("IRI").pattern("ICI").pattern("IRI").define('I', Tags.Items.INGOTS_IRON).define('R', Tags.Items.DUSTS_REDSTONE).define('C', ItemTags.COALS).unlockedBy("criterion_3", InventoryChangeTrigger.Instance.hasItems(Items.IRON_INGOT)).save(consumer, rl("craft_processor"));
    }

    private void blasting(Consumer<IFinishedRecipe> consumer, Item input, Item result, String criteria, String saveName) {
        CookingRecipeBuilder.blasting(Ingredient.of(input), result, 0.7f, 100).unlockedBy(criteria, has(ModTags.IRON)).save(consumer, rl(saveName));
    }

    private ResourceLocation rl(String name) {
        return new ResourceLocation(OrePlusLT.MODID, name);
    }
}

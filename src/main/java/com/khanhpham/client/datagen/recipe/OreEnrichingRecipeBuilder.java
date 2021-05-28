package com.khanhpham.client.datagen.recipe;

import com.google.gson.JsonObject;
import com.khanhpham.RawOres;
import com.khanhpham.common.recipe.OreEnriching;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class OreEnrichingRecipeBuilder {
    private final Ingredient input;
    private final Item output;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    private OreEnrichingRecipeBuilder(Ingredient input, Item output) {
        this.input = input;
        this.output = output;
    }

    public static OreEnrichingRecipeBuilder build(Ingredient input, IItemProvider output) {
        return new OreEnrichingRecipeBuilder(input, output.asItem());
    }

    public OreEnrichingRecipeBuilder unlockedBy(String criterionId, ICriterionInstance criterion) {
        advancement.addCriterion(criterionId, criterion);
        return this;
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation rl) {
        ensureValid(rl);
        if (!advancement.getCriteria().isEmpty())
            advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(rl)).rewards(AdvancementRewards.Builder.recipe(rl)).requirements(IRequirementsStrategy.OR);
        assert output.getItemCategory() != null;
        consumer.accept(
                new Output(rl, input, output, advancement,
                        new ResourceLocation(rl.getNamespace(), "recipes/" + output.getItemCategory().getRecipeFolderName() + "/" + rl.getPath())
                ));
    }

    @SuppressWarnings("deprecation")
    public void save(Consumer<IFinishedRecipe> consumer) {
        save(consumer, Registry.ITEM.getKey(output));
    }

    private void ensureValid(ResourceLocation rl) {
        if (advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("Can not obtain recipe: " + rl);
        }
    }

    /**
     * @see CookingRecipeBuilder.Result
     */
    public static final class Output implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient input;
        private final Item output;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;


        public Output(ResourceLocation id, Ingredient input, Item output, Advancement.Builder builder, ResourceLocation advancementId) {
            this.id = id;
            this.input = input;
            this.output = output;
            this.advancementBuilder = builder;
            this.advancementId = advancementId;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("inputs", input.toJson());
            json.addProperty("output", Registry.ITEM.getKey(output).toString());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getType() {
            return ForgeRegistries.RECIPE_SERIALIZERS.getValue(new ResourceLocation(RawOres.MODID, "ore_enriching"));
            //return serializer;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return advancementBuilder.getCriteria().isEmpty() ? advancementBuilder.serializeToJson() : null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}

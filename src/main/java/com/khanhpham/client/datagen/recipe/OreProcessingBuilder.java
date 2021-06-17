package com.khanhpham.client.datagen.recipe;

import com.google.gson.JsonObject;
import com.khanhpham.OrePlusLT;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

public class OreProcessingBuilder {
    private final Ingredient input;
    private final Item output;
    private final Advancement.Builder advancementBuilder;
    private final int count;

    public OreProcessingBuilder(Ingredient input, Item output, int count) {
        this.input = input;
        this.output = output;
        advancementBuilder = Advancement.Builder.advancement();
        this.count = count;
    }

    public static OreProcessingBuilder build(Ingredient input, IItemProvider output, int count) {
        return new OreProcessingBuilder(input, output.asItem(), count);
    }

    public OreProcessingBuilder unlockedBy(String criterionId, ICriterionInstance criterionInstance) {
        advancementBuilder.addCriterion(criterionId, criterionInstance);
        return this;
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation rl) {
        ensureValid(rl);
        if (!advancementBuilder.getCriteria().isEmpty())
            advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(rl)).rewards(AdvancementRewards.Builder.recipe(rl)).requirements(IRequirementsStrategy.OR);
        assert output.getItemCategory() != null;
        consumer.accept(
                new Result(rl, input, output, count, advancementBuilder,
                        new ResourceLocation(rl.getNamespace(), "recipes/" + output.getItemCategory().getRecipeFolderName() + "/" + rl.getPath())
                ));
    }

    private void ensureValid(ResourceLocation rl) {
        if (advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("Can not obtain recipe: " + rl);
        }
    }

    public static final class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient input;
        private final Item output;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final int count;

        public Result(ResourceLocation id, Ingredient input, Item output, int count, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
            this.id = id;
            this.input = input;
            this.output = output;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.count = count;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("inputs", input.toJson());
            json.addProperty("output", Registry.ITEM.getKey(output).toString());
            json.addProperty("count", this.count);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getType() {
            return Objects.requireNonNull(ForgeRegistries.RECIPE_SERIALIZERS.getValue(new ResourceLocation(OrePlusLT.MODID, "ore_processing")));
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

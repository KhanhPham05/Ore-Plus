package com.khanhpham.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.khanhpham.OrePlusLT;
import com.khanhpham.registries.BlockRegistries;
import com.khanhpham.registries.RecipeTypeRegistries;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class OreProcessing implements IRecipe<IInventory> {
    private final Ingredient input;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int count;

    public OreProcessing(Ingredient input, ItemStack output, ResourceLocation id, int count) {
        this.input = input;
        this.output = output;
        this.id = id;
        this.count = count;
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        return input.test(p_77569_1_.getItem(0));
    }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return new Serializers();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypeRegistries.ORE_PROCESSING;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input);
        return ingredients;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistries.ORE_PROCESSOR.get());
    }

    public int getResultCount() {
        return count;
    }

    public static final class Type implements IRecipeType<OreProcessing> {
        @Override
        public String toString() {
            return OrePlusLT.MODID + ":ore_processing";
        }
    }

    public static final class Serializers extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<OreProcessing> {

        public Serializers() {
            setRegistryName(OrePlusLT.MODID, "ore_processing");
        }

        /**
         * @see CookingRecipeSerializer
         */
        @SuppressWarnings("deprecation")
        @Override
        public OreProcessing fromJson(ResourceLocation p_199425_1_, JsonObject json) {
            final JsonElement inputElement = JSONUtils.isArrayNode(json, "inputs")
                    ? JSONUtils.getAsJsonArray(json, "inputs")
                    : JSONUtils.getAsJsonObject(json, "inputs");

            final Ingredient input = Ingredient.fromJson(inputElement);

            final ItemStack output;
            if (json.get("output").isJsonObject()) {
                output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));
            } else {
                output = new ItemStack(Registry.ITEM.getOptional(new ResourceLocation(JSONUtils.getAsString(json, "output"))).orElseThrow(() -> new IllegalStateException("Oops")));
            }

            final int count = JSONUtils.getAsInt(json, "count", 2);

            return new OreProcessing(input, output, p_199425_1_, count);
        }

        @Override
        public OreProcessing fromNetwork(ResourceLocation p_199426_1_, PacketBuffer buffer) {
            final Ingredient input = Ingredient.fromNetwork(buffer);
            final ItemStack output = buffer.readItem();
            final int count = buffer.readVarInt();

            return new OreProcessing(input, output, p_199426_1_, count);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, OreProcessing recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItemStack(recipe.output, false);
            buffer.writeVarInt(recipe.count);
        }
    }
}

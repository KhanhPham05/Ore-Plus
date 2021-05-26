package com.khanhpham.registries.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.khanhpham.RawOres;
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

/**
 * @see net.minecraft.inventory.IRecipeHolder
 * @see net.minecraft.inventory.container.FurnaceContainer
 * @see CookingRecipeSerializer
 */
public class OreEnriching implements IRecipe<IInventory> {
    public final Ingredient input;
    public final ItemStack output;
    private final ResourceLocation id;
    public final int time;

    public OreEnriching(Ingredient input, ItemStack output, ResourceLocation id, int time) {
        this.input = input;
        this.output = output;
        this.id = id;
        this.time = time;
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        return input.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(IInventory inv) {
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
        return new OreEnriching.Serializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypeRegistries.ORE_ENRICHING;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistries.ORE_ENRICHER.get().asItem());
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(input);
        return ingredients;
    }

    public int getWorkingTime() {
        return this.time;
    }

    public static final class Type implements IRecipeType<OreEnriching> {
        @Override
        public String toString() {
            return RawOres.MODID + ":ore_enriching";
        }
    }

    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<OreEnriching> {
        public Serializer() {
            setRegistryName(RawOres.MODID, "ore_enriching");
        }

        /**
         * @see com.google.gson.JsonArray
         */
        @Override
        public OreEnriching fromJson(ResourceLocation recipeId, JsonObject json) {
            final JsonElement inputEl = JSONUtils.isArrayNode(json, "inputs")
                    ? JSONUtils.getAsJsonArray(json, "inputs")
                    : JSONUtils.getAsJsonObject(json, "inputs");
            final Ingredient input = Ingredient.fromJson(inputEl);

            final ItemStack output;
            if (json.get("output").isJsonObject())
                output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));
            else  {
                output = new ItemStack(Registry.ITEM.getOptional(new ResourceLocation(JSONUtils.getAsString(json, "output"))).orElseThrow(() -> {
                    return new IllegalStateException("Oops");
                }));
            }
            return new OreEnriching(input, output, recipeId, 100);
        }

        @Override
        public OreEnriching fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            final Ingredient input = Ingredient.fromNetwork(buffer);
            final ItemStack output = buffer.readItem();


            return new OreEnriching(input, output, recipeId, 100);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, OreEnriching recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItemStack(recipe.output, false);
        }
    }
}

package com.khanhpham.registries;

import com.khanhpham.common.recipe.OreEnriching;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.Map;

public class RecipeTypeRegistries {
    public static final IRecipeType<OreEnriching> ORE_ENRICHING = new OreEnriching.Type();
    //public static final IRecipeType<TestRecipe> TEST_RECIPE = new TestRecipe.Type();

    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        registerRecipe(event, ORE_ENRICHING, new OreEnriching.Serializer());
       // registerRecipe(event, TEST_RECIPE, new TestRecipe.Serializer());
    }

    private static void registerRecipe(RegistryEvent.Register<IRecipeSerializer<?>> event, IRecipeType<?> type,
                                       IRecipeSerializer<?> serializer) {
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(type.toString()), type);
        event.getRegistry().register(serializer);
    }

    public static Map<ResourceLocation, IRecipe<?>> getRecipes(IRecipeType<?> type, RecipeManager manager) {
        final Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = ObfuscationReflectionHelper
                .getPrivateValue(RecipeManager.class, manager, "recipes");
        return recipes.get(type);
    }
}

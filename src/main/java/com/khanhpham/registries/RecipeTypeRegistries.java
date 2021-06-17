package com.khanhpham.registries;

import com.khanhpham.common.recipe.OreEnriching;
import com.khanhpham.common.recipe.OreProcessing;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;

public class RecipeTypeRegistries {
    public static final IRecipeType<OreEnriching> ORE_ENRICHING = new OreEnriching.Type();
    public static final IRecipeType<OreProcessing> ORE_PROCESSING = new OreProcessing.Type();

    public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        registerRecipe(event, ORE_ENRICHING, new OreEnriching.Serializer());
        registerRecipe(event, ORE_PROCESSING, new OreProcessing.Serializers());
    }

    private static void registerRecipe(RegistryEvent.Register<IRecipeSerializer<?>> event, IRecipeType<?> type,
                                       IRecipeSerializer<?> serializer) {
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(type.toString()), type);
        event.getRegistry().register(serializer);
    }
}

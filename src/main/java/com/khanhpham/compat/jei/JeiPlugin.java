package com.khanhpham.compat.jei;

import com.khanhpham.RawOres;
import com.khanhpham.compat.jei.recipe.OreEnrichingCategory;
import com.khanhpham.registries.RecipeTypeRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.stream.Collectors;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(RawOres.MODID, "jei_compat");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @SuppressWarnings("resource")
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(recipeManager.getRecipes().parallelStream().filter(recipe -> recipe.getType() == RecipeTypeRegistries.ORE_ENRICHING).collect(Collectors.toList()), OreEnrichingCategory.ID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new OreEnrichingCategory(helper));
    }
}

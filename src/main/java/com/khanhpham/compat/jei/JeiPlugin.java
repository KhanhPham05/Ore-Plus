package com.khanhpham.compat.jei;

import com.khanhpham.RawOres;
import com.khanhpham.common.machine.oreenricher.EnricherContainer;
import com.khanhpham.common.machine.oreenricher.EnricherScreen;
import com.khanhpham.compat.jei.recipe.OreEnrichingCategory;
import com.khanhpham.registries.BlockRegistries;
import com.khanhpham.registries.RecipeTypeRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
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

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        addCatalyst(registration, new ItemStack(BlockRegistries.ORE_ENRICHER.get().asItem()), OreEnrichingCategory.ID);
    }

    private void addCatalyst(IRecipeCatalystRegistration registry, ItemStack catalystIcon, ResourceLocation categoryName) {
        registry.addRecipeCatalyst(catalystIcon, categoryName);
    }

    /**
     * Register the + icon for recipe GUI
     */
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(EnricherContainer.class, OreEnrichingCategory.ID, 0, 2, 3, 36);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(EnricherScreen.class, 93, 35, 23, 17, OreEnrichingCategory.ID);
    }
}

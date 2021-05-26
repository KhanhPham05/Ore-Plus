package com.khanhpham.compat.jei.recipe;

import com.khanhpham.RawOres;
import com.khanhpham.registries.recipe.OreEnriching;
import com.khanhpham.registries.BlockRegistries;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OreEnrichingCategory implements IRecipeCategory<OreEnriching> {
    public static final ResourceLocation ID = new ResourceLocation(RawOres.MODID, "ore_enriching_category");
    private static final ResourceLocation TEXTURE_ID = new ResourceLocation(RawOres.MODID, "textures/gui/enricher_jei.png");

    private final IDrawable back;
    private final IDrawable icon;

    public OreEnrichingCategory(IGuiHelper helper) {
        //back = helper.createBlankDrawable(180, 100);
        back = helper.createDrawable(TEXTURE_ID, 0, 0, 176, 83);
        icon = helper.createDrawableIngredient(new ItemStack(BlockRegistries.ORE_ENRICHER.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends OreEnriching> getRecipeClass() {
        return OreEnriching.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("jei.raw_ores.ore_enriching").getString();
    }

    @Override
    public IDrawable getBackground() {
        return back;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(OreEnriching recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void draw(OreEnriching recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, matrixStack, mouseX, mouseY);
    }


    @Override
    public void setRecipe(IRecipeLayout recipeLayout, OreEnriching recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();

        group.init(0, true, 55, 35);
        group.init(2, false, 116, 35);

        group.set(ingredients);
    }
}

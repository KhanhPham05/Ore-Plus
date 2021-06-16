package com.khanhpham.compat.jei.recipe;

import com.khanhpham.OrePlusLT;
import com.khanhpham.common.LangKeys;
import com.khanhpham.common.recipe.OreEnriching;
import com.khanhpham.registries.BlockRegistries;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class OreEnrichingCategory implements IRecipeCategory<OreEnriching> {
    public static final ResourceLocation ID = new ResourceLocation(OrePlusLT.MODID, "ore_enriching_category");
    private static final ResourceLocation TEXTURE_ID = new ResourceLocation(OrePlusLT.MODID, "textures/gui/enricher_jei.png");

    private final IDrawable background;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;

    public OreEnrichingCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE_ID, 0, 0, 143, 48);
        icon = helper.createDrawableIngredient(new ItemStack(BlockRegistries.ORE_ENRICHER.get()));

        IDrawableStatic arrowNonAnimated = helper.createDrawable(TEXTURE_ID, 144, 3, 24, 17);
        arrow = helper.createAnimatedDrawable(arrowNonAnimated, 80, IDrawableAnimated.StartDirection.LEFT, false);
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
        return LangKeys.ENRICHER_JEI.getString();
    }

    @Override
    public IDrawable getBackground() {
        return background;
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
        arrow.draw(matrixStack, 78, 16);
    }


    @Override
    public void setRecipe(IRecipeLayout recipeLayout, OreEnriching recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();

        group.init(0, true, 52, 16);
        group.init(1, true, 9, 16);
        group.init(2, false, 113, 16);

        group.set(ingredients);
    }
}

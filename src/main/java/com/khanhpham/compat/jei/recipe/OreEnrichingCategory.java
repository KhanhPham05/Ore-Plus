package com.khanhpham.compat.jei.recipe;

import com.khanhpham.RawOres;
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
import net.minecraft.util.text.TranslationTextComponent;

public class OreEnrichingCategory implements IRecipeCategory<OreEnriching> {
    public static final ResourceLocation ID = new ResourceLocation(RawOres.MODID, "ore_enriching_category");
    private static final ResourceLocation TEXTURE_ID = new ResourceLocation(RawOres.MODID, "textures/gui/enricher_jei_new.png");

    private final IDrawable background;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;

    public OreEnrichingCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE_ID, 0, 0, 176, 83);
        icon = helper.createDrawableIngredient(new ItemStack(BlockRegistries.ORE_ENRICHER.get()));
        IDrawableStatic arrowNonAnimated = helper.createDrawable(TEXTURE_ID, 183, 1, 22, 16);
        arrow = helper.createAnimatedDrawable(arrowNonAnimated, 100, IDrawableAnimated.StartDirection.LEFT, false);
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

    /**
     *
     */

    @Override
    public void draw(OreEnriching recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 93, 36);
    }


    @Override
    public void setRecipe(IRecipeLayout recipeLayout, OreEnriching recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();

        group.init(0, true, 66, 34);
        group.init(1, true, 23, 34);
        group.init(2, false, 126, 34);

        group.set(ingredients);
    }
}

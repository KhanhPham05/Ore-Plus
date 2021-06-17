package com.khanhpham.compat.jei.recipe;

import com.khanhpham.OrePlusLT;
import com.khanhpham.common.LangKeys;
import com.khanhpham.common.recipe.OreProcessing;
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

public class OreProcessingCategory implements IRecipeCategory<OreProcessing> {

    public static final ResourceLocation ID = new ResourceLocation(OrePlusLT.MODID, "ore_processing_category");
    public static final ResourceLocation TEXTURE_ID = new ResourceLocation(OrePlusLT.MODID, "textures/gui/processor_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated fuelPipe;

    public OreProcessingCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE_ID, 0, 0, 140, 41);
        icon = helper.createDrawableIngredient(new ItemStack(BlockRegistries.ORE_PROCESSOR.get()));

        IDrawableStatic staticArrow = helper.createDrawable(TEXTURE_ID, 142, 15, 24, 17);
        arrow = helper.createAnimatedDrawable(staticArrow, 80, IDrawableAnimated.StartDirection.LEFT, false);

        IDrawableStatic staticPipe = helper.createDrawable(TEXTURE_ID, 142, 4, 21, 10);
        fuelPipe = helper.createAnimatedDrawable(staticPipe, 200, IDrawableAnimated.StartDirection.RIGHT, true);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends OreProcessing> getRecipeClass() {
        return OreProcessing.class;
    }

    @Override
    public String getTitle() {
        return LangKeys.PROCESSOR_JEI.getString();
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
    public void setIngredients(OreProcessing recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void draw(OreProcessing recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 76, 12);
        fuelPipe.draw(matrixStack, 27, 16);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, OreProcessing recipe, IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();

        group.init(0, true, 50, 12);
        group.init(2, false, 111, 12);

        group.set(ingredients);
    }
}

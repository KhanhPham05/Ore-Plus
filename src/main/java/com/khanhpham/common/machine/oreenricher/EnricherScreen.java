package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.RawOres;
import com.khanhpham.api.TranslationUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @see net.minecraft.client.gui.screen.inventory.FurnaceScreen
 */
public class EnricherScreen extends ContainerScreen<EnricherContainer> {
    public EnricherScreen(EnricherContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    public static final ResourceLocation GUI = new ResourceLocation(RawOres.MODID, "textures/gui/enricher_new.png");

    /**
     * @see net.minecraft.inventory.container.AbstractFurnaceContainer
     * @see net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen
     * @see net.minecraft.client.gui.screen.inventory.CreativeScreen
     */

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(MatrixStack matrix, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1, 1, 1, 1);
        assert minecraft != null;
        minecraft.getTextureManager().bind(GUI);
        int i = leftPos;
        int j = topPos;
        blit(matrix, i, j, 0, 0, 176, 177);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int p_230451_2_, int p_230451_3_) {
        font.draw(matrix, this.inventory.getDisplayName(), 8,
                79, 4210752);
        font.draw(matrix, EnricherTile.getTitle(), 10, 10, 4210732);
    }

}

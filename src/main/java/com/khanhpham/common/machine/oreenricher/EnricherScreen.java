package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.RawOres;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @see net.minecraft.client.gui.screen.inventory.FurnaceScreen
 */
public class EnricherScreen extends ContainerScreen<EnricherContainer> {
    private final EnricherContainer container;

    public EnricherScreen(EnricherContainer menu, PlayerInventory playerInventory, ITextComponent translate) {
        super(menu, playerInventory, translate);
        this.container = menu;
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

        int process = container.getProcess();
        blit(matrix, i + 92, j + 34, 176, 13, process + 1, 17);

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

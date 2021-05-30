package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.RawOres;
import com.khanhpham.common.LangKeys;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @see net.minecraft.client.gui.screen.inventory.FurnaceScreen
 */
@OnlyIn(Dist.CLIENT)
public class EnricherScreen extends ContainerScreen<EnricherContainer> {

    public EnricherScreen(EnricherContainer menu, PlayerInventory playerInventory, ITextComponent translate) {
        super(menu, playerInventory, translate);
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
        this.blit(matrix, i, j, 0, 0, 176, 177);
        this.blit(matrix, i + 93, j + 35, 176, 13, this.menu.getProcess() + 1, 17);

        if (this.menu.isElementCharged()) {
            this.blit(matrix, i + 45, j + 34, 205, 31, 16, 16);
        }
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
        font.draw(matrix, LangKeys.ENRICHER_SCREEN, 10, 10, 4210732);
    }

}

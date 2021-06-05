package com.khanhpham.common.machine.oreprocessor;

import com.khanhpham.RawOres;
import com.khanhpham.common.LangKeys;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ProcessorScreen extends ContainerScreen<ProcessorContainer> {
    public static final ResourceLocation SCREEN_ID = new ResourceLocation(RawOres.MODID, "textures/gui/ore_processor.png");

    public ProcessorScreen(ProcessorContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(MatrixStack matrix, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(SCREEN_ID);
        int left = leftPos;
        int top = topPos;
        int xOffset = 30;

        /* Main Screen */
        blit(matrix, left - xOffset, top, 0, 0, 206, 177);

        /* Processing Arrow */
        blit(matrix, left + 122 - xOffset, top + 34, 208, 23, menu.getProcess() + 1, 17);

        /* upgrade pipe */
        if (menu.isSpeedUpgraded())
        blit(matrix, left + 13 - xOffset, top + 31, 212, 1, 17, 10);

        /* fuel Pipe */
        blit(matrix, left + 73 - xOffset, top + 38, 208, 12, menu.getFuel() + 1, 10);
    }

    @Override
    public void render(MatrixStack matrix, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        renderBackground(matrix);
        super.render(matrix, p_230430_2_, p_230430_3_, p_230430_4_);
        renderTooltip(matrix, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int p_230451_2_, int p_230451_3_) {
        this.font.draw(matrix, inventory.getDisplayName(), 8, 79, 4210752);
        font.draw(matrix, LangKeys.PROCESSOR_SCREEN, 10, 10, 4210752);
    }
}

package com.khanhpham.common.machine.oreprocessor;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class OreProcessor extends Block {
    public OreProcessor() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
        );
    }


}

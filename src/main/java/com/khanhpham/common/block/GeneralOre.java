package com.khanhpham.common.block;

import com.khanhpham.registries.BlockRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nonnull;
import java.util.Random;

public class GeneralOre extends OreBlock {
    public GeneralOre(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    protected int xpOnDrop(@Nonnull Random random) {
        if (this == BlockRegistries.ELEMENT_ORE.get()) return MathHelper.nextInt(random, 3, 6);
        else return 0;
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? xpOnDrop(RANDOM) : 0;
    }
}

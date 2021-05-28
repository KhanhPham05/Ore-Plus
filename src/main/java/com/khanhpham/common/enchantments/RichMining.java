package com.khanhpham.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * @see net.minecraft.enchantment.Enchantments
 */
public class RichMining extends Enchantment {
    public RichMining() {
        super(Rarity.VERY_RARE, EnchantmentType.DIGGER, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int p_77321_1_) {
        return 15;
    }

    @Override
    public int getMaxCost(int value) {
        return super.getMinCost(value) + 50;
    }

    @Override
    protected boolean checkCompatibility(Enchantment p_77326_1_) {
        boolean valid1 =  super.checkCompatibility(p_77326_1_) && p_77326_1_ != Enchantments.SILK_TOUCH;
        boolean valid2 =  super.checkCompatibility(p_77326_1_) && p_77326_1_ != Enchantments.BLOCK_FORTUNE;
        if (valid1 && !valid2) return valid1;
            else return valid2;
    }
}

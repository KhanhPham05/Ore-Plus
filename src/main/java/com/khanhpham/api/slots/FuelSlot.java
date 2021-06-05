package com.khanhpham.api.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;

public class FuelSlot extends Slot {
    public FuelSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean mayPlace(ItemStack p_75214_1_) {
        return ForgeHooks.getBurnTime(p_75214_1_) > 0 || p_75214_1_.getItem() == Items.BUCKET;
    }
}

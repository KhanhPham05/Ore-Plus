package com.khanhpham.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class UpgradeSlot extends Slot {
    public UpgradeSlot(TileEntity p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super((IInventory) p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof IUpgradeable;
    }
}

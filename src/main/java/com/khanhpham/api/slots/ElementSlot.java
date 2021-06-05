package com.khanhpham.api.slots;

import com.khanhpham.registries.ItemRegistries;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * @see net.minecraft.inventory.container.FurnaceFuelSlot
 */
public class ElementSlot extends Slot {

    public ElementSlot(IInventory iinventory, int index, int x, int y) {
        super(iinventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getStack().sameItem(new ItemStack(ItemRegistries.ENRICHING_ELEMENT.get()));
    }
}

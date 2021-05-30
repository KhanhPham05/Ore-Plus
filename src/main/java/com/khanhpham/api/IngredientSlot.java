package com.khanhpham.api;

import com.khanhpham.common.machine.oreenricher.EnricherTile;
import com.khanhpham.registries.ItemRegistries;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class IngredientSlot extends Slot {
    protected IngredientSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    public IngredientSlot (EnricherTile te, int slotIndex, int x, int y) {
        this((IInventory) te, slotIndex, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.getStack().sameItem(new ItemStack(ItemRegistries.ENRICHING_ELEMENT.get()));
    }
}

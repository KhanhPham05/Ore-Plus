package com.khanhpham.api.slots;

import com.khanhpham.registries.ItemRegistries;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;

public class IngredientSlot extends Slot {
    public IngredientSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.getStack().sameItem(new ItemStack(ItemRegistries.ENRICHING_ELEMENT.get()))
                || !stack.getStack().sameItem(new ItemStack(Items.BUCKET))
                || ForgeHooks.getBurnTime(stack) <= 0;
    }
}

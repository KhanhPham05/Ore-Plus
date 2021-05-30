package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.api.ElementSlot;
import com.khanhpham.api.IngredientSlot;
import com.khanhpham.api.OutputSlot;
import com.khanhpham.registries.ContainerRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnricherContainer extends Container {
    private final IIntArray data;
    /**
     * @see net.minecraft.inventory.container.AbstractFurnaceContainer
     * @see net.minecraft.block.Blocks#FURNACE
     */
    public EnricherContainer(final int p_i50105_2_, final PlayerInventory inv, final IInventory blockInv, IIntArray data) {
        super(ContainerRegistries.ENRICHER.get(), p_i50105_2_);
        this.data = data;

        //adding slots to container
        addSlot(new IngredientSlot(blockInv, 0, 67, 35));
        addSlot(new ElementSlot(blockInv, 1, 24, 35));
        addSlot(new OutputSlot(blockInv, 2, 127, 35));

        //player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, (166 - (4 - row) * 18 - 10) + 11));
            }
        }

        // Player Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 142 + 11));
        }
    }

    public EnricherContainer(final int id, final PlayerInventory inv, final PacketBuffer buffer) {
        this(id, inv, new Inventory(3), new IntArray(3));
    }

    public EnricherContainer (int id, PlayerInventory playerInv, IIntArray data) {
        this(id, playerInv, new Inventory(3), data);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            if (index < EnricherTile.slots
                    && !this.moveItemStackTo(stack1, EnricherTile.slots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            if (!this.moveItemStackTo(stack1, 0, EnricherTile.slots, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    // Dist marked similar as vanilla code
    @OnlyIn(Dist.CLIENT)
    public int getProcess() {
        int process = data.get(0);
        int maxTick = data.get(1);
        return maxTick != 0 && process != 0 ? process * 24 / maxTick : 0;
    }

    /**
     * @return true - if element is charged (1)
     * false - if element slot is empty (0)
     */

    // Dist marked similar as vanilla code
    @OnlyIn(Dist.CLIENT)
    public boolean isElementCharged() {
        return data.get(2) == 1;
    }
}

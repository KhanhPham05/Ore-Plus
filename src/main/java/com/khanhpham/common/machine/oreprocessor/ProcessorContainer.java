package com.khanhpham.common.machine.oreprocessor;

import com.khanhpham.api.slots.FuelSlot;
import com.khanhpham.api.slots.IngredientSlot;
import com.khanhpham.api.slots.OutputSlot;
import com.khanhpham.api.slots.UpgradeSlot;
import com.khanhpham.registries.ContainerRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

import java.util.Objects;

//slots for block gui
public class ProcessorContainer extends Container {
    private final IIntArray data;

    //MAIN CONSTRUCTOR
    public ProcessorContainer(int p_i50105_2_, PlayerInventory inv, ProcessorTile tile, IIntArray data) {
        super(ContainerRegistries.PROCESSOR.get(), p_i50105_2_);
        this.data = data;

        int xOffset = 30;
        addSlot(new IngredientSlot(tile, 0, 97 - xOffset, 35));
        addSlot(new FuelSlot(tile, 1, 54 - xOffset, 35));
        addSlot(new OutputSlot(tile, 2, 158 - xOffset, 35));

        addSlot(new UpgradeSlot(tile, 3, 9 - xOffset, 10));

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

        addDataSlots(this.data);
    }

    public ProcessorContainer(int p_i50105_2_, PlayerInventory playerInv, PacketBuffer buffer) {
        this(p_i50105_2_, playerInv, getTileEntity(playerInv, buffer), new IntArray(ProcessorTile.dataCount));
    }


    private static ProcessorTile getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
        Objects.requireNonNull(playerInv, "Player Inventory cannot be null.");
        Objects.requireNonNull(data, "Packet Buffer cannot be null.");
        final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
        if (te instanceof ProcessorTile) {
            return (ProcessorTile) te;
        }
        throw new IllegalStateException("Tile Entity Is Not Correct");
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true;
    }

    public int getProcess() {
        int process = data.get(0);
        int maxTick = data.get(1);
        return maxTick != 0 && process != 0 ? process * 24 / maxTick : 0;
    }

    public boolean isSpeedUpgraded() {
        return data.get(5) == 1;
    }

    public int getFuel() {
        int currentFuel = data.get(3);
        int maxFuel = data.get(4);
        return maxFuel != 0 && currentFuel != 0 ? currentFuel * 21 / maxFuel : 0;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            if (index < ProcessorTile.slots
                    && !this.moveItemStackTo(stack1, ProcessorTile.slots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            if (!this.moveItemStackTo(stack1, 0, ProcessorTile.slots, false)) {
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
}

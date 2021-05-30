package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.api.ElementSlot;
import com.khanhpham.api.IngredientSlot;
import com.khanhpham.registries.BlockRegistries;
import com.khanhpham.registries.ContainerRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

public class EnricherContainer extends Container {
    public final EnricherTile te;
    private final IWorldPosCallable canInteractWithCallable;
    private final IIntArray data;

    /**
     * @see net.minecraft.inventory.container.AbstractFurnaceContainer
     * @see net.minecraft.block.Blocks#FURNACE
     */
    public EnricherContainer(final int p_i50105_2_, final PlayerInventory inv, final EnricherTile te, IIntArray data) {
        super(ContainerRegistries.ENRICHER.get(), p_i50105_2_);
        this.canInteractWithCallable = IWorldPosCallable.create(Objects.requireNonNull(te.getLevel()), te.getBlockPos());
        this.te = te;
        this.data = data;
        //BLOCK CONTAINER
        addSlot(new IngredientSlot(te, 0, 67, 35));
        addSlot(new ElementSlot((IInventory) te, 1, 24, 35));
        addSlot(new FurnaceResultSlot(inv.player, te, 2, 127, 35));


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
        this(id, inv, getTileEntity(inv, buffer), new IntArray(3));
    }

    private static EnricherTile getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
        Objects.requireNonNull(playerInv, "Player Inventory cannot be null.");
        Objects.requireNonNull(data, "Packet Buffer cannot be null.");
        final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
        if (te instanceof EnricherTile) {
            return (EnricherTile) te;
        }
        throw new IllegalStateException("Tile Entity Is Not Correct");
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return stillValid(canInteractWithCallable, player, BlockRegistries.ORE_ENRICHER.get());
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
     *         false - if element slot is empty (0)
     */

    // Dist marked similar as vanilla code
    @OnlyIn(Dist.CLIENT)
    public boolean isElementCharged() {
        return data.get(2) == 1;
    }
}

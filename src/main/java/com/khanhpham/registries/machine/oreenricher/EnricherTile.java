package com.khanhpham.registries.machine.oreenricher;

import com.khanhpham.RawOres;
import com.khanhpham.registries.RecipeTypeRegistries;
import com.khanhpham.registries.TileEntityRegistries;
import com.khanhpham.registries.recipe.OreEnriching;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class EnricherTile extends LockableLootTileEntity implements ITickableTileEntity {

    public EnricherTile() {
        super(TileEntityRegistries.ENRICHER_TILE.get());
    }

    protected static final int slots = 3;
    private NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
    private static ITextComponent title;

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        items = itemsIn;
    }

    @Override
    protected ITextComponent getDefaultName() {
        title = new TranslationTextComponent("container." + RawOres.MODID + ".enricher");
        return title;
    }

    public static ITextComponent getTitle() {
        return title;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory inv) {
        return new EnricherContainer(id, inv, this);
    }

    /**
     * @return size of inv
     */
    @Override
    public int getContainerSize() {
        return slots;
    }

    /**
     * @see net.minecraft.item.crafting.IRecipeSerializer#SMELTING_RECIPE
     */
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        ItemStackHelper.loadAllItems(nbt, this.items);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    /**
     * @see net.minecraft.inventory.container.AbstractFurnaceContainer
     * @see net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen
     * @see Items#COAL
     * @see net.minecraftforge.common.ForgeHooks
     */

    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide) {
            //EXCHANGE INPUT - OUTPUT
            if (!items.get(0).isEmpty()) {
                OreEnriching recipe = getRecipe();
                if (recipe != null) {

                    //INPUT ITEM CHANGES
                    if (items.get(0).getCount() <= 0) {
                        items.set(0, ItemStack.EMPTY);
                    } else {
                        items.get(0).shrink(1);
                    }
                    //RESULT ITEM CHANGES

                    ItemStack stack1 = items.get(2);
                    if (!stack1.isEmpty()) {
                        items.get(2).grow(recipe.output.copy().getCount());
                    } else {
                        items.set(2, recipe.output.copy());

                    }
                }
            }
        }
        setChanged();
    }


    private void modifySlot(NonNullList<ItemStack> slots, int slot, int amount) {
        if (slots.get(slot).isEmpty() && slot >= 0 && slot < slots.size()) {
            slots.get(slot).grow(amount);
            if (slots.get(slot).getCount() <= 0) {
                slots.set(slot, ItemStack.EMPTY);
            }
        }
    }

    @Nullable
    private OreEnriching getRecipe() {
        if (items.get(0).isEmpty()) {
            return null;
        }

        //TEMPORARY LEFT THIS AWAY
        assert level != null;
        OreEnriching recipe = level.getRecipeManager().getRecipeFor(RecipeTypeRegistries.ORE_ENRICHING, this, level).orElse(null);

        return recipe;
    }


}

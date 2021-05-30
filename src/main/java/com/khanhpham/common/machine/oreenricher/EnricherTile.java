package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.common.LangKeys;
import com.khanhpham.common.recipe.OreEnriching;
import com.khanhpham.registries.RecipeTypeRegistries;
import com.khanhpham.registries.TileEntityRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class EnricherTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public EnricherTile() {
        super(TileEntityRegistries.ENRICHER_TILE.get());
    }

    protected static final int slots = 3;
    private final NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
    private final ITextComponent title = getDisplayName();
    private int maxTickPerItem = 160;
    private int processingCurrentTick = 0;
    private int isElementCharged;

    private final IIntArray timeData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return EnricherTile.this.processingCurrentTick;
                case 1:
                    return EnricherTile.this.maxTickPerItem;
                case 2:
                    return EnricherTile.this.isElementCharged;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    EnricherTile.this.processingCurrentTick = value;
                case 1:
                    EnricherTile.this.maxTickPerItem = value;
                case 2:
                    if (value > 3 || value < 0)
                        throw new IllegalStateException("isElementCharged can not different with 0 - 2");
                    else
                        EnricherTile.this.isElementCharged = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide) {
            ItemStack elementSlot = items.get(1);
            ItemStack inputSlot = items.get(0);
            isElementCharged = elementSlot.isEmpty() ? 0 : 1;
            if (!elementSlot.isEmpty() && !inputSlot.isEmpty()) {
                OreEnriching recipe = getRecipe();
                if (canProcessFromRecipe(recipe)) {
                    ++processingCurrentTick;
                    if (processingCurrentTick == maxTickPerItem) {
                        processingCurrentTick = 0;
                        processing(recipe);
                    }
                } else {
                    processingCurrentTick = 0;
                    setChanged();
                }
            }
        }
        setChanged();
    }

    /**
     * @see net.minecraft.inventory.container.AbstractFurnaceContainer
     * @see net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen
     * @see net.minecraftforge.common.ForgeHooks
     * @see net.minecraft.tileentity.AbstractFurnaceTileEntity
     */

    /*private boolean canProcess(@Nullable OreEnriching recipe) {
        ItemStack inputSlot = items.get(0);
        ItemStack elementSlot = items.get(1);
        ItemStack outputSlot = items.get(2);

        if (inputSlot.isEmpty() || elementSlot.isEmpty()) {
            return false;
        } else if (recipe == null) {
            return false;
        } else if (!elementSlot.isEmpty()) {
            ItemStack output = recipe.getResultItem();
            return !inputSlot.isEmpty() && (outputSlot.isEmpty() || outputSlot.sameItem(output));
        } else return false;
    }*/
    private void processing(@Nullable OreEnriching recipe) {
        if (recipe != null && canProcessFromRecipe(recipe)) {
            ItemStack inputSlot = items.get(0);
            ItemStack result = recipe.getResultItem();
            ItemStack outputSlot = items.get(2);

            if (outputSlot.isEmpty()) {
                items.set(2, result.copy());
            } else if (outputSlot.getItem() == result.getItem()) {
                outputSlot.grow(1);
            }

            inputSlot.shrink(1);
            items.get(1).shrink(1);
            setChanged();
        }
    }

    private boolean canProcessFromRecipe(@Nullable OreEnriching recipe) {
        if (!this.items.get(0).isEmpty() && recipe != null) {
            ItemStack stack = recipe.getResultItem();
            if (stack.isEmpty()) {
                return false;
            } else {
                ItemStack resultSlot = items.get(2);
                if (resultSlot.isEmpty()) {
                    return true;
                } else if (!resultSlot.sameItem(stack)) {
                    return false;
                } else if (resultSlot.getCount() + stack.getCount() <= 64 && resultSlot.getCount() + stack.getCount() <= resultSlot.getMaxStackSize()) {
                    return true;
                } else {
                    return resultSlot.getCount() + stack.getCount() <= stack.getMaxStackSize();
                }
            }
        } else return false;
    }


    @Nullable
    private OreEnriching getRecipe() {
        if (items.get(0).isEmpty()) {
            return null;
        }
        assert level != null;
        return level.getRecipeManager().getRecipeFor(RecipeTypeRegistries.ORE_ENRICHING, (IInventory) this, level).orElse(null);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        ItemStackHelper.loadAllItems(nbt, this.items);
        processingCurrentTick = nbt.getInt("ProcessingCurrentTime");
        maxTickPerItem = nbt.getInt("MaxTimePerItemProcessing");
        isElementCharged = nbt.getInt("IsContainsElement");
    }

    /*
     Fix save / load issue affected the machine processing
     by move ItemStackHelper.saveAllItems(compound, this.items);
     down to the end;
    */
    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("ProcessingCurrentTime", processingCurrentTick);
        compound.putInt("MaxTimePerItemProcessing", maxTickPerItem);
        compound.putInt("IsContainsElement", isElementCharged);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    @Override
    public ITextComponent getDisplayName() {
        return LangKeys.ENRICHER_SCREEN;
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new EnricherContainer(p_createMenu_1_, p_createMenu_2_, timeData);
    }
}

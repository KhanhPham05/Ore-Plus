package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.api.ISpeedUpgrade;
import com.khanhpham.api.IUpgradeable;
import com.khanhpham.common.LangKeys;
import com.khanhpham.common.items.AbstractSpeedUpgrade;
import com.khanhpham.common.recipe.OreEnriching;
import com.khanhpham.registries.RecipeTypeRegistries;
import com.khanhpham.registries.TileEntityRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * @see net.minecraft.tileentity.LockableLootTileEntity
 */
public class EnricherTile extends LockableLootTileEntity implements ITickableTileEntity, INamedContainerProvider {

    protected static final int slots = 6;
    private NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
    private int maxTickPerItem = 160;
    private int processingCurrentTick = 0;
    private int isElementCharged;
    private int isSpeedUpgraded;
    private final IIntArray blockData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return EnricherTile.this.processingCurrentTick;
                case 1:
                    return EnricherTile.this.maxTickPerItem;
                case 2:
                    return EnricherTile.this.isElementCharged;
                case 3:
                    return EnricherTile.this.isSpeedUpgraded;
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
                        throw new IllegalStateException("isElementCharged (2) can not different with 0 - 1");
                    else
                        EnricherTile.this.isElementCharged = value;
                case 3:
                    if (value > 3 || value < 0)
                    throw new IllegalStateException("isSpeedUpgraded (3) can not different with 0 - 1");
                else
                    EnricherTile.this.isSpeedUpgraded = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public EnricherTile() {
        super(TileEntityRegistries.ENRICHER_TILE.get());
    }

    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide) {
            ItemStack elementSlot = items.get(1);
            ItemStack inputSlot = items.get(0);
            isElementCharged = elementSlot.isEmpty() ? 0 : 1;
            isSpeedUpgraded = items.get(3).isEmpty() ? 0 : 1;

            if (!elementSlot.isEmpty() && !inputSlot.isEmpty()) {
                OreEnriching recipe = getRecipe();
                if (canProcessFromRecipe(recipe)) {
                    if (isSpeedUpgraded(items)) {
                        processingCurrentTick = applyUpgrade(items, processingCurrentTick);
                    } else
                        ++processingCurrentTick;
                    if (processingCurrentTick == maxTickPerItem) {
                        processingCurrentTick = 0;
                        processing(recipe);
                    }
                } else {
                    processingCurrentTick = 0;
                    setChanged();
                }
            } else {
                processingCurrentTick = 0;
            }
        }
        setChanged();
    }


    private boolean isSpeedUpgraded(NonNullList<ItemStack> items) {
        return items.get(3).getItem() instanceof AbstractSpeedUpgrade;
    }

    private int applyUpgrade(NonNullList<ItemStack> items, int currentTick) {
        ItemStack upgradeSlot = items.get(3);
        if (upgradeSlot.getItem() instanceof ISpeedUpgrade) {
            ISpeedUpgrade upgrade = (ISpeedUpgrade) upgradeSlot.getItem();
            return currentTick + upgrade.speedEffect();
        }
        return currentTick + 1;
    }

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
        isSpeedUpgraded = nbt.getInt("IsSpeedUpgraded");
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
        compound.putInt("IsSpeedUpgraded", isSpeedUpgraded);
        ItemStackHelper.saveAllItems(compound, items);
        return compound;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return LangKeys.ENRICHER_SCREEN;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> p_199721_1_) {
        this.items = p_199721_1_;
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return new EnricherContainer(p_213906_1_, p_213906_2_, this, blockData);
    }

    @Override
    public int getContainerSize() {
        return slots;
    }
}

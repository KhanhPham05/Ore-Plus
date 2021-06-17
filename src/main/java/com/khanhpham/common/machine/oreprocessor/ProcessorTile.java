package com.khanhpham.common.machine.oreprocessor;

import com.khanhpham.api.ISpeedUpgrade;
import com.khanhpham.common.LangKeys;
import com.khanhpham.common.items.AbstractSpeedUpgrade;
import com.khanhpham.common.recipe.OreProcessing;
import com.khanhpham.registries.RecipeTypeRegistries;
import com.khanhpham.registries.TileEntityRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

//the core of minecraft engine TILE ENTITY
public class ProcessorTile extends LockableLootTileEntity implements ITickableTileEntity {
    public static final int dataCount = 6;
    public static final int slots = 4;
    private int maxFuel;
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private int currentTick;
    private int maxTick = 160;
    private int containsFuel;
    private int fuelRemains;
    private int speedUpgraded;

    private final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return currentTick;
                case 1:
                    return maxTick;
                case 2:
                    return containsFuel;
                case 3:
                    return fuelRemains;
                case 4:
                    return maxFuel;
                case 5:
                    return speedUpgraded;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    currentTick = value;
                case 1:
                    throw new IllegalStateException("you can not change this value");
                case 2:
                    containsFuel = setValue(value);
                case 3:
                    fuelRemains = value;
                case 4:
                    throw new IllegalStateException("you can not change this value");
                case 5:
                    speedUpgraded = setValue(value);
            }
        }

        @Override
        public int getCount() {
            return dataCount;
        }
    };

    public ProcessorTile() {
        super(TileEntityRegistries.PROCESSOR_TILE.get());
    }

    /**
     * @see net.minecraft.block.Blocks#FURNACE
     */
    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide) {
            ItemStack inputSlot = items.get(0);
            ItemStack fuelSlot = items.get(1);
            ItemStack upgradeSlot = items.get(3);

            if (!fuelSlot.isEmpty() && fuelRemains <= 0) {
                maxFuel = getBurnTime(fuelSlot);
                fuelRemains = maxFuel;
                fuelSlot.shrink(1);
            }

            speedUpgraded = upgradeSlot.isEmpty() ? 0 : 1;

            if (!inputSlot.isEmpty()) {
                OreProcessing recipe = getRecipe();
                if (canProcess(recipe)) {
                    if (isSpeedUpgraded(items)) {
                        currentTick = applyUpgrade(items, currentTick);
                    } else {
                        ++currentTick;
                        level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(OreProcessor.WORKING, Boolean.TRUE), 3);
                    }

                    if (currentTick >= maxTick) {
                        currentTick = 0;
                        process(recipe);
                    }
                } else {
                    level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(OreProcessor.WORKING, Boolean.FALSE), 3);
                    currentTick = 0;
                    setChanged();
                }
            } else {
                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(OreProcessor.WORKING, Boolean.FALSE), 3);
                currentTick = 0;
            }
        }
        setChanged();
    }

    private void process(@Nullable OreProcessing recipe) {
        if (recipe != null && canProcess(recipe)) {
            ItemStack inputSlot = items.get(0);
            ItemStack result = recipe.getResultItem();
            ItemStack outputSlot = items.get(2);
            --fuelRemains;

            if (outputSlot.isEmpty()) {
                items.set(2, result.copy());
                items.get(2).grow(1);
            } else if (outputSlot.getItem() == result.getItem()) {
                outputSlot.grow(recipe.getResultCount());
            }
            inputSlot.shrink(1);
            setChanged();
        }
    }

    private int applyUpgrade(NonNullList<ItemStack> items, int currentTick) {
        ItemStack upgradeSlot = items.get(3);
        if (upgradeSlot.getItem() instanceof AbstractSpeedUpgrade) {
            ISpeedUpgrade upgrade = (ISpeedUpgrade) upgradeSlot.getItem();
            return currentTick + upgrade.speedEffect();
        }
        return currentTick + 1;
    }

    private boolean isSpeedUpgraded(NonNullList<ItemStack> items) {
        return items.get(3).getItem() instanceof AbstractSpeedUpgrade;
    }

    private boolean canProcess(@Nullable OreProcessing recipe) {
        if (!items.get(0).isEmpty() && recipe != null && fuelRemains > 0) {
            ItemStack outputSlot = items.get(2);
            if (outputSlot.isEmpty()) {
                return true;
            } else return outputSlot.getCount() + recipe.getResultCount() <= 64;

        } else {
            return false;
        }
    }

    @Nullable
    private OreProcessing getRecipe() {
        if (items.get(0).isEmpty()) {
            return null;
        } else {
            assert level != null;
            //System.out.println("Recipe collected");
            return level.getRecipeManager().getRecipeFor(RecipeTypeRegistries.ORE_PROCESSING, this, level).orElse(null);
        }
    }

    private int getBurnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack) / 50;
    }

    private int setValue(int value) {
        if (value < 0 || value > 3) throw new IllegalStateException("this value have to be 0 or 1");
        return value;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> p_199721_1_) {
        items = p_199721_1_;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return LangKeys.PROCESSOR_SCREEN;
    }

    @Override
    protected Container createMenu(int p_213906_1_, PlayerInventory p_213906_2_) {
        return new ProcessorContainer(p_213906_1_, p_213906_2_, this, data);
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT nbt) {
        super.load(p_230337_1_, nbt);
        ItemStackHelper.loadAllItems(nbt, items);
        currentTick = nbt.getInt("ProcessingCurrentTick");
        maxTick = nbt.getInt("MaxTimePerItemProcessing");
        containsFuel = nbt.getInt("IsContainsFuel");
        fuelRemains = nbt.getInt("FuelRemains");
        maxFuel = nbt.getInt("MaxFuel");
        speedUpgraded = nbt.getInt("IsSpeedUpgraded");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("ProcessingCurrentTick", currentTick);
        compound.putInt("MaxTimePerItemProcessing", maxTick);
        compound.putInt("IsContainsFuel", containsFuel);
        compound.putInt("FuelRemains", fuelRemains);
        compound.putInt("MaxFuel", maxFuel);
        compound.putInt("IsSpeedUpgraded", speedUpgraded);
        ItemStackHelper.saveAllItems(compound, items);
        return compound;
    }
}

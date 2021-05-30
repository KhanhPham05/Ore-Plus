package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.RawOres;
import com.khanhpham.common.recipe.OreEnriching;
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
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class EnricherTile extends LockableLootTileEntity implements ITickableTileEntity {

    public EnricherTile() {
        super(TileEntityRegistries.ENRICHER_TILE.get());
    }

    protected static final int slots = 3;
    private NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
    private static ITextComponent title;
    private int maxTickPerItem = 160;
    private int processingCurrentTick = 0;

    private final IIntArray timeData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return EnricherTile.this.processingCurrentTick;
                case 1:
                    return EnricherTile.this.maxTickPerItem;
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
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

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
        return new EnricherContainer(id, inv, this, timeData);
    }

    @Override
    public int getContainerSize() {
        return slots;
    }

    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide) {
            ItemStack elementSlot = items.get(1);
            ItemStack inputSlot = items.get(0);
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
                }
            }

           /* OreEnriching recipe = getRecipe();
            if (recipe != null)
                if (canProcess(recipe)) {
                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(OreEnricher.WORKING, canProcess(recipe)), 3);
            }*/
        }
        setChanged();
    }

    /**
     * @see net.minecraft.inventory.container.AbstractFurnaceContainer
     * @see net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen
     * @see net.minecraftforge.common.ForgeHooks
     * @see net.minecraft.tileentity.AbstractFurnaceTileEntity
     */

    private boolean canProcess(@Nullable OreEnriching recipe) {
        ItemStack inputSlot = items.get(0);
        ItemStack elementSlot = items.get(1);
        ItemStack outputSlot = items.get(2);

        if (inputSlot.isEmpty() || elementSlot.isEmpty()) {
            return false;
        } else if (recipe == null) {
            return false;
        } else if (!elementSlot.isEmpty()) {
            ItemStack output = recipe.getResultItem();
            if (!inputSlot.isEmpty() && (outputSlot.isEmpty() || outputSlot.sameItem(output))) {
                return true;
            } else return false;
        } else return false;
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
                } else if (resultSlot.getCount() + stack.getCount() <= getMaxStackSize() && resultSlot.getCount() + stack.getCount() <= resultSlot.getMaxStackSize()) {
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
        return level.getRecipeManager().getRecipeFor(RecipeTypeRegistries.ORE_ENRICHING, this, level).orElse(null);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        ItemStackHelper.loadAllItems(nbt, this.items);
      /* processingCurrentTime = nbt.getInt("ProcessingCurrentTime");
        maxTickPerItem = nbt.getInt("MaxTimePerItemProcessing");*/
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        ItemStackHelper.saveAllItems(compound, this.items);
       /* compound.putInt("ProcessingCurrentTime", processingCurrentTime);
        compound.putInt("MaxTimePerItemProcessing", maxTickPerItem);*/
        return compound;
    }
}

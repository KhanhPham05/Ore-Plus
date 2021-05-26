package com.khanhpham.registries.machine.oreenricher;

import com.khanhpham.RawOres;
import com.khanhpham.registries.recipe.OreEnriching;
import com.khanhpham.registries.RecipeTypeRegistries;
import com.khanhpham.registries.TileEntityRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

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
    private int fuel = 0;

    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide) {
            if (!items.get(1).isEmpty() && ForgeHooks.getBurnTime(items.get(1)) >= 0) {
                if (fuel <= 0) {
                    fuel = ForgeHooks.getBurnTime(items.get(1)) / 200;

                    //charge fuels and remove 1 fuel
                    if (items.get(1).getCount() <= 0) {
                        items.set(1, ItemStack.EMPTY);
                    } else {
                        items.get(1).shrink(1);
                    }
                }

                //EXCHANGE INPUT - OUTPUT
                if (!items.get(0).isEmpty()) {
                    OreEnriching recipe = getRecipe();
                    if (recipe != null) {

                        //INPUT ITEM CHANGES
                        if (items.get(0).getCount() <= 0) {
                            items.set(0, ItemStack.EMPTY);
                        } else {
                            items.get(0).shrink(1);
                            --fuel;
                        }


                        // if (fuel <= 0)

                        //RESULT ITEM CHANGES
                        if (fuel > 0) {
                            ItemStack stack1 = items.get(2);
                            if (!stack1.isEmpty()) {
                                items.get(2).grow(recipe.output.copy().getCount());
                            } else {
                                items.set(2, recipe.output.copy());
                            }
                        }
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

    /**
     * @see World#getRecipeManager()
     * @see net.minecraft.item.crafting.RecipeManager#getAllRecipesFor(IRecipeType)
     */
   /* @Override
    public void tick() {
        boolean working = isWorking();
        boolean working1 = false;

        if (isWorking()) {
            --workingTime;
        }

        if (level.isClientSide) {
            ItemStack stack = items.get(1);
            if (isWorking() || !stack.isEmpty() && !this.items.get(0).isEmpty()) {
                IRecipe<?> irecipe = level.getRecipeManager().getRecipeFor(RecipeTypeRegistries.ORE_ENRICHING, this, level).orElse(null);
                if (!isWorking() && canBurn(irecipe)) {
                    workingTime = getBurnDuration(stack);
                    workingDuration = workingTime;
                    if (isWorking()) {
                        working1 = true;
                        if (stack.hasContainerItem()) {
                            items.set(1, stack.getContainerItem());
                        } else {
                            if (!stack.isEmpty()) {
                                Item item = stack.getItem();
                                stack.shrink(1);
                                if (stack.isEmpty()) {
                                    items.set(1, stack.getContainerItem());
                                }
                            }
                        }
                    }
                }

                if (isWorking() && canBurn(irecipe)) {
                    ++workingProgress;
                    if (workingProgress == totalWorkingTime) {
                        workingProgress = 0;
                        totalWorkingTime = getTotalWorkingTime();
                        burn(irecipe);
                        working1 = true;
                    }
                } else {
                    workingProgress = 0;
                }
            } else if (!isWorking() && workingProgress > 0) {
                workingProgress = MathHelper.clamp(workingProgress - 2, 0, totalWorkingTime);
            }

            if (working != isWorking()) {
                working = true;
                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(AbstractFurnaceBlock.LIT, isWorking()), 3);
            }
        }

        if (working1) {
            setChanged();
        }
    }

    private void burn(@Nullable IRecipe<?> p_214007_1_) {
        if (p_214007_1_ != null && this.canBurn(p_214007_1_)) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = p_214007_1_.getResultItem();
            ItemStack itemstack2 = this.items.get(2);
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!this.level.isClientSide) {
                this.setRecipeUsed(p_214007_1_);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    protected int getTotalWorkingTime() {
        return this.level.getRecipeManager().getRecipeFor(RecipeTypeRegistries.ORE_ENRICHING, this, this.level).map(OreEnriching::getWorkingTime).orElse(200);
    }

    protected int getBurnDuration(ItemStack p_213997_1_) {
        if (p_213997_1_.isEmpty()) {
            return 0;
        } else {
            Item item = p_213997_1_.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(p_213997_1_);
        }
    }

    protected boolean canBurn(@Nullable IRecipe<?> p_214008_1_) {
        if (!this.items.get(0).isEmpty() && p_214008_1_ != null) {
            ItemStack itemstack = p_214008_1_.getResultItem();
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.items.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getMaxStackSize() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }*/
}

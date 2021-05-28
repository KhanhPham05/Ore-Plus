package com.khanhpham.common.machine.oreenricher;

import com.khanhpham.RawOres;
import com.khanhpham.api.IEnricherElement;
import com.khanhpham.common.recipe.OreEnriching;
import com.khanhpham.registries.ItemRegistries;
import com.khanhpham.registries.RecipeTypeRegistries;
import com.khanhpham.registries.TileEntityRegistries;
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
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class EnricherTile extends LockableLootTileEntity implements ITickableTileEntity {

    public EnricherTile() {
        super(TileEntityRegistries.ENRICHER_TILE.get());
    }

    protected static final int slots = 3;
    private NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
    private static ITextComponent title;
    private final int MaxTickPerItem = 160;
    private int tick = 160;
    private int totalTick;

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
     * @see net.minecraft.tileentity.AbstractFurnaceTileEntity
     * @see IEnergyStorage#getEnergyStored()
     */

    @Override
    public void tick() {
        if (items.get(1).isEmpty() || items.get(0).isEmpty()) return;
        else {
            if (items.get(1).getItem() instanceof IEnricherElement) {
                //EXCHANGE INPUT - OUTPUT
                if (!items.get(0).isEmpty()) {
                    OreEnriching recipe = getRecipe();
                    if (recipe != null && isTicking(tick)) {

                        //INPUT ITEM CHANGES
                        if (items.get(0).isEmpty() || items.get(1).isEmpty()) {
                            items.set(0, ItemStack.EMPTY);
                            items.set(1, ItemStack.EMPTY);
                        } else {
                            items.get(0).shrink(1);
                            items.get(1).shrink(1);
                            //RESULT ITEM CHANGES

                            ItemStack stack1 = items.get(2);
                            if (!stack1.isEmpty()) {
                                items.get(2).grow(recipe.output.copy().getCount());
                            } else {
                                items.set(2, recipe.output.copy());
                            }
                        }
                    }
                    --tick;
                }
            }
        }


        setChanged();
    }

    /**
     * @see net.minecraft.tileentity.AbstractFurnaceTileEntity
     */
    private boolean isTicking(int tick) {
        return tick <= 0;
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

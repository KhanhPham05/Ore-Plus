package com.khanhpham.client.datagen;

import com.khanhpham.OrePlusLT;
import com.khanhpham.client.datagen.loottable.ModLootTable;
import com.khanhpham.client.datagen.models.ModelProvider;
import com.khanhpham.client.datagen.recipe.ModRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

/**
 * @see net.minecraft.data.Main
 */
@Mod.EventBusSubscriber(modid = OrePlusLT.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEvent {

    private DataGenEvent() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator data = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        data.addProvider(new ModelProvider.Item(data, helper));
        data.addProvider(new ModelProvider.BlockState(data, helper));
        data.addProvider(new ModRecipes(data));
        data.addProvider(new ModLootTable(data));
        data.addProvider(new LangProvider(data));

    }
}

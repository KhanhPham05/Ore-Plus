package com.khanhpham;

import com.khanhpham.events.oregen.OreGenerator;
import com.khanhpham.registries.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OrePlusLT.MODID)
public class OrePlusLT {
    public static final String MODID = "oreplus";

    public static final ItemGroup RAW_ORES = new ItemGroup("raw_ores") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistries.IRON_RAW_ORE.get());
        }
    };

    public OrePlusLT() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();


        bus.register(this);
        ItemRegistries.ITEMS.register(bus);
        BlockRegistries.BLOCKS.register(bus);
        EnchantRegistries.ENCHANTS.register(bus);
        TileEntityRegistries.TILE_TYPES.register(bus);
        ContainerRegistries.CONTAINERS.register(bus);

        bus.addGenericListener(IRecipeSerializer.class, RecipeTypeRegistries::registerRecipes);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, OreGenerator::addOres);
    }
}

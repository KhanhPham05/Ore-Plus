package com.khanhpham.registries;

import com.khanhpham.OrePlusLT;
import com.khanhpham.common.items.AbstractSpeedUpgrade;
import com.khanhpham.common.items.EnricherElement;
import com.khanhpham.common.items.upgradespeed.SpeedMk1;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ItemRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OrePlusLT.MODID);

    public static final RegistryObject<Item> IRON_RAW_ORE = ITEMS.register("raw_iron", () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON).tab(OrePlusLT.RAW_ORES)));

    public static final RegistryObject<EnricherElement> ENRICHING_ELEMENT = ITEMS.register("enriching_element", EnricherElement::new);

    public static final RegistryObject<AbstractSpeedUpgrade> SPEED_MK_I = registerUpgrade("speed_upgrade_mk_1", SpeedMk1::new);

    private static RegistryObject<AbstractSpeedUpgrade> registerUpgrade(String name, Supplier<AbstractSpeedUpgrade> supplier) {
        return ITEMS.register(name, supplier);
    }

}

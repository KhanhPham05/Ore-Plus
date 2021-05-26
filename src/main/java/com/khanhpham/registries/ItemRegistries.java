package com.khanhpham.registries;

import com.khanhpham.RawOres;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistries {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RawOres.MODID);

    public static final RegistryObject<Item> IRON_RAW_ORE = ITEMS.register("raw_iron", () ->  new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON).tab(RawOres.RAW_ORES)));
    public static final RegistryObject<Item> GOLD_RAW_ORE = registryRawOre("raw_gold");
    public static final RegistryObject<Item> COPPER_RAW_ORE = registryRawOre("raw_copper");
    public static final RegistryObject<Item> RAW_DIAMOND = registryRawOre("raw_diamond");
    public static final RegistryObject<Item> RICH_COAL = register("rich_coal");
    public static final RegistryObject<Item> ENRICHING_ELEMENT = register("enriching_element");
    /**
     * @see net.minecraft.item.Items
     */
    private static RegistryObject<Item> registryRawOre(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON).tab(RawOres.RAW_ORES)));
    }

    private static RegistryObject<Item> register(String name) {
        return ITEMS.register(name, ()-> new Item(new Item.Properties()
                .tab(RawOres.RAW_ORES)
                .stacksTo(64)
                .setNoRepair()
        ));
    }

}

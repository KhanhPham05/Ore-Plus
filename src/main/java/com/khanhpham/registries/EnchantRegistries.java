package com.khanhpham.registries;

import com.khanhpham.OrePlusLT;
import com.khanhpham.common.enchantments.RichMining;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @see net.minecraft.enchantment.Enchantments
 */
public class EnchantRegistries {
    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, OrePlusLT.MODID);

    public static final RegistryObject<RichMining> RICH_MINING = ENCHANTS.register("rich_mining", RichMining::new);
}

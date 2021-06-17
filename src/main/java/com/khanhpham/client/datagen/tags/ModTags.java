package com.khanhpham.client.datagen.tags;

import com.khanhpham.OrePlusLT;
import com.khanhpham.registries.ItemRegistries;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.TagRegistry;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.nio.file.Path;

public class ModTags extends ItemTagsProvider {
    public ModTags(DataGenerator data, BlockTagsProvider blockTag, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(data, blockTag, modId, existingFileHelper);
    }

    private static final TagRegistry<Item> HELPER = TagRegistryManager.create(new ResourceLocation(OrePlusLT.MODID,"item"), ITagCollectionSupplier::getItems);

    public static final ITag.INamedTag<Item> IRON = HELPER.bind("raw_ores/iron");
    public static final ITag.INamedTag<Item> COPPER = HELPER.bind("raw_ores/copper");
    public static final ITag.INamedTag<Item> DIAMOND = HELPER.bind("raw_ores/diamond");
    public static final ITag.INamedTag<Item> GOLD = HELPER.bind("raw_ores/gold");

    @Override
    protected void addTags() {
        this.tag(IRON).add(ItemRegistries.IRON_RAW_ORE.get());
        /*this.tag(COPPER).add(ItemRegistries.COPPER_RAW_ORE.get());
        this.tag(DIAMOND).add(ItemRegistries.RAW_DIAMOND.get());
        this.tag(GOLD).add(ItemRegistries.GOLD_RAW_ORE.get());*/
    }

    @Override
    protected Path getPath(ResourceLocation rl) {
        return this.generator.getOutputFolder().resolve("data/" + rl.getNamespace() + "/tags/items/" + rl.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Raw Ores Tags";
    }
}

package com.khanhpham.client.datagen.loottable;

import com.google.common.collect.ImmutableList;
import com.khanhpham.RawOres;
import com.khanhpham.registries.BlockRegistries;
import com.khanhpham.registries.EnchantRegistries;
import com.khanhpham.registries.ItemRegistries;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTable extends LootTableProvider {
    public ModLootTable(DataGenerator data) {
        super(data);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(ModBlockLoot::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker tracker) {
        map.forEach((p_218436_2_, p_218436_3_) ->
                LootTableManager.validate(tracker, p_218436_2_, p_218436_3_));
    }

    @Override
    public String getName() {
        return RawOres.MODID + " Loot Tables Providers";
    }

    public static final class ModBlockLoot extends BlockLootTables {
        private static final ILootCondition.IBuilder HAS_RICH_MINING = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(EnchantRegistries.RICH_MINING.get(), MinMaxBounds.IntBound.atLeast(1))));

        /**
         * @see BlockLootTables line 627 for STONE to cobblestone
         * @see BlockLootTables#createSingleItemTableWithSilkTouch(Block, IItemProvider)
         * @see net.minecraft.item.Items#ROTTEN_FLESH
         * */
        @Override
        protected void addTables() {
            dropSelf(BlockRegistries.RAW_IRON_BLOCK.get());
            add(BlockRegistries.RICH_IRON_ORE.get(), (ore) -> createRichMiningWithSilkTouch(ore, Blocks.IRON_ORE.asItem()));
            dropSelf(BlockRegistries.ORE_ENRICHER.get());
            dropSelf(BlockRegistries.ORE_PROCESSOR.get());
            add(BlockRegistries.ORE_ENRICHER.get(), BlockLootTables::createNameableBlockEntityTable);
            add(BlockRegistries.ORE_PROCESSOR.get(), BlockLootTables::createNameableBlockEntityTable);
        }


        private static LootTable.Builder createRichMiningWithSilkTouch(Block block, IItemProvider drop) {
            return createRichMiningDispatchTable(block, applyExplosionCondition(block, ItemLootEntry.lootTableItem(drop)));
        }

        private static LootTable.Builder createRichMiningDispatchTable(Block block, LootEntry.Builder<?> builder) {
            return selfDropDispatchTable(block, HAS_RICH_MINING, builder);
        }

        private static LootTable.Builder selfDropDispatchTable(Block block, ILootCondition.IBuilder builder, LootEntry.Builder<?> loot) {
            return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(block).when(builder).otherwise(loot)));
        }


        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BlockRegistries.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get)
                    .collect(Collectors.toList());
        }
    }
}

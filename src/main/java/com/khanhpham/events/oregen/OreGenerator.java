package com.khanhpham.events.oregen;

import com.khanhpham.registries.BlockRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;

/**
 * @see net.minecraft.block.OreBlock
 * @see net.minecraft.world.biome.Biome
 * @see net.minecraft.world.gen.feature.Features
 * @see net.minecraft.world.gen.IDecoratable
 */

//@Mod.EventBusSubscriber(modid = RawOres.MODID, value = Dist.DEDICATED_SERVER, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreGenerator {
    private static final BlockState RICH_IRON = getState(BlockRegistries.RICH_IRON_ORE);

    private static ConfiguredFeature<?, ?> RICH_IRON_VEIN;

   // @SubscribeEvent
    public static void addOres(final BiomeLoadingEvent event) {
        addOre(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                RICH_IRON, 4, 0, 60, 20);
    }

    public static void addOre(final BiomeLoadingEvent event, RuleTest rule, BlockState state, int veinSize,
                              int minHeight, int maxHeight, int amount) {
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.configured(new OreFeatureConfig(rule, state, veinSize))
                        .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight, 0, maxHeight))).squared().count(amount))
        ;
    }


    private static BlockState getState(RegistryObject<? extends Block> block) {
        return block.get().getBlock().defaultBlockState();
    }
}
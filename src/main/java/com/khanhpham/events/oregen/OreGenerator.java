package com.khanhpham.events.oregen;

import com.khanhpham.registries.BlockRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.GenerationStage;
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

//@Mod.EventBusSubscriber(modid = OrePlusLT.MODID, value = Dist.DEDICATED_SERVER, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreGenerator {
    private static final BlockState ELEMENT_ORE = getState(BlockRegistries.ELEMENT_ORE);

    // @SubscribeEvent
    public static void addOres(final BiomeLoadingEvent event) {
        addOre(event, OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                ELEMENT_ORE, 4, 8, 30, 20);
    }

    public static void addOre(final BiomeLoadingEvent event, RuleTest rule, BlockState state, int veinSize,
                              int minHeight, int maxHeight, int amount) {
        event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.configured(new OreFeatureConfig(rule, state, veinSize))
                        .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight, 0, maxHeight))).squared())
        ;
    }

    private static BlockState getState(RegistryObject<? extends Block> block) {
        return block.get().getBlock().defaultBlockState();
    }
}
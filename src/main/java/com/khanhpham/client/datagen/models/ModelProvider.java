package com.khanhpham.client.datagen.models;

import com.khanhpham.RawOres;
import com.khanhpham.registries.BlockRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelProvider {

    public static final class Item extends net.minecraftforge.client.model.generators.ItemModelProvider {
        public Item(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, RawOres.MODID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            block("rich_iron_ore");
            block("raw_iron_block");
            withExistingParent("ore_enricher", modLoc("block/ore_enricher"));
            ModelFile file = getExistingFile(mcLoc("item/generated"));

            build(file, "raw_iron");
            build(file, "raw_copper");
            build(file, "raw_gold");
            build(file, "enriching_element");
        }

        private void build(ModelFile file, String path) {
            getBuilder(path).parent(file).texture("layer0", "item/" + path);
        }

        private void block(String name) {
            withExistingParent(name, modLoc("block/" + name));
        }

        /**
         * @see BlockStateProvider#simpleBlock(Block, net.minecraftforge.client.model.generators.ConfiguredModel...)
         * @see BlockStateProvider#getVariantBuilder(Block)
         * @see VariantBlockStateBuilder#partialState()
         * @see VariantBlockStateBuilder#setModels(VariantBlockStateBuilder.PartialBlockstate, ConfiguredModel...)
         * @see Blocks#FURNACE
         *
         */
    }

    public static final class BlockState extends BlockStateProvider {
        public BlockState(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, RawOres.MODID, existingFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlock(BlockRegistries.RICH_IRON_ORE.get());

            simpleBlock(BlockRegistries.RAW_IRON_BLOCK.get());

            simpleBlock(BlockRegistries.ORE_ENRICHER.get(), models().cubeAll("ore_enricher", modLoc("block/enricher_sides")));
//            axisBlock(new RotatedPillarBlock(AbstractBlock.Properties.copy(BlockRegistries.ORE_ENRICHER.get())),modLoc("block/enricher_sides"),modLoc("block/enricher_face"));

           /* models().withExistingParent("ore_enricher", mcLoc("block/block"))
                    .texture("0", modLoc("block/enricher_face"))
                    .texture("1", modLoc("block/enricher_sides"))
                    .texture("particle", modLoc("block/enricher_sides"))
                    .element()
                    .from(0,0,0)
                    .to(16,16,16)
                    .face(Direction.NORTH).uvs(0,0,16,16).texture("#0").end()
                    .face(Direction.EAST).uvs(0,0,16,16).texture("#1").end()
                    .face(Direction.SOUTH).uvs(0,0,16,16).texture("#1").end()
                    .face(Direction.WEST).uvs(0,0,16,16).texture("#1").end()
                    .face(Direction.UP).uvs(0,0,16,16).texture("#1").end()
                    .face(Direction.DOWN).uvs(0,0,16,16).texture("#1").end()
                    .end();*/
        }
    }

    /**
     * @see BlockModel
     * @see net.minecraft.block.Blocks#FURNACE
     */
    public static final class BlockModel extends BlockModelProvider {
        public BlockModel(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, RawOres.MODID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            // cube("ore_enricher", modLoc("block/enricher_sides"), modLoc("block/enricher_sides"), modLoc("block/enricher_face"), modLoc("block/enricher_sides"), modLoc("block/enricher_sides"), modLoc("block/enricher_sides"));
        }
    }
}

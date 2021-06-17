package com.khanhpham.client.datagen.models;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.khanhpham.OrePlusLT;
import com.khanhpham.registries.BlockRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @see net.minecraft.data.BlockModelProvider
 */
public class ModelProvider {

    public static final class Item extends net.minecraftforge.client.model.generators.ItemModelProvider {
        public Item(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, OrePlusLT.MODID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            //block("rich_iron_ore");
            ModelFile file = getExistingFile(mcLoc("item/generated"));
            build(file, "raw_iron");
            build(file, "enriching_element");
            build(file, "speed_upgrade_mk_1");
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
        private static final Logger LOGGER = LogManager.getLogger();
        private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        private final DataGenerator generator;
        public BlockState(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, OrePlusLT.MODID, existingFileHelper);
            this.generator = generator;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void run(DirectoryCache cache) {
            Path path = this.generator.getOutputFolder();
            Map<Block, IFinishedBlockState> map = Maps.newHashMap();
            Consumer<IFinishedBlockState> consumer = (p_240085_1_) -> {
                Block block = p_240085_1_.getBlock();
                IFinishedBlockState ifinishedblockstate = map.put(block, p_240085_1_);
                if (ifinishedblockstate != null) {
                    throw new IllegalStateException("Duplicate blockstate definition for " + block);
                }
            };
            Map<ResourceLocation, Supplier<JsonElement>> map1 = Maps.newHashMap();
            Set<net.minecraft.item.Item> set = Sets.newHashSet();
            BiConsumer<ResourceLocation, Supplier<JsonElement>> biconsumer = (p_240086_1_, p_240086_2_) -> {
                Supplier<JsonElement> supplier = map1.put(p_240086_1_, p_240086_2_);
                if (supplier != null) {
                    throw new IllegalStateException("Duplicate model definition for " + p_240086_1_);
                }
            };
            Consumer<net.minecraft.item.Item> consumer1 = set::add;
            (new net.minecraft.data.BlockModelProvider(consumer, biconsumer, consumer1)).run();
            (new BlockModels(consumer, biconsumer, consumer1)).run();
            List<Block> list = Registry.BLOCK.stream()
                    .filter((p_240084_1_) -> !map.containsKey(p_240084_1_)).collect(Collectors.toList());
            if (!list.isEmpty()) {
                throw new IllegalStateException("Missing blockstate definitions for: " + list);
            } else {
                Registry.BLOCK.forEach((p_240087_2_) -> {
                    net.minecraft.item.Item item = net.minecraft.item.Item.BY_BLOCK.get(p_240087_2_);
                    if (item != null) {
                        if (set.contains(item)) {
                            return;
                        }

                        ResourceLocation resourcelocation = ModelsResourceUtil.getModelLocation(item);
                        if (!map1.containsKey(resourcelocation)) {
                            map1.put(resourcelocation, new BlockModelWriter(ModelsResourceUtil.getModelLocation(p_240087_2_)));
                        }
                    }

                });
                saveCollection(cache, path, map, this::createBlockStatePath);
                saveCollection(cache, path, map1, this::createModelPath);
            }
        }

        private <T> void saveCollection(DirectoryCache p_240081_1_, Path p_240081_2_, Map<T, ? extends Supplier<JsonElement>> p_240081_3_, BiFunction<Path, T, Path> p_240081_4_) {
            p_240081_3_.forEach((p_240088_3_, p_240088_4_) -> {
                Path path = p_240081_4_.apply(p_240081_2_, p_240088_3_);

                try {
                    IDataProvider.save(GSON, p_240081_1_, p_240088_4_.get(), path);
                } catch (Exception exception) {
                    LOGGER.error("Couldn't save {}", path, exception);
                }

            });
        }

        private Path createBlockStatePath(Path p_240082_0_, Block p_240082_1_) {
            ResourceLocation resourcelocation = Registry.BLOCK.getKey(p_240082_1_);
            return p_240082_0_.resolve("assets/" + resourcelocation.getNamespace() + "/blockstates/" + resourcelocation.getPath() + ".json");
        }

        private Path createModelPath(Path p_240083_0_, ResourceLocation p_240083_1_) {
            return p_240083_0_.resolve("assets/" + p_240083_1_.getNamespace() + "/models/" + p_240083_1_.getPath() + ".json");
        }

        @Override
        protected void registerStatesAndModels() {}
    }

    private static final class BlockModels extends BlockModelProvider {
        private final Consumer<IFinishedBlockState> blockStateOutput;
        private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
        public BlockModels(Consumer<IFinishedBlockState> p_i232514_1_, BiConsumer<ResourceLocation, Supplier<JsonElement>> p_i232514_2_, Consumer<net.minecraft.item.Item> p_i232514_3_) {
            super(p_i232514_1_, p_i232514_2_, p_i232514_3_);

            blockStateOutput = p_i232514_1_;
            modelOutput = p_i232514_2_;
        }

        private static BlockStateVariantBuilder createBooleanModelDispatch(ResourceLocation p_239894_1_, ResourceLocation p_239894_2_) {
            return BlockStateVariantBuilder.property(BlockStateProperties.LIT).select(true, BlockModelDefinition.variant().with(BlockModelFields.MODEL, p_239894_1_)).select(false, BlockModelDefinition.variant().with(BlockModelFields.MODEL, p_239894_2_));
        }

        private static BlockStateVariantBuilder createHorizontalFacingDispatch() {
            return BlockStateVariantBuilder.property(BlockStateProperties.HORIZONTAL_FACING).select(Direction.EAST, BlockModelDefinition.variant().with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R90)).select(Direction.SOUTH, BlockModelDefinition.variant().with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R180)).select(Direction.WEST, BlockModelDefinition.variant().with(BlockModelFields.Y_ROT, BlockModelFields.Rotation.R270)).select(Direction.NORTH, BlockModelDefinition.variant());
        }

        private static FinishedVariantBlockState createSimpleBlock(Block p_239978_0_, ResourceLocation p_239978_1_) {
            return FinishedVariantBlockState.multiVariant(p_239978_0_, BlockModelDefinition.variant().with(BlockModelFields.MODEL, p_239978_1_));
        }

        private void createFurnace(Block p_239977_1_, TexturedModel.ISupplier p_239977_2_) {
            ResourceLocation resourcelocation = p_239977_2_.create(p_239977_1_, this.modelOutput);
            ResourceLocation frontOn = ModelTextures.getBlockTexture(p_239977_1_, "_front_on");
            ResourceLocation rl2 = p_239977_2_.get(p_239977_1_).updateTextures((p_239963_1_) -> {
                p_239963_1_.put(StockTextureAliases.FRONT, frontOn);
            }).createWithSuffix(p_239977_1_, "_on", this.modelOutput);
            this.blockStateOutput.accept(FinishedVariantBlockState.multiVariant(p_239977_1_).with(createBooleanModelDispatch(rl2, resourcelocation)).with(createHorizontalFacingDispatch()));
        }

        private void createTrivialCube(Block p_239975_1_) {
            this.createTrivialBlock(p_239975_1_);
        }

        private void createTrivialBlock(Block p_239956_1_) {
            this.blockStateOutput.accept(createSimpleBlock(p_239956_1_, TexturedModel.CUBE.create(p_239956_1_, this.modelOutput)));
        }

        @Override
        public void run() {
            createFurnace(BlockRegistries.ORE_ENRICHER.get(), TexturedModel.ORIENTABLE_ONLY_TOP);
            createFurnace(BlockRegistries.ORE_PROCESSOR.get(), TexturedModel.ORIENTABLE_ONLY_TOP);
            createTrivialCube(BlockRegistries.RICH_IRON_ORE.get());
            createTrivialCube(BlockRegistries.ELEMENT_ORE.get());
        }
    }
}

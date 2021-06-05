package com.khanhpham.common.machine.oreprocessor;

import com.khanhpham.registries.TileEntityRegistries;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class OreProcessor extends ContainerBlock {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty WORKING = BlockStateProperties.LIT;

    public OreProcessor() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
        );
    }

    @SuppressWarnings("deprecation")
    public void onRemove(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
        if (!p_196243_1_.is(p_196243_4_.getBlock())) {
            TileEntity tileentity = p_196243_2_.getBlockEntity(p_196243_3_);
            if (tileentity instanceof ProcessorTile) {
                InventoryHelper.dropContents(p_196243_2_, p_196243_3_, (ProcessorTile) tileentity);
                p_196243_2_.updateNeighbourForOutputSignal(p_196243_3_, this);
            }

            super.onRemove(p_196243_1_, p_196243_2_, p_196243_3_, p_196243_4_, p_196243_5_);
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.defaultBlockState().setValue(FACING, p_196258_1_.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        super.use(state, level, pos, playerEntity, hand, rayTraceResult);
        if (!level.isClientSide()) {
            TileEntity te = level.getBlockEntity(pos);
            if (te instanceof ProcessorTile) {
                NetworkHooks.openGui((ServerPlayerEntity) playerEntity, (INamedContainerProvider) te, pos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return TileEntityRegistries.PROCESSOR_TILE.get().create();
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        if (p_180655_1_.getValue(WORKING)) {
            double d0 = (double) p_180655_3_.getX() + 0.5D;
            double d1 = p_180655_3_.getY();
            double d2 = (double) p_180655_3_.getZ() + 0.5D;
            if (p_180655_4_.nextDouble() < 0.1D) {
                p_180655_2_.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = p_180655_1_.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = p_180655_4_.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
            double d6 = p_180655_4_.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
            p_180655_2_.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            p_180655_2_.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING, WORKING);
    }
}

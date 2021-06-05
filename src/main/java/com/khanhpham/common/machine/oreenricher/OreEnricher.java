package com.khanhpham.common.machine.oreenricher;

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
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

/**
 * @see AbstractFurnaceBlock
 */
public class OreEnricher extends ContainerBlock {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty WORKING = BlockStateProperties.LIT;

    public OreEnricher() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .strength(3)
                .noOcclusion()
                .requiresCorrectToolForDrops());
    }


    @SuppressWarnings("deprecation")
    public void onRemove(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
        if (!p_196243_1_.is(p_196243_4_.getBlock())) {
            TileEntity tileentity = p_196243_2_.getBlockEntity(p_196243_3_);
            if (tileentity instanceof EnricherTile) {
                InventoryHelper.dropContents(p_196243_2_, p_196243_3_, (EnricherTile) tileentity);
                p_196243_2_.updateNeighbourForOutputSignal(p_196243_3_, this);
            }

            super.onRemove(p_196243_1_, p_196243_2_, p_196243_3_, p_196243_4_, p_196243_5_);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        super.use(state, world, pos, player, hand, trace);
        if (!world.isClientSide()) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof EnricherTile) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, pos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return TileEntityRegistries.ENRICHER_TILE.get().create();
    }

    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.defaultBlockState().setValue(FACING, p_196258_1_.getHorizontalDirection().getOpposite());
    }

    //Try to copy / paste from vanilla code
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        if (p_180655_1_.getValue(WORKING)) {
            double d0 = (double)p_180655_3_.getX() + 0.5D;
            double d1 = p_180655_3_.getY();
            double d2 = (double)p_180655_3_.getZ() + 0.5D;
            Direction direction = p_180655_1_.getValue(FACING);
            Direction.Axis directionAxis = direction.getAxis();
            double d4 = p_180655_4_.nextDouble() * 0.6D - 0.3D;
            double d5 = directionAxis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : d4;
            double d6 = p_180655_4_.nextDouble() * 6.0D / 16.0D;
            double d7 = directionAxis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : d4;
            p_180655_2_.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            p_180655_2_.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(FACING, WORKING);
    }
}

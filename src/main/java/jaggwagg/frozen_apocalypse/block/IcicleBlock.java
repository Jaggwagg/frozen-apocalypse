package jaggwagg.frozen_apocalypse.block;

import jaggwagg.frozen_apocalypse.FrozenApocalypse;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Thickness;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class IcicleBlock extends Block implements LandingBlock, Waterloggable {
    public static final DirectionProperty VERTICAL_DIRECTION = Properties.VERTICAL_DIRECTION;
    public static final EnumProperty<Thickness> THICKNESS = Properties.THICKNESS;
    public static final IntProperty GROWTH = IntProperty.of("growth", 0, 1);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final VoxelShape TIP_MERGE_SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape UP_TIP_SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 11.0, 11.0);
    private static final VoxelShape DOWN_TIP_SHAPE = Block.createCuboidShape(5.0, 5.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    private static final VoxelShape FRUSTUM_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public IcicleBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(VERTICAL_DIRECTION, Direction.DOWN).with(THICKNESS, Thickness.TIP).with(GROWTH, 0).with(WATERLOGGED, false));
    }

    private static void spawnFallingBlock(BlockState state, ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutableCopy();

        for (BlockState blockState = state; isPointingDown(blockState); blockState = world.getBlockState(mutable)) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, mutable, blockState);

            if (isTip(blockState)) {
                int i = Math.max(1 + pos.getY() - mutable.getY(), 6);
                float f = (float) i;

                fallingBlockEntity.setHurtEntities(f, 40);

                break;
            }

            mutable.move(Direction.DOWN);
        }
    }

    public static void tryGrow(BlockState state, ServerWorld world, BlockPos pos) {
        BlockState aboveBlockState = world.getBlockState(pos.up());
        BlockState belowBlockState = world.getBlockState(pos.down());
        int blockStateGrowth = state.get(GROWTH);

        if (belowBlockState.isAir()) {
            if (!FrozenApocalypse.apocalypseLevel.canPlaceIcicles()) {
                return;
            }

            if (state.get(GROWTH) > 0) {
                return;
            }

            if (!state.get(THICKNESS).equals(Thickness.TIP)) {
                return;
            }

            if (aboveBlockState.isOf(ModBlocks.ICICLE.getBlock())) {
                place(world, pos, Thickness.FRUSTUM, blockStateGrowth);
            }

            place(world, pos.down(), Thickness.TIP, blockStateGrowth + 1);
        }
    }

    private static void place(WorldAccess world, BlockPos pos, Thickness thickness, int growth) {
        BlockState blockState = ModBlocks.ICICLE.getBlock().getDefaultState().with(VERTICAL_DIRECTION, Direction.DOWN).with(THICKNESS, thickness).with(GROWTH, growth).with(WATERLOGGED, world.getFluidState(pos).getFluid() == Fluids.WATER);
        world.setBlockState(pos, blockState, 3);
    }

    @Nullable
    private static Direction getDirectionToPlaceAt(WorldView world, BlockPos pos, Direction direction) {
        Direction placingDirection;

        if (canPlaceAtWithDirection(world, pos, direction)) {
            placingDirection = direction;
        } else {
            if (!canPlaceAtWithDirection(world, pos, direction.getOpposite())) {
                return null;
            }

            placingDirection = direction.getOpposite();
        }

        return placingDirection;
    }

    private static Thickness getThickness(WorldView world, BlockPos pos, Direction direction, boolean tryMerge) {
        Direction oppositeDirection = direction.getOpposite();
        BlockState blockState = world.getBlockState(pos.offset(direction));

        if (isIcicleFacingDirection(blockState, oppositeDirection)) {
            return !tryMerge && blockState.get(THICKNESS) != Thickness.TIP_MERGE ? Thickness.TIP : Thickness.TIP_MERGE;
        } else if (!isIcicleFacingDirection(blockState, direction)) {
            return Thickness.TIP;
        } else {
            Thickness thickness = blockState.get(THICKNESS);

            if (thickness != Thickness.TIP && thickness != Thickness.TIP_MERGE) {
                BlockState nextBlockState = world.getBlockState(pos.offset(oppositeDirection));

                return !isIcicleFacingDirection(nextBlockState, direction) ? Thickness.BASE : Thickness.MIDDLE;
            } else {
                return Thickness.FRUSTUM;
            }
        }
    }

    private static boolean canPlaceAtWithDirection(WorldView world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);

        return blockState.isSideSolidFullSquare(world, blockPos, direction) || isIcicleFacingDirection(blockState, direction);
    }

    private static boolean isTip(BlockState state) {
        if (!state.isOf(ModBlocks.ICICLE.getBlock())) {
            return false;
        } else {
            Thickness thickness = state.get(THICKNESS);

            return thickness == Thickness.TIP || thickness == Thickness.TIP_MERGE;
        }
    }

    private static boolean isPointingDown(BlockState state) {
        return isIcicleFacingDirection(state, Direction.DOWN);
    }

    private static boolean isPointingUp(BlockState state) {
        return isIcicleFacingDirection(state, Direction.UP);
    }

    private static boolean isHeldByIcicle(BlockState state, WorldView world, BlockPos pos) {
        return isPointingDown(state) && !world.getBlockState(pos.up()).isOf(ModBlocks.ICICLE.getBlock());
    }

    private static boolean isIcicleFacingDirection(BlockState state, Direction direction) {
        return state.isOf(ModBlocks.ICICLE.getBlock()) && state.get(VERTICAL_DIRECTION) == direction;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_DIRECTION, THICKNESS, GROWTH, WATERLOGGED);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) == 0) {
            if (world.getDimension().ultrawarm()) {
                world.removeBlock(pos, false);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return canPlaceAtWithDirection(world, pos, state.get(VERTICAL_DIRECTION));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        if (direction != Direction.UP && direction != Direction.DOWN) {
            return state;
        } else {
            Direction verticalDirection = state.get(VERTICAL_DIRECTION);

            if (verticalDirection == Direction.DOWN && world.getBlockTickScheduler().isQueued(pos, this)) {
                return state;
            } else if (direction == verticalDirection.getOpposite() && !this.canPlaceAt(state, world, pos)) {
                if (verticalDirection == Direction.DOWN) {
                    world.scheduleBlockTick(pos, this, 2);
                } else {
                    world.scheduleBlockTick(pos, this, 1);
                }

                return state;
            } else {
                boolean isMerge = state.get(THICKNESS) == Thickness.TIP_MERGE;
                Thickness thickness = getThickness(world, pos, verticalDirection, isMerge);

                return state.with(THICKNESS, thickness);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        BlockPos blockPos = hit.getBlockPos();
        if (!world.isClient && projectile.canModifyAt(world, blockPos) && projectile instanceof TridentEntity && projectile.getVelocity().length() > 0.6) {
            world.breakBlock(blockPos, true);
        }

    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (state.get(VERTICAL_DIRECTION) == Direction.UP && state.get(THICKNESS) == Thickness.TIP) {
            entity.handleFallDamage(fallDistance + 2.0F, 2.0F, world.getDamageSources().stalagmite());
        } else {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        }

    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (isPointingUp(state) && !this.canPlaceAt(state, world, pos)) {
            world.breakBlock(pos, true);
        } else {
            spawnFallingBlock(state, world, pos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(4) == 0 && isHeldByIcicle(state, world, pos)) {
            tryGrow(state, world, pos);
        }

    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        WorldAccess worldAccess = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction direction = ctx.getVerticalPlayerLookDirection().getOpposite();
        Direction placingDirection = getDirectionToPlaceAt(worldAccess, blockPos, direction);

        if (placingDirection == null) {
            return null;
        } else {
            boolean canMerge = !ctx.shouldCancelInteraction();
            Thickness thickness = getThickness(worldAccess, blockPos, placingDirection, canMerge);

            return thickness == null ? null : this.getDefaultState().with(VERTICAL_DIRECTION, placingDirection).with(THICKNESS, thickness).with(WATERLOGGED, worldAccess.getFluidState(blockPos).getFluid() == Fluids.WATER);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Thickness thickness = state.get(THICKNESS);
        VoxelShape voxelShape;

        if (thickness == Thickness.TIP_MERGE) {
            voxelShape = TIP_MERGE_SHAPE;
        } else if (thickness == Thickness.TIP) {
            if (state.get(VERTICAL_DIRECTION) == Direction.DOWN) {
                voxelShape = DOWN_TIP_SHAPE;
            } else {
                voxelShape = UP_TIP_SHAPE;
            }
        } else if (thickness == Thickness.FRUSTUM) {
            voxelShape = BASE_SHAPE;
        } else if (thickness == Thickness.MIDDLE) {
            voxelShape = FRUSTUM_SHAPE;
        } else {
            voxelShape = MIDDLE_SHAPE;
        }

        Vec3d modelOffset = state.getModelOffset(world, pos);

        return voxelShape.offset(modelOffset.x, 0.0, modelOffset.z);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    public float getMaxHorizontalModelOffset() {
        return 0.125F;
    }

    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
        if (!fallingBlockEntity.isSilent()) {
            world.syncWorldEvent(1045, pos, 0);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}

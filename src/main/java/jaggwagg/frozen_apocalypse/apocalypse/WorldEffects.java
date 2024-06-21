package jaggwagg.frozen_apocalypse.apocalypse;

import jaggwagg.frozen_apocalypse.FrozenApocalypse;
import jaggwagg.frozen_apocalypse.block.IcicleBlock;
import jaggwagg.frozen_apocalypse.block.ModBlocks;
import jaggwagg.frozen_apocalypse.config.ApocalypseLevel;
import jaggwagg.frozen_apocalypse.network.ModNetwork;
import jaggwagg.frozen_apocalypse.world.ModBooleanGameRules;
import jaggwagg.frozen_apocalypse.world.ModIntegerGameRules;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;

import java.util.function.BiConsumer;

public final class WorldEffects {
    private static int calculateDay(ServerWorld serverWorld) {
        return (int) Math.floor(serverWorld.getTimeOfDay() / 24000.0f);
    }

    public static boolean shouldSkipTick(ServerWorld serverWorld) {
        return serverWorld.isClient() || !FrozenApocalypse.CONFIG.isFrozenApocalypseEnabled() || AllEffects.isSafeDimension(serverWorld);
    }

    public static void initializeFrozenApocalypseLevel(ServerWorld serverWorld) {
        int configLevel = Math.max(0, serverWorld.getGameRules().getInt(ModIntegerGameRules.FROZEN_APOCALYPSE_LEVEL.getKey()));

        FrozenApocalypse.CONFIG.getApocalypseLevels().stream()
                .filter(level -> level.getApocalypseLevel() == configLevel)
                .findFirst()
                .ifPresent(level -> FrozenApocalypse.apocalypseLevel = level);
    }

    public static void updateFrozenApocalypseLevel(ServerWorld serverWorld) {
        if (!serverWorld.getGameRules().get(ModBooleanGameRules.FROZEN_APOCALYPSE_LEVEL_UPDATES_EACH_DAY.getKey()).get()) {
            return;
        }

        int currentDay = calculateDay(serverWorld);

        serverWorld.getGameRules().get(ModIntegerGameRules.FROZEN_APOCALYPSE_LEVEL.getKey()).set(
                FrozenApocalypse.CONFIG.getApocalypseLevels().stream()
                        .filter(level -> level.getStartingDay() <= currentDay)
                        .mapToInt(ApocalypseLevel::getApocalypseLevel)
                        .max()
                        .orElse(0),
                serverWorld.getServer()
        );
    }

    public static void sendFrozenApocalypseLevelToPlayers(ServerWorld serverWorld) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(FrozenApocalypse.apocalypseLevel.getApocalypseLevel());

        serverWorld.getPlayers().stream()
                .filter(player -> ServerPlayNetworking.canSend(player, ModNetwork.FROZEN_APOCALYPSE_LEVEL_ID))
                .forEach(player -> ServerPlayNetworking.send(player, ModNetwork.FROZEN_APOCALYPSE_LEVEL_ID, buf));
    }

    public static int calculateUpdateSpeed(ServerWorld serverWorld) {
        int speed = serverWorld.getGameRules().getInt(ModIntegerGameRules.FROZEN_APOCALYPSE_WORLD_UPDATE_SPEED.getKey());

        return (int) Math.ceil((Math.ceil(3.0 / speed * 512) / FrozenApocalypse.apocalypseLevel.getWorldUpdateSpeed()));
    }

    public static void applyApocalypseEffects(ServerWorld serverWorld, BlockPos blockPos) {
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canGrassTurnToFrostedGrass(),
                (currentServerWorld, currentBlockPos) -> placeGrassBlock(currentServerWorld, currentBlockPos, Blocks.GRASS_BLOCK, ModBlocks.FROSTED_GRASS_BLOCK.getBlock()));
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canWaterTurnToIce(), WorldEffects::placeIce);
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canPlaceIcicles(), WorldEffects::placeIcicle);
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canPlaceSnow(), WorldEffects::placeSnow);
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canFrostedGrassTurnToDeadGrass(),
                (currentServerWorld, currentBlockPos) -> placeGrassBlock(currentServerWorld, currentBlockPos, ModBlocks.FROSTED_GRASS_BLOCK.getBlock(), ModBlocks.DEAD_GRASS_BLOCK.getBlock()));
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canLeavesTurnToDeadLeaves(), WorldEffects::placeDeadLeaves);
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canIceTurnToPackedIce(), WorldEffects::placePackedIce);
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canLavaTurnToObsidian(), WorldEffects::placeObsidian);
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canPlaceSnowBlock(), WorldEffects::placeSnowBlock);
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canGrassTurnToPermafrost(), WorldEffects::placePermafrost);
        applyEffectIfEnabled(serverWorld, blockPos, FrozenApocalypse.apocalypseLevel.canLeavesDecay(), WorldEffects::doLeafDecay);
    }

    private static void applyEffectIfEnabled(ServerWorld serverWorld, BlockPos blockPos, boolean shouldApply, BiConsumer<ServerWorld, BlockPos> effect) {
        if (shouldApply) {
            effect.accept(serverWorld, blockPos);
        }
    }

    private static void placeBlock(ServerWorld serverWorld, BlockPos blockPos, BlockState blockState) {
        serverWorld.setBlockState(blockPos, blockState);
    }

    private static void placeGrassBlock(ServerWorld serverWorld, BlockPos blockPos, Block grassBlock, Block newGrassBlock) {
        BlockPos blockPosBelow = blockPos.down();
        BlockState blockState = serverWorld.getBlockState(blockPosBelow);

        if (blockState.isOf(grassBlock)) {
            if (serverWorld.getBlockState(blockPos).getBlock() instanceof PlantBlock) {
                placeBlock(serverWorld, blockPos, Blocks.AIR.getDefaultState());
            }

            Block blockToPlace = FrozenApocalypse.CONFIG.isPlacingCustomBlocksEnabled() ? newGrassBlock : Blocks.PODZOL;
            placeBlock(serverWorld, blockPosBelow, blockToPlace.getStateWithProperties(blockState));
        }
    }

    private static void placeIce(ServerWorld serverWorld, BlockPos blockPos) {
        BlockPos blockPosBelow = blockPos.down();
        BlockState blockStateBelow = serverWorld.getBlockState(blockPosBelow);

        if (blockStateBelow.getFluidState().isOf(Fluids.WATER) || (blockStateBelow.getProperties().contains(Properties.WATERLOGGED) && blockStateBelow.get(Properties.WATERLOGGED))) {
            serverWorld.setBlockState(blockPosBelow, Blocks.ICE.getDefaultState());
        }
    }

    private static void placeIcicle(ServerWorld serverWorld, BlockPos blockPos) {
        BlockState blockState = serverWorld.getBlockState(blockPos);
        BlockState blockStateDown = serverWorld.getBlockState(blockPos.down());

        if (!FrozenApocalypse.CONFIG.isPlacingCustomBlocksEnabled()) {
            return;
        }

        if (!(serverWorld.isRaining() || serverWorld.isThundering())) {
            return;
        }

        if (blockStateDown.isOf(Blocks.AIR) || blockState.isOf(Blocks.SNOW) || blockStateDown.isOf(Blocks.SNOW_BLOCK)) {
            return;
        }

        for (int y = 0; y < 5; y++) {
            BlockPos blockPosBelow = blockPos.down(y);
            Block icicleBlock = ModBlocks.ICICLE.getBlock();

            if (serverWorld.getBlockState(blockPosBelow).isOf(icicleBlock)) {
                break;
            }

            if (serverWorld.getLightLevel(LightType.BLOCK, blockPosBelow) > FrozenApocalypse.apocalypseLevel.getMinimumHeatSourceDistance()) {
                break;
            }

            if (serverWorld.getBlockState(blockPosBelow).isAir() && serverWorld.getRandom().nextInt(4) == 0) {
                if (icicleBlock.getDefaultState().canPlaceAt(serverWorld, blockPosBelow)) {
                    placeBlock(serverWorld, blockPosBelow, icicleBlock.getDefaultState().with(IcicleBlock.VERTICAL_DIRECTION, Direction.DOWN).with(IcicleBlock.GROWTH, serverWorld.getRandom().nextInt(2)));
                    break;
                }
            }
        }
    }

    private static void placeSnow(ServerWorld serverWorld, BlockPos blockPos) {
        BlockState blockState = serverWorld.getBlockState(blockPos);
        BlockState belowBlockState = serverWorld.getBlockState(blockPos.down());

        if (!(serverWorld.isRaining() || serverWorld.isThundering())) {
            return;
        }

        if (!Blocks.SNOW.getDefaultState().canPlaceAt(serverWorld, blockPos)) {
            return;
        }

        if (blockState.isOf(Blocks.SNOW) || blockState.isOf(Blocks.POWDER_SNOW) || belowBlockState.isOf(Blocks.ICE) || belowBlockState.isOf(Blocks.PACKED_ICE)) {
            return;
        }

        placeBlock(serverWorld, blockPos, Blocks.SNOW.getDefaultState());
    }

    private static void placeDeadLeaves(ServerWorld serverWorld, BlockPos blockPos) {
        Block blockBelow = serverWorld.getBlockState(blockPos.down()).getBlock();

        if (blockBelow instanceof LeavesBlock || blockBelow instanceof VineBlock) {
            applyEffectToArea(serverWorld, blockPos, 1, 3, 1, (world, pos) -> {
                if (world.getBlockState(pos).getBlock() instanceof LeavesBlock) {
                    if (!world.getBlockState(pos).isOf(ModBlocks.DEAD_LEAVES.getBlock())) {
                        BlockState blockState = world.getBlockState(pos);

                        world.setBlockState(pos, ModBlocks.DEAD_LEAVES.getBlock().getStateWithProperties(blockState));
                    }
                }
            });
        }
    }

    private static void placePackedIce(ServerWorld serverWorld, BlockPos blockPos) {
        BlockState blockStateBelow = serverWorld.getBlockState(blockPos.down());

        if (blockStateBelow.getFluidState().isOf(Fluids.WATER) || blockStateBelow.isOf(Blocks.ICE) || (blockStateBelow.getProperties().contains(Properties.WATERLOGGED) && blockStateBelow.get(Properties.WATERLOGGED))) {
            placeBlock(serverWorld, blockPos.down(), Blocks.PACKED_ICE.getDefaultState());
        }
    }

    private static void placeObsidian(ServerWorld serverWorld, BlockPos blockPos) {
        BlockState blockStateBelow = serverWorld.getBlockState(blockPos.down());

        if (blockStateBelow.getFluidState().isOf(Fluids.LAVA)) {
            placeBlock(serverWorld, blockPos.down(), Blocks.OBSIDIAN.getDefaultState());
            serverWorld.syncWorldEvent(1501, blockPos.down(), 0);
        }
    }

    private static void placeSnowBlock(ServerWorld serverWorld, BlockPos blockPos) {
        BlockState blockState = serverWorld.getBlockState(blockPos);
        BlockState blockStateDown = serverWorld.getBlockState(blockPos.down());

        if (!(serverWorld.isRaining() || serverWorld.isThundering())) {
            return;
        }

        if (blockStateDown.contains(LeavesBlock.PERSISTENT)) {
            return;
        }

        if (!Blocks.SNOW_BLOCK.getDefaultState().canPlaceAt(serverWorld, blockPos)) {
            return;
        }

        if (blockState.isOf(Blocks.POWDER_SNOW) || blockStateDown.isOf(Blocks.AIR) || blockStateDown.isOf(Blocks.SNOW_BLOCK)) {
            return;
        }

        if (blockStateDown.getBlock() instanceof FluidBlock) {
            return;
        }

        placeBlock(serverWorld, blockPos, Blocks.SNOW_BLOCK.getDefaultState());
    }

    private static void placePermafrost(ServerWorld serverWorld, BlockPos blockPos) {
        BlockState blockState = serverWorld.getBlockState(blockPos.down());

        if (blockState.getBlock() instanceof GrassBlock || blockState.isOf(Blocks.DIRT) || blockState.isOf(Blocks.PODZOL)) {
            if (FrozenApocalypse.CONFIG.isPlacingCustomBlocksEnabled()) {
                placeBlock(serverWorld, blockPos.down(), ModBlocks.PERMAFROST.getBlock().getDefaultState());
            } else {
                placeBlock(serverWorld, blockPos.down(), Blocks.PODZOL.getDefaultState());
            }
        }
    }

    private static void doLeafDecay(ServerWorld serverWorld, BlockPos blockPos) {
        Block blockBelow = serverWorld.getBlockState(blockPos.down()).getBlock();
        if (blockBelow instanceof LeavesBlock || blockBelow instanceof VineBlock) {
            applyEffectToArea(serverWorld, blockPos, 1, 3, 1, (world, pos) -> {
                if (world.getBlockState(pos).getBlock() instanceof LeavesBlock) {
                    world.removeBlock(pos, true);
                }
            });
        }
    }

    private static void applyEffectToArea(ServerWorld serverWorld, BlockPos centerPos, int xRange, int yRange, int zRange, BiConsumer<ServerWorld, BlockPos> effect) {
        for (int x = -xRange; x <= xRange; x++) {
            for (int z = -zRange; z <= zRange; z++) {
                for (int y = -yRange; y <= 1; y++) {
                    BlockPos targetPos = centerPos.add(x, y, z);
                    effect.accept(serverWorld, targetPos);
                }
            }
        }
    }
}

package jaggwagg.frozen_apocalypse.apocalypse;

import jaggwagg.frozen_apocalypse.FrozenApocalypse;
import jaggwagg.frozen_apocalypse.config.MinecraftIdentifier;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.WorldAccess;

public class AllEffects {
    public static boolean isSafeDimension(WorldAccess worldAccess) {
        if (worldAccess.getServer() != null) {
            for (MinecraftIdentifier affectedDimension : FrozenApocalypse.CONFIG.getAffectedDimensions()) {
                ServerWorld serverWorld = worldAccess.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, new Identifier(affectedDimension.getId())));

                if (serverWorld != null) {
                    if (serverWorld.getDimension().equals(worldAccess.getDimension())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}

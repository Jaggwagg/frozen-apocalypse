package jaggwagg.frozen_apocalypse;

import jaggwagg.frozen_apocalypse.network.FrozenApocalypseClientRecievers;
import net.fabricmc.api.ClientModInitializer;

public class FrozenApocalypseClient implements ClientModInitializer {
    public static int frozenApocalypseLevelClient;

    @Override
    public void onInitializeClient() {
        FrozenApocalypseClientRecievers.init();

        FrozenApocalypse.LOGGER.info(FrozenApocalypse.MOD_ID + ": Initialized client successfully");
    }
}
package fuzs.miniumstone.fabric.client;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.client.MiniumStoneClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class MiniumStoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(MiniumStone.MOD_ID, MiniumStoneClient::new);
    }
}

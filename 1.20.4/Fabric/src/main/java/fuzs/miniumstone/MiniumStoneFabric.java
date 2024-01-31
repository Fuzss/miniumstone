package fuzs.miniumstone;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class MiniumStoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(MiniumStone.MOD_ID, MiniumStone::new);
    }
}

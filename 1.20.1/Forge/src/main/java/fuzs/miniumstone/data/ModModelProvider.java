package fuzs.miniumstone.data;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractModelProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void registerStatesAndModels() {
        this.basicItem(ModRegistry.MINIUM_SHARD_ITEM.get());
        this.basicItem(ModRegistry.MINIUM_STONE_ITEM.get());
    }
}

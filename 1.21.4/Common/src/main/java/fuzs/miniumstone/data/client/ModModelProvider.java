package fuzs.miniumstone.data.client;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ModRegistry.MINIUM_SHARD_ITEM.value(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModRegistry.MINIUM_STONE_ITEM.value(), ModelTemplates.FLAT_ITEM);
    }
}

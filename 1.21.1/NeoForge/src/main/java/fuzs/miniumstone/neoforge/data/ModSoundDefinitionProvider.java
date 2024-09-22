package fuzs.miniumstone.neoforge.data;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.neoforge.api.data.v2.client.AbstractSoundDefinitionProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.ForgeDataProviderContext;

public class ModSoundDefinitionProvider extends AbstractSoundDefinitionProvider {

    public ModSoundDefinitionProvider(ForgeDataProviderContext context) {
        super(context);
    }

    @Override
    public void addSoundDefinitions() {
        this.add(ModRegistry.ITEM_MINIUM_STONE_CHARGE_SOUND_EVENT.value(), sound(this.id("item/miniumstone/charge")).volume(0.5F));
        this.add(ModRegistry.ITEM_MINIUM_STONE_UNCHARGE_SOUND_EVENT.value(), sound(this.id("item/miniumstone/uncharge")).volume(0.5F));
        this.add(ModRegistry.ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT.value(), sound(this.id("item/miniumstone/transmute")).volume(0.5F));
    }
}

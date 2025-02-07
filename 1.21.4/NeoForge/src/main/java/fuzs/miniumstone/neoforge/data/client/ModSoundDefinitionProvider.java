package fuzs.miniumstone.neoforge.data.client;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.neoforge.api.client.data.v2.AbstractSoundProvider;

public class ModSoundDefinitionProvider extends AbstractSoundProvider {

    public ModSoundDefinitionProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addSounds() {
        this.add(ModRegistry.ITEM_MINIUM_STONE_CHARGE_SOUND_EVENT.value(),
                sound(this.id("item/miniumstone/charge")).volume(0.5F));
        this.add(ModRegistry.ITEM_MINIUM_STONE_UNCHARGE_SOUND_EVENT.value(),
                sound(this.id("item/miniumstone/uncharge")).volume(0.5F));
        this.add(ModRegistry.ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT.value(),
                sound(this.id("item/miniumstone/transmute")).volume(0.5F));
    }
}

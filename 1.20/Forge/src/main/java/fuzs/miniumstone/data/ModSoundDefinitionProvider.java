package fuzs.miniumstone.data;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractSoundDefinitionProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModSoundDefinitionProvider extends AbstractSoundDefinitionProvider {

    public ModSoundDefinitionProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    public void registerSounds() {
        this.add(ModRegistry.ITEM_MINIUM_STONE_CHARGE_SOUND_EVENT.get(), sound(this.id("item/miniumstone/charge")).volume(0.5F));
        this.add(ModRegistry.ITEM_MINIUM_STONE_UNCHARGE_SOUND_EVENT.get(), sound(this.id("item/miniumstone/uncharge")).volume(0.5F));
        this.add(ModRegistry.ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT.get(), sound(this.id("item/miniumstone/transmute")).volume(0.5F));
    }
}

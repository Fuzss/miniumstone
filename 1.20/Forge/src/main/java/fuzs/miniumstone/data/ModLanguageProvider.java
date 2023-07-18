package fuzs.miniumstone.data;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.client.handler.StoneChargeHandler;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.MINIUM_SHARD_ITEM.get(), "Minium Shard");
        this.add(ModRegistry.MINIUM_STONE_ITEM.get(), "Minium Stone");
        this.addAdditional(ModRegistry.MINIUM_STONE_ITEM.get(), "description", "Press [%s] to open crafting grid");
        this.add("key.categories." + MiniumStone.MOD_ID, MiniumStone.MOD_NAME);
        this.add(StoneChargeHandler.CHARGE_MINIUM_STONE_KEY_MAPPING, "Charge Minium Stone");
        this.add(StoneChargeHandler.OPEN_CRAFTING_GRID_KEY_MAPPING, "Open Crafting Grid");
    }
}

package fuzs.miniumstone.data;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.client.handler.MiniumStoneKeyHandler;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.MiniumStoneItem;
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
        this.addAdditional(ModRegistry.MINIUM_STONE_ITEM.get(), "selection", "Selection Mode: %s");
        this.addAdditional(ModRegistry.MINIUM_STONE_ITEM.get(), "changedSelection", "Changed Selection Mode to %s");
        this.addAdditional(ModRegistry.MINIUM_STONE_ITEM.get(), "changeSelection", "Press [%s] + [%s] to cycle mode");
        this.add(MiniumStoneItem.SelectionMode.CUBE.translationKey, "Cube");
        this.add(MiniumStoneItem.SelectionMode.FLAT.translationKey, "Flat");
        this.add(MiniumStoneItem.SelectionMode.LINE.translationKey, "Line");
        this.addAdditional(ModRegistry.MINIUM_STONE_ITEM.get(), "charge", "Press [%s] to charge up");
        this.addAdditional(ModRegistry.MINIUM_STONE_ITEM.get(), "crafting", "Press [%s] to open crafting grid");
        this.add("key.categories." + MiniumStone.MOD_ID, MiniumStone.MOD_NAME);
        this.add(MiniumStoneKeyHandler.CHARGE_MINIUM_STONE_KEY_MAPPING, "Charge Minium Stone");
        this.add(MiniumStoneKeyHandler.OPEN_CRAFTING_GRID_KEY_MAPPING, "Open Crafting Grid");
        this.add(ModRegistry.ITEM_MINIUM_STONE_CHARGE_SOUND_EVENT.get(), "Minium Stone charges");
        this.add(ModRegistry.ITEM_MINIUM_STONE_UNCHARGE_SOUND_EVENT.get(), "Minium Stone uncharges");
        this.add(ModRegistry.ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT.get(), "Minium Stone transmutes");
    }
}

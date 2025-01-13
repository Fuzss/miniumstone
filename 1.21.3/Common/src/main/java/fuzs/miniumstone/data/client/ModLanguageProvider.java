package fuzs.miniumstone.data.client;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.client.handler.MiniumStoneKeyHandler;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ModRegistry.MINIUM_SHARD_ITEM.value(), "Minium Shard");
        builder.add(ModRegistry.MINIUM_STONE_ITEM.value(), "Minium Stone");
        builder.add(ModRegistry.MINIUM_STONE_ITEM.value(), "selection", "Selection Mode: %s");
        builder.add(ModRegistry.MINIUM_STONE_ITEM.value(), "changedSelection", "Changed Selection Mode to %s");
        builder.add(ModRegistry.MINIUM_STONE_ITEM.value(), "changeSelection", "Press %s + %s to cycle mode");
        builder.add(ModRegistry.MINIUM_STONE_ITEM.value(), "more", "Hold %s for more information");
        builder.add(ModRegistry.MINIUM_STONE_ITEM.value(), "shift", "Shift");
        builder.add(MiniumStoneItem.SelectionMode.CUBE.getComponent(), "Cube");
        builder.add(MiniumStoneItem.SelectionMode.FLAT.getComponent(), "Flat");
        builder.add(MiniumStoneItem.SelectionMode.LINE.getComponent(), "Line");
        builder.add(ModRegistry.MINIUM_STONE_ITEM.value(), "charge", "Press %s to charge");
        builder.add(ModRegistry.MINIUM_STONE_ITEM.value(), "crafting", "Press %s to open crafting grid");
        builder.addKeyCategory(MiniumStone.MOD_ID, MiniumStone.MOD_NAME);
        builder.add(MiniumStoneKeyHandler.CHARGE_MINIUM_STONE_KEY_MAPPING, "Charge Minium Stone");
        builder.add(MiniumStoneKeyHandler.OPEN_CRAFTING_GRID_KEY_MAPPING, "Open Crafting Grid");
        builder.add(ModRegistry.ITEM_MINIUM_STONE_CHARGE_SOUND_EVENT.value(), "Minium Stone charges");
        builder.add(ModRegistry.ITEM_MINIUM_STONE_UNCHARGE_SOUND_EVENT.value(), "Minium Stone uncharges");
        builder.add(ModRegistry.ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT.value(), "Minium Stone transmutes");
        builder.add(TransmutationInWorldRecipe.TRANSMUTATION_IN_WORLD_COMPONENT, "Block Transmutation");
    }
}

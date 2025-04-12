package fuzs.miniumstone.data;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ModItemTagProvider extends AbstractTagProvider<Item> {

    public ModItemTagProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(ModRegistry.MINIUM_STONE_ITEM);
        this.tag(ModRegistry.RECIPES_IGNORE_COMPONENTS_ITEM_TAG).add(ModRegistry.MINIUM_STONE_ITEM);
        this.tag(ModRegistry.RECIPES_DO_NOT_CONSUME_ITEM_TAG).add(ModRegistry.MINIUM_STONE_ITEM);
    }
}

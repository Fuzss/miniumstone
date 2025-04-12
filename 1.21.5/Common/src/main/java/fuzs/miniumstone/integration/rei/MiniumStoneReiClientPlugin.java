package fuzs.miniumstone.integration.rei;

import fuzs.miniumstone.init.ModRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class MiniumStoneReiClientPlugin implements REIClientPlugin {
    static final CategoryIdentifier<?> CRAFTING_CATEGORY = CategoryIdentifier.of("minecraft",
            "plugins/crafting");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new TransmutationInWorldCategory());
        registry.addWorkstations(CRAFTING_CATEGORY, EntryStacks.of(ModRegistry.MINIUM_STONE_ITEM.value()));
    }
}

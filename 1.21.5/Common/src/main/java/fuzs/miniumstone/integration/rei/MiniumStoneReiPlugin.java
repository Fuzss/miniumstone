package fuzs.miniumstone.integration.rei;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;

public class MiniumStoneReiPlugin implements REICommonPlugin {

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(TransmutationInWorldRecipe.class)
                .filterType(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value())
                .fill(TransmutationInWorldDisplay::new);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(MiniumStone.id("default/transmutation_in_world"), TransmutationInWorldDisplay.SERIALIZER);
    }
}

package fuzs.miniumstone.neoforge.client;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.client.MiniumStoneClient;
import fuzs.miniumstone.data.client.ModLanguageProvider;
import fuzs.miniumstone.data.client.ModModelProvider;
import fuzs.miniumstone.neoforge.data.client.ModSoundDefinitionProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = MiniumStone.MOD_ID, dist = Dist.CLIENT)
public class MiniumStoneNeoForgeClient {

    public MiniumStoneNeoForgeClient() {
        ClientModConstructor.construct(MiniumStone.MOD_ID, MiniumStoneClient::new);
        DataProviderHelper.registerDataProviders(MiniumStone.MOD_ID, ModLanguageProvider::new, ModModelProvider::new,
                ModSoundDefinitionProvider::new
        );
    }

//    private static void registerLoadingHandlers(IEventBus eventBus) {
//        eventBus.addListener((final RegisterRecipeBookCategoriesEvent evt) -> {
//            evt.registerRecipeCategoryFinder(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value(), $ -> {
//                String internalName = MiniumStone.id("transmutation").toDebugFileName().toUpperCase(Locale.ROOT);
//                return RecipeBookCategories.create(internalName);
//            });
//        });
//    }
}

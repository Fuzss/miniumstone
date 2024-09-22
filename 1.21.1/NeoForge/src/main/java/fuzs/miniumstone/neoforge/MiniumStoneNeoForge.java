package fuzs.miniumstone.neoforge;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.data.ModEntityInjectLootProvider;
import fuzs.miniumstone.data.client.ModLanguageProvider;
import fuzs.miniumstone.data.client.ModModelProvider;
import fuzs.miniumstone.data.recipes.ModRecipeProvider;
import fuzs.miniumstone.neoforge.data.ModSoundDefinitionProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(MiniumStone.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MiniumStoneNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(MiniumStone.MOD_ID, MiniumStone::new);
        DataProviderHelper.registerDataProviders(MiniumStone.MOD_ID,
                ModEntityInjectLootProvider::new,
                ModLanguageProvider::new,
                ModModelProvider::new,
                ModRecipeProvider::new,
                ModSoundDefinitionProvider::new
        );
    }
}

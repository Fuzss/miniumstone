package fuzs.miniumstone.neoforge;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.data.ModEntityInjectLootProvider;
import fuzs.miniumstone.data.ModItemTagProvider;
import fuzs.miniumstone.data.recipes.ModRecipeProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(MiniumStone.MOD_ID)
public class MiniumStoneNeoForge {

    public MiniumStoneNeoForge() {
        ModConstructor.construct(MiniumStone.MOD_ID, MiniumStone::new);
        DataProviderHelper.registerDataProviders(MiniumStone.MOD_ID, ModEntityInjectLootProvider::new,
                ModRecipeProvider::new, ModItemTagProvider::new
        );
    }
}

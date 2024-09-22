package fuzs.miniumstone.neoforge.client;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.client.MiniumStoneClient;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraft.client.RecipeBookCategories;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = MiniumStone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MiniumStoneNeoForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(MiniumStone.MOD_ID, MiniumStoneClient::new);
    }

    @SubscribeEvent
    public static void onRegisterRecipeBookCategories(final RegisterRecipeBookCategoriesEvent evt) {
        evt.registerRecipeCategoryFinder(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value(), $ -> {
            String internalName = MiniumStone.id("transmutation").toDebugFileName().toUpperCase(Locale.ROOT);
            return RecipeBookCategories.create(internalName);
        });
    }
}

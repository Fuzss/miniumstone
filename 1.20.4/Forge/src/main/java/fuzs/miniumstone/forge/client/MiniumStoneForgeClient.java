package fuzs.miniumstone.forge.client;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.client.MiniumStoneClient;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraft.client.RecipeBookCategories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = MiniumStone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MiniumStoneForgeClient {

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

package fuzs.miniumstone;

import fuzs.miniumstone.data.ModEntityInjectLootProvider;
import fuzs.miniumstone.data.ModLanguageProvider;
import fuzs.miniumstone.data.ModModelProvider;
import fuzs.miniumstone.data.ModRecipeProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(MiniumStone.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MiniumStoneForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(MiniumStone.MOD_ID, MiniumStone::new);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        final DataGenerator dataGenerator = evt.getGenerator();
        boolean includeClient = evt.includeClient();
        dataGenerator.addProvider(includeClient, new ModEntityInjectLootProvider(evt, MiniumStone.MOD_ID));
        dataGenerator.addProvider(includeClient, new ModLanguageProvider(evt, MiniumStone.MOD_ID));
        dataGenerator.addProvider(includeClient, new ModModelProvider(evt, MiniumStone.MOD_ID));
        dataGenerator.addProvider(includeClient, new ModRecipeProvider(evt, MiniumStone.MOD_ID));
    }
}

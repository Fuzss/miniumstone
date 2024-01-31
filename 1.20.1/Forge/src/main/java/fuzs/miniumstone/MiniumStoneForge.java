package fuzs.miniumstone;

import fuzs.miniumstone.data.*;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
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
        evt.getGenerator().addProvider(true, new ModEntityInjectLootProvider(evt, MiniumStone.MOD_ID));
        evt.getGenerator().addProvider(true, new ModLanguageProvider(evt, MiniumStone.MOD_ID));
        evt.getGenerator().addProvider(true, new ModModelProvider(evt, MiniumStone.MOD_ID));
        evt.getGenerator().addProvider(true, new ModRecipeProvider(evt, MiniumStone.MOD_ID));
        evt.getGenerator().addProvider(true, new ModSoundDefinitionProvider(evt, MiniumStone.MOD_ID));
    }
}

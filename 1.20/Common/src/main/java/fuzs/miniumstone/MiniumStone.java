package fuzs.miniumstone;

import fuzs.miniumstone.config.CommonConfig;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.networking.client.ServerboundChargeStoneMessage;
import fuzs.miniumstone.networking.client.ServerboundOpenCraftingGridMessage;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.event.v1.server.LootTableLoadEvents;
import fuzs.puzzleslib.api.network.v3.NetworkHandlerV3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.IntPredicate;

public class MiniumStone implements ModConstructor {
    public static final String MOD_ID = "miniumstone";
    public static final String MOD_NAME = "Minium Stone";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandlerV3 NETWORK = NetworkHandlerV3.builder(MOD_ID).registerServerbound(ServerboundChargeStoneMessage.class).registerServerbound(ServerboundOpenCraftingGridMessage.class);
    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).common(CommonConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        LootTableLoadEvents.MODIFY.register((LootDataManager lootManager, ResourceLocation identifier, Consumer<LootPool> addPool, IntPredicate removePool) -> {
            if (CONFIG.get(CommonConfig.class).miniumShardDropMonsters.contains(identifier)) {
                addPool.accept(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(ModRegistry.MINIUM_SHARD_INJECT_LOOT_TABLE)).build());
            }
        });
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}

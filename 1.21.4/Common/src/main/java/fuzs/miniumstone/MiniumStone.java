package fuzs.miniumstone;

import fuzs.miniumstone.config.CommonConfig;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.network.ClientboundTransmutationInWorldRecipesMessage;
import fuzs.miniumstone.network.ClientboundTransmutationParticleMessage;
import fuzs.miniumstone.network.client.ServerboundChargeStoneMessage;
import fuzs.miniumstone.network.client.ServerboundOpenCraftingGridMessage;
import fuzs.miniumstone.network.client.ServerboundStoneTransmutationMessage;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.BuildCreativeModeTabContentsCallback;
import fuzs.puzzleslib.api.event.v1.server.LootTableLoadCallback;
import fuzs.puzzleslib.api.event.v1.server.SyncDataPackContentsCallback;
import fuzs.puzzleslib.api.network.v3.NetworkHandler;
import fuzs.puzzleslib.api.network.v3.PlayerSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiniumStone implements ModConstructor {
    public static final String MOD_ID = "miniumstone";
    public static final String MOD_NAME = "Minium Stone";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = NetworkHandler.builder(MOD_ID)
            .registerServerbound(ServerboundChargeStoneMessage.class)
            .registerServerbound(ServerboundOpenCraftingGridMessage.class)
            .registerServerbound(ServerboundStoneTransmutationMessage.class)
            .registerClientbound(ClientboundTransmutationParticleMessage.class)
            .registerSerializer(ClientboundTransmutationInWorldRecipesMessage.class,
                    ClientboundTransmutationInWorldRecipesMessage.STREAM_CODEC)
            .registerClientbound(ClientboundTransmutationInWorldRecipesMessage.class);
    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).common(CommonConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        LootTableLoadCallback.EVENT.register((ResourceLocation resourceLocation, LootTable.Builder builder, HolderLookup.Provider provider) -> {
            if (CONFIG.get(CommonConfig.class).miniumShardDropMonsters.contains(resourceLocation)) {
                builder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(NestedLootTable.lootTableReference(ModRegistry.MINIUM_SHARD_INJECT_LOOT_TABLE)));
            }
        });
        SyncDataPackContentsCallback.EVENT.register((ServerPlayer serverPlayer, boolean joined) -> {
            NETWORK.sendMessage(PlayerSet.ofPlayer(serverPlayer),
                    new ClientboundTransmutationInWorldRecipesMessage(ClientboundTransmutationInWorldRecipesMessage.transmutationInWorldRecipes));
        });
        BuildCreativeModeTabContentsCallback.buildCreativeModeTabContents(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((CreativeModeTab creativeModeTab, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) -> {
                    output.accept(ModRegistry.MINIUM_STONE_ITEM.value());
                });
        BuildCreativeModeTabContentsCallback.buildCreativeModeTabContents(CreativeModeTabs.INGREDIENTS)
                .register((CreativeModeTab creativeModeTab, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) -> {
                    output.accept(ModRegistry.MINIUM_SHARD_ITEM.value());
                });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}

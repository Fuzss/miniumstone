package fuzs.miniumstone.config;

import com.google.common.collect.ImmutableSet;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import fuzs.puzzleslib.api.config.v3.serialization.KeyedValueProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CommonConfig implements ConfigCore {
    @Config(
            name = "minium_shard_drop_monsters", description = {
            "Monster types that have a rare chance to drop a minium shard for crafting a minium stone.",
            "Drop chances are similar to wither skeleton skulls from wither skeletons."
    }
    )
    List<String> miniumShardDropMonstersRaw = KeyedValueProvider.toString(Registries.ENTITY_TYPE,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.DROWNED,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.HUSK,
            EntityType.SKELETON,
            EntityType.WITHER_SKELETON,
            EntityType.STRAY,
            EntityType.CREEPER,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.ENDERMAN,
            EntityType.WITCH);

    public Set<ResourceLocation> miniumShardDropMonsters;

    @Override
    public void afterConfigReload() {
        this.miniumShardDropMonsters = ConfigDataSet.from(Registries.ENTITY_TYPE, this.miniumShardDropMonstersRaw)
                .stream()
                .map(EntityType::getDefaultLootTable)
                .mapMulti(Optional<ResourceKey<LootTable>>::ifPresent)
                .map(ResourceKey::location)
                .collect(ImmutableSet.toImmutableSet());
    }
}

package fuzs.miniumstone.data;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractLootProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModEntityInjectLootProvider extends AbstractLootProvider.Simple {

    public ModEntityInjectLootProvider(DataProviderContext context) {
        super(LootContextParamSets.ENTITY, context);
    }

    @Override
    public void addLootTables() {
        this.add(ModRegistry.MINIUM_SHARD_INJECT_LOOT_TABLE,
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModRegistry.MINIUM_SHARD_ITEM.value()))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                                this.registries(), 0.025F, 0.01F)))
        );
    }
}

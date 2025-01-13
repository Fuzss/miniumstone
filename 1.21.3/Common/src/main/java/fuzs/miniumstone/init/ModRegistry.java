package fuzs.miniumstone.init;

import com.mojang.serialization.Codec;
import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.miniumstone.world.item.crafting.TransmutationCraftingRecipe;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.miniumstone.world.item.crafting.TransmutationSmeltingRecipe;
import fuzs.miniumstone.world.item.crafting.display.BlockSlotDisplay;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(MiniumStone.MOD_ID);
    public static final Holder.Reference<DataComponentType<Byte>> CHARGE_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "charge",
            builder -> builder.persistent(Codec.BYTE).networkSynchronized(ByteBufCodecs.BYTE));
    public static final Holder.Reference<DataComponentType<MiniumStoneItem.SelectionMode>> SELECTION_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "selection",
            builder -> builder.persistent(MiniumStoneItem.SelectionMode.CODEC)
                    .networkSynchronized(MiniumStoneItem.SelectionMode.STREAM_CODEC));
    public static final Holder.Reference<Item> MINIUM_SHARD_ITEM = REGISTRIES.registerItem("minium_shard");
    public static final Holder.Reference<Item> MINIUM_STONE_ITEM = REGISTRIES.registerItem("minium_stone",
            MiniumStoneItem::new,
            () -> new Item.Properties().durability(1521)
                    .rarity(Rarity.UNCOMMON)
                    .component(CHARGE_DATA_COMPONENT_TYPE.value(), (byte) 0)
                    .component(SELECTION_DATA_COMPONENT_TYPE.value(), MiniumStoneItem.SelectionMode.FLAT));
    public static final Holder.Reference<RecipeType<TransmutationInWorldRecipe>> TRANSMUTATION_IN_WORLD_RECIPE_TYPE = REGISTRIES.registerRecipeType(
            "transmutation_in_world");
    public static final Holder.Reference<RecipeSerializer<TransmutationInWorldRecipe>> TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER = REGISTRIES.register(
            Registries.RECIPE_SERIALIZER,
            "transmutation_in_world",
            TransmutationInWorldRecipe.Serializer::new);
    public static final Holder.Reference<RecipeBookCategory> TRANSMUTATION_IN_WORLD_RECIPE_BOOK_CATEGORY = REGISTRIES.register(
            Registries.RECIPE_BOOK_CATEGORY,
            "transmutation_in_world",
            RecipeBookCategory::new);
    public static final Holder.Reference<RecipeSerializer<TransmutationCraftingRecipe>> TRANSMUTATION_CRAFTING_RECIPE_SERIALIZER = REGISTRIES.register(
            Registries.RECIPE_SERIALIZER,
            "transmutation_crafting",
            TransmutationCraftingRecipe.Serializer::new);
    public static final Holder.Reference<RecipeSerializer<TransmutationSmeltingRecipe>> TRANSMUTATION_SMELTING_RECIPE_SERIALIZER = REGISTRIES.register(
            Registries.RECIPE_SERIALIZER,
            "transmutation_smelting",
            () -> new CustomRecipe.Serializer<>(TransmutationSmeltingRecipe::new));
    public static final Holder.Reference<SoundEvent> ITEM_MINIUM_STONE_CHARGE_SOUND_EVENT = REGISTRIES.registerSoundEvent(
            "item.miniumstone.charge");
    public static final Holder.Reference<SoundEvent> ITEM_MINIUM_STONE_UNCHARGE_SOUND_EVENT = REGISTRIES.registerSoundEvent(
            "item.miniumstone.uncharge");
    public static final Holder.Reference<SoundEvent> ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT = REGISTRIES.registerSoundEvent(
            "item.miniumstone.transmute");
    public static final ResourceKey<LootTable> MINIUM_SHARD_INJECT_LOOT_TABLE = REGISTRIES.registerLootTable(
            "entities/inject/minium_shard");

    static final TagFactory TAGS = TagFactory.make(MiniumStone.MOD_ID);
    /**
     * Items that should be moved by the recipe picker regardless of item components, such as damage, enchantments, and
     * display name.
     */
    public static final TagKey<Item> RECIPES_IGNORE_COMPONENTS_ITEM_TAG = TAGS.registerItemTag(
            "recipes_ignore_components");
    /**
     * Items which are not consumed during a crafting operation and remain in the crafting interface, like items that
     * take durability damage instead.
     */
    public static final TagKey<Item> RECIPES_DO_NOT_CONSUME_ITEM_TAG = TAGS.registerItemTag("recipes_do_not_consume");

    public static void bootstrap() {
        REGISTRIES.register(Registries.SLOT_DISPLAY, "block", () -> BlockSlotDisplay.TYPE);
    }
}

package fuzs.miniumstone.init;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.miniumstone.world.item.crafting.TransmutationCraftingRecipe;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.miniumstone.world.item.crafting.TransmutationSmeltingRecipe;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(MiniumStone.MOD_ID);
    public static final Holder.Reference<Item> MINIUM_SHARD_ITEM = REGISTRY.registerItem("minium_shard", () -> new Item(new Item.Properties()));
    public static final Holder.Reference<Item> MINIUM_STONE_ITEM = REGISTRY.registerItem("minium_stone", () -> new MiniumStoneItem(new Item.Properties().durability(1521).rarity(Rarity.UNCOMMON)));
    public static final Holder.Reference<RecipeType<TransmutationInWorldRecipe>> TRANSMUTATION_IN_WORLD_RECIPE_TYPE = REGISTRY.registerRecipeType("transmutation_in_world");
    public static final Holder.Reference<RecipeSerializer<TransmutationInWorldRecipe>> TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER = REGISTRY.register(Registries.RECIPE_SERIALIZER, "transmutation_in_world", () -> new TransmutationInWorldRecipe.Serializer());
    public static final Holder.Reference<RecipeSerializer<TransmutationCraftingRecipe>> TRANSMUTATION_CRAFTING_RECIPE_SERIALIZER = REGISTRY.register(Registries.RECIPE_SERIALIZER, "transmutation_crafting", () -> new TransmutationCraftingRecipe.Serializer());
    public static final Holder.Reference<RecipeSerializer<TransmutationSmeltingRecipe>> TRANSMUTATION_SMELTING_RECIPE_SERIALIZER = REGISTRY.register(Registries.RECIPE_SERIALIZER, "transmutation_smelting", () -> new SimpleCraftingRecipeSerializer<>(TransmutationSmeltingRecipe::new));
    public static final Holder.Reference<SoundEvent> ITEM_MINIUM_STONE_CHARGE_SOUND_EVENT = REGISTRY.registerSoundEvent("item.miniumstone.charge");
    public static final Holder.Reference<SoundEvent> ITEM_MINIUM_STONE_UNCHARGE_SOUND_EVENT = REGISTRY.registerSoundEvent("item.miniumstone.uncharge");
    public static final Holder.Reference<SoundEvent> ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT = REGISTRY.registerSoundEvent("item.miniumstone.transmute");

    public static final ResourceLocation MINIUM_SHARD_INJECT_LOOT_TABLE = REGISTRY.makeKey("entities/inject/minium_shard");

    public static void touch() {

    }
}

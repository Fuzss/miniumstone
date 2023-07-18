package fuzs.miniumstone.init;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.miniumstone.world.item.crafting.TransmutationCraftingRecipe;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.init.v2.RegistryManager;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.instant(MiniumStone.MOD_ID);
    public static final RegistryReference<Item> MINIUM_SHARD_ITEM = REGISTRY.registerItem("minium_shard", () -> new Item(new Item.Properties()));
    public static final RegistryReference<Item> MINIUM_STONE_ITEM = REGISTRY.registerItem("minium_stone", () -> new MiniumStoneItem(new Item.Properties().durability(1521).rarity(Rarity.UNCOMMON)));
    public static final RegistryReference<RecipeType<TransmutationInWorldRecipe>> TRANSMUTATION_IN_WORLD_RECIPE_TYPE = REGISTRY.registerRecipeType("transmutation_in_world");
    public static final RegistryReference<RecipeSerializer<TransmutationInWorldRecipe>> TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER = REGISTRY.register(Registries.RECIPE_SERIALIZER, "transmutation_in_world", () -> new TransmutationInWorldRecipe.Serializer());
    public static final RegistryReference<RecipeSerializer<TransmutationCraftingRecipe>> TRANSMUTATION_CRAFTING_RECIPE_SERIALIZER = REGISTRY.register(Registries.RECIPE_SERIALIZER, "transmutation_crafting", () -> new TransmutationCraftingRecipe.Serializer());

    public static final ResourceLocation MINIUM_SHARD_INJECT_LOOT_TABLE = REGISTRY.makeKey("entities/inject/minium_shard");

    public static void touch() {

    }
}

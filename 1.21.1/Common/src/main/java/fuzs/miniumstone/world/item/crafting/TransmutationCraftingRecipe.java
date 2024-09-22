package fuzs.miniumstone.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.util.MiniumStoneHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransmutationCraftingRecipe extends ShapelessRecipe {

    public TransmutationCraftingRecipe(ShapelessRecipe shapelessRecipe) {
        this(shapelessRecipe, shapelessRecipe.getIngredients());
    }

    public TransmutationCraftingRecipe(ShapelessRecipe shapelessRecipe, NonNullList<Ingredient> ingredients) {
        this(shapelessRecipe.getGroup(), shapelessRecipe.category(), shapelessRecipe.getResultItem(null), ingredients);
    }

    public TransmutationCraftingRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(group, category, result, ingredients);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        return MiniumStoneHelper.damageMiniumStoneClearRest(container);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.TRANSMUTATION_CRAFTING_RECIPE_SERIALIZER.value();
    }

    public static class Serializer implements RecipeSerializer<TransmutationCraftingRecipe> {
        public static final Codec<TransmutationCraftingRecipe> CODEC = RecipeSerializer.SHAPELESS_RECIPE.codec()
                .flatXmap(recipe -> {
                    ItemStack itemStack = new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value());
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        if (ingredient.test(itemStack)) {
                            return DataResult.error(() -> {
                                return "Transmutation recipe must not include minium stone as ingredient";
                            });
                        }
                    }
                    if (recipe.getIngredients().size() > 8) {
                        return DataResult.error(() -> {
                            return "Too many ingredients for transmutation recipe";
                        });
                    } else {
                        NonNullList<Ingredient> ingredients = Stream.concat(Stream.of(Ingredient.of(ModRegistry.MINIUM_STONE_ITEM.value())),
                                recipe.getIngredients().stream()
                        ).collect(Collectors.toCollection(NonNullList::create));
                        return DataResult.success(new TransmutationCraftingRecipe(recipe, ingredients));
                    }
                }, recipe -> {
                    ItemStack itemStack = new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value());
                    NonNullList<Ingredient> ingredients = recipe.getIngredients()
                            .stream()
                            .filter(ingredient -> !ingredient.test(itemStack))
                            .collect(Collectors.toCollection(NonNullList::create));
                    return DataResult.success(new TransmutationCraftingRecipe(recipe, ingredients));
                });

        @Override
        public Codec<TransmutationCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public TransmutationCraftingRecipe fromNetwork(FriendlyByteBuf buffer) {
            return new TransmutationCraftingRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TransmutationCraftingRecipe recipe) {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
        }
    }
}

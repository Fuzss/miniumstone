package fuzs.miniumstone.data.recipes;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.crafting.TransmutationCraftingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class TransmutationCraftingRecipeBuilder extends ShapelessRecipeBuilder {

    protected TransmutationCraftingRecipeBuilder(HolderGetter<Item> items, RecipeCategory recipeCategory, Ingredient ingredient, ItemStack result, int ingredientCount) {
        super(items, recipeCategory, result);
        if (ingredient.test(new ItemStack(ModRegistry.MINIUM_STONE_ITEM.value()))) {
            throw new IllegalArgumentException("Transmutation recipe must not include minium stone as ingredient");
        } else {
            super.requires(ingredient, ingredientCount);
        }
    }

    @Override
    public ShapelessRecipeBuilder requires(TagKey<Item> tag) {
        throw new UnsupportedOperationException("Set ingredients in constructor!");
    }

    @Override
    public ShapelessRecipeBuilder requires(ItemLike item) {
        throw new UnsupportedOperationException("Set ingredients in constructor!");
    }

    @Override
    public ShapelessRecipeBuilder requires(ItemLike item, int quantity) {
        throw new UnsupportedOperationException("Set ingredients in constructor!");
    }

    @Override
    public ShapelessRecipeBuilder requires(Ingredient ingredient) {
        throw new UnsupportedOperationException("Set ingredients in constructor!");
    }

    @Override
    public ShapelessRecipeBuilder requires(Ingredient ingredient, int quantity) {
        throw new UnsupportedOperationException("Set ingredients in constructor!");
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
        super.save(new RecipeOutput() {

            @Override
            public void accept(ResourceKey<Recipe<?>> key, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {
                recipeOutput.accept(key, new TransmutationCraftingRecipe((ShapelessRecipe) recipe), advancement);
            }

            @Override
            public Advancement.Builder advancement() {
                return recipeOutput.advancement();
            }

            @Override
            public void includeRootAdvancement() {
                // NO-OP
            }
        }, resourceKey);
    }

    public static TransmutationCraftingRecipeBuilder singleResult(HolderGetter<Item> items, Ingredient ingredient, ItemLike result, int ingredientCount) {
        return new TransmutationCraftingRecipeBuilder(items,
                RecipeCategory.MISC,
                ingredient,
                result.asItem().getDefaultInstance().copyWithCount(1),
                ingredientCount);
    }

    public static TransmutationCraftingRecipeBuilder singleIngredient(HolderGetter<Item> items, Ingredient ingredient, ItemLike result, int count) {
        return new TransmutationCraftingRecipeBuilder(items,
                RecipeCategory.MISC,
                ingredient,
                result.asItem().getDefaultInstance().copyWithCount(count),
                1);
    }

    public static TransmutationCraftingRecipeBuilder single(HolderGetter<Item> items, Ingredient ingredient, ItemLike result) {
        return singleIngredient(items, ingredient, result, 1);
    }
}

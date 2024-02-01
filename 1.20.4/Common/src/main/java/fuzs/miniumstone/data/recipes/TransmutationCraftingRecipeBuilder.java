package fuzs.miniumstone.data.recipes;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.crafting.TransmutationCraftingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class TransmutationCraftingRecipeBuilder extends ShapelessRecipeBuilder {

    protected TransmutationCraftingRecipeBuilder(RecipeCategory recipeCategory, Ingredient ingredient, ItemLike result, int ingredientCount, int resultCount) {
        super(recipeCategory, result, resultCount);
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
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        super.save(new RecipeOutput() {
            @Override
            public void accept(ResourceLocation location, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {
                recipeOutput.accept(location, new TransmutationCraftingRecipe((ShapelessRecipe) recipe), advancement);
            }

            @Override
            public Advancement.Builder advancement() {
                return recipeOutput.advancement();
            }
        }, id);
    }

    public static TransmutationCraftingRecipeBuilder singleResult(Ingredient ingredient, ItemLike result, int ingredientCount) {
        return new TransmutationCraftingRecipeBuilder(RecipeCategory.MISC, ingredient, result, ingredientCount, 1);
    }

    public static TransmutationCraftingRecipeBuilder singleIngredient(Ingredient ingredient, ItemLike result, int count) {
        return new TransmutationCraftingRecipeBuilder(RecipeCategory.MISC, ingredient, result, 1, count);
    }

    public static TransmutationCraftingRecipeBuilder single(Ingredient ingredient, ItemLike result) {
        return new TransmutationCraftingRecipeBuilder(RecipeCategory.MISC, ingredient, result, 1, 1);
    }
}

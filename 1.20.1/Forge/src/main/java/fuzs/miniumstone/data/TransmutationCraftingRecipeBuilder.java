package fuzs.miniumstone.data;

import com.google.gson.JsonObject;
import fuzs.miniumstone.init.ModRegistry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TransmutationCraftingRecipeBuilder extends SingleItemRecipeBuilder {
    private final int ingredientCount;

    public TransmutationCraftingRecipeBuilder(RecipeCategory recipeCategory, Ingredient ingredient, ItemLike result, int count, int ingredientCount) {
        super(recipeCategory, ModRegistry.TRANSMUTATION_CRAFTING_RECIPE_SERIALIZER.get(), ingredient, result, count);
        this.ingredientCount = ingredientCount;
    }

    public static TransmutationCraftingRecipeBuilder singleResult(Ingredient ingredient, ItemLike result, int ingredientCount) {
        return new TransmutationCraftingRecipeBuilder(RecipeCategory.MISC, ingredient, result, 1, ingredientCount);
    }

    public static TransmutationCraftingRecipeBuilder singleIngredient(Ingredient ingredient, ItemLike result, int count) {
        return new TransmutationCraftingRecipeBuilder(RecipeCategory.MISC, ingredient, result, count, 1);
    }

    public static TransmutationCraftingRecipeBuilder single(Ingredient ingredient, ItemLike result) {
        return new TransmutationCraftingRecipeBuilder(RecipeCategory.MISC, ingredient, result, 1, 1);
    }

    @Override
    public void save(Consumer<FinishedRecipe> exporter, ResourceLocation identifier) {
        super.save(finishedRecipe -> {
            exporter.accept(new Result(finishedRecipe, this.ingredientCount));
        }, identifier);
    }

    public record Result(FinishedRecipe other, int ingredientCount) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            this.other.serializeRecipeData(jsonObject);
            jsonObject.addProperty("result_count", jsonObject.get("count").getAsInt());
            jsonObject.remove("count");
            jsonObject.addProperty("ingredient_count", this.ingredientCount);
        }

        @Override
        public ResourceLocation getId() {
            return this.other.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.other.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.other.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.other.getAdvancementId();
        }
    }
}

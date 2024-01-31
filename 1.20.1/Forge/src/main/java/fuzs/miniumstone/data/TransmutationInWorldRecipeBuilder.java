package fuzs.miniumstone.data;

import com.google.gson.JsonObject;
import fuzs.miniumstone.init.ModRegistry;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public record TransmutationInWorldRecipeBuilder(Block ingredient, Block result, boolean reversible) implements RecipeBuilder {

    public static TransmutationInWorldRecipeBuilder reversible(Block ingredient, Block result) {
        return new TransmutationInWorldRecipeBuilder(ingredient, result, true);
    }
    public static TransmutationInWorldRecipeBuilder oneWay(Block ingredient, Block result) {
        return new TransmutationInWorldRecipeBuilder(ingredient, result, false);
    }

    @Override
    public RecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item getResult() {
        return this.result.asItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
        finishedRecipeConsumer.accept(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(JsonObject jsonObject) {
                jsonObject.addProperty("ingredient", BuiltInRegistries.BLOCK.getKey(TransmutationInWorldRecipeBuilder.this.ingredient()).toString());
                jsonObject.addProperty("result", BuiltInRegistries.BLOCK.getKey(TransmutationInWorldRecipeBuilder.this.result()).toString());
                jsonObject.addProperty("reversible", TransmutationInWorldRecipeBuilder.this.reversible());
            }

            @Override
            public ResourceLocation getId() {
                return recipeId;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER.get();
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}

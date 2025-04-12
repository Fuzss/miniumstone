package fuzs.miniumstone.data.recipes;

import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class TransmutationInWorldRecipeBuilder extends SingleItemRecipeBuilder {

    public TransmutationInWorldRecipeBuilder(Block ingredient, Block result, boolean reversible) {
        super(RecipeCategory.MISC, (String group, Ingredient ingredient1, ItemStack itemStack) -> {
            return new TransmutationInWorldRecipe(group, ingredient, result, reversible);
        }, Ingredient.of(ingredient), result, 1);
    }

    public static TransmutationInWorldRecipeBuilder reversible(Block ingredient, Block result) {
        return new TransmutationInWorldRecipeBuilder(ingredient, result, true);
    }

    public static TransmutationInWorldRecipeBuilder oneWay(Block ingredient, Block result) {
        return new TransmutationInWorldRecipeBuilder(ingredient, result, false);
    }
}

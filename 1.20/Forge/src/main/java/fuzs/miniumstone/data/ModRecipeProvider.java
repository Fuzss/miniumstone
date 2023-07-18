package fuzs.miniumstone.data;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.function.Consumer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.MINIUM_STONE_ITEM.get())
                .define('#', ModRegistry.MINIUM_SHARD_ITEM.get())
                .define('X', Items.DIAMOND)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .unlockedBy(getHasName(ModRegistry.MINIUM_SHARD_ITEM.get(), Items.DIAMOND), has(ModRegistry.MINIUM_SHARD_ITEM.get(), Items.DIAMOND))
                .save(exporter);
        singleResultReversible(exporter, Items.IRON_INGOT, Items.ENDER_PEARL, 4);
        singleResultReversible(exporter, Items.IRON_INGOT, Items.GOLD_INGOT, 8);
        singleResultReversible(exporter, Items.GOLD_INGOT, Items.DIAMOND, 4);
        singleIngredient(exporter, Items.GLOWSTONE, Items.GLOWSTONE_DUST, 4);
        inWorldReversible(exporter, Blocks.MELON, Blocks.PUMPKIN);
        inWorldReversible(exporter, Blocks.PUMPKIN, Blocks.MELON);
    }

    public static void inWorldReversible(Consumer<FinishedRecipe> exporter, Block ingredient, Block result) {
        TransmutationInWorldRecipeBuilder.reversible(ingredient, result).save(exporter, MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_in_world"));
    }

    public static void inWorldOneWay(Consumer<FinishedRecipe> exporter, Block ingredient, Block result) {
        TransmutationInWorldRecipeBuilder.oneWay(ingredient, result).save(exporter, MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_in_world"));
    }

    public static void singleResult(Consumer<FinishedRecipe> exporter, ItemLike ingredient, ItemLike result, int ingredientCount) {
        TransmutationCraftingRecipeBuilder.singleResult(Ingredient.of(ingredient), result, ingredientCount).unlockedBy(getHasName(ingredient), has(ingredient)).save(exporter, MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting"));
    }

    public static void singleIngredient(Consumer<FinishedRecipe> exporter, ItemLike ingredient, ItemLike result, int resultCount) {
        TransmutationCraftingRecipeBuilder.singleIngredient(Ingredient.of(ingredient), result, resultCount).unlockedBy(getHasName(ingredient), has(ingredient)).save(exporter, MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting"));
    }

    public static void single(Consumer<FinishedRecipe> exporter, ItemLike ingredient, ItemLike result) {
        TransmutationCraftingRecipeBuilder.single(Ingredient.of(ingredient), result).unlockedBy(getHasName(ingredient), has(ingredient)).save(exporter, MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting"));
    }

    public static void singleResultReversible(Consumer<FinishedRecipe> exporter, ItemLike ingredient, ItemLike result, int ingredientCount) {
        singleResult(exporter, ingredient, result, ingredientCount);
        singleIngredient(exporter, result, ingredient, ingredientCount);
    }

    public static void singleIngredientReversible(Consumer<FinishedRecipe> exporter, ItemLike ingredient, ItemLike result, int resultCount) {
        singleIngredient(exporter, ingredient, result, resultCount);
        singleResult(exporter, result, ingredient, resultCount);
    }

    public static void singleReversible(Consumer<FinishedRecipe> exporter, ItemLike ingredient, ItemLike result) {
        single(exporter, ingredient, result);
        single(exporter, result, ingredient);
    }
}

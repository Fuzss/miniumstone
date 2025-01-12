package fuzs.miniumstone.data.recipes;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.crafting.TransmutationSmeltingRecipe;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModRegistry.MINIUM_STONE_ITEM.value())
                .define('#', ModRegistry.MINIUM_SHARD_ITEM.value())
                .define('X', Items.DIAMOND)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .unlockedBy(getHasName(ModRegistry.MINIUM_SHARD_ITEM.value(), Items.DIAMOND),
                        has(ModRegistry.MINIUM_SHARD_ITEM.value(), Items.DIAMOND)
                )
                .save(recipeOutput);
        SpecialRecipeBuilder.special(TransmutationSmeltingRecipe::new).save(recipeOutput, "transmutation_smelting");
        singleResultReversible(recipeOutput, Items.IRON_INGOT, Items.ENDER_PEARL, 4);
        singleResultReversible(recipeOutput, Items.IRON_INGOT, Items.GOLD_INGOT, 8);
        singleResultReversible(recipeOutput, Items.GOLD_INGOT, Items.DIAMOND, 4);
        singleIngredient(recipeOutput, Items.GLOWSTONE, Items.GLOWSTONE_DUST, 4);
        singleIngredient(recipeOutput, Items.BRICK, Items.BRICKS, 4);
        singleResult(recipeOutput, Items.BONE_MEAL, Items.BONE, 3);
        singleResult(recipeOutput, Items.BLAZE_POWDER, Items.BLAZE_ROD, 2);
        singleResult(recipeOutput, Items.GRAVEL, Items.FLINT, 4);
        inWorldReversible(recipeOutput, Blocks.MELON, Blocks.PUMPKIN);
        inWorldReversible(recipeOutput, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM);
        inWorldReversible(recipeOutput, Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK);
        inWorldReversible(recipeOutput, Blocks.SAND, Blocks.DIRT, Blocks.STONE, Blocks.GRASS_BLOCK);
        inWorldOneWay(recipeOutput, Blocks.COBBLESTONE, Blocks.STONE);
        inWorldOneWay(recipeOutput, Blocks.NETHERRACK, Blocks.COBBLESTONE);
        inWorldReversible(recipeOutput, Blocks.NETHER_BRICKS, Blocks.RED_NETHER_BRICKS);
        inWorldReversible(recipeOutput, Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK);
        inWorldReversible(recipeOutput, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);
        inWorldReversible(recipeOutput, Blocks.CRIMSON_STEM, Blocks.WARPED_STEM);
        inWorldReversible(recipeOutput,
                Blocks.OAK_LOG,
                Blocks.BIRCH_LOG,
                Blocks.SPRUCE_LOG,
                Blocks.JUNGLE_LOG,
                Blocks.ACACIA_LOG,
                Blocks.DARK_OAK_LOG,
                Blocks.MANGROVE_LOG,
                Blocks.CHERRY_LOG
        );
        inWorldReversible(recipeOutput,
                Blocks.OAK_LEAVES,
                Blocks.BIRCH_LEAVES,
                Blocks.SPRUCE_LEAVES,
                Blocks.JUNGLE_LEAVES,
                Blocks.ACACIA_LEAVES,
                Blocks.DARK_OAK_LEAVES,
                Blocks.MANGROVE_LEAVES,
                Blocks.CHERRY_LEAVES
        );
        inWorldReversible(recipeOutput,
                Blocks.OAK_PLANKS,
                Blocks.BIRCH_PLANKS,
                Blocks.SPRUCE_PLANKS,
                Blocks.JUNGLE_PLANKS,
                Blocks.ACACIA_PLANKS,
                Blocks.DARK_OAK_PLANKS,
                Blocks.MANGROVE_PLANKS,
                Blocks.CHERRY_PLANKS,
                Blocks.BAMBOO_PLANKS
        );
        inWorldOneWay(recipeOutput, Blocks.GRAVEL, Blocks.SANDSTONE);
        inWorldReversible(recipeOutput, Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        inWorldReversible(recipeOutput, Blocks.CUT_SANDSTONE, Blocks.CUT_RED_SANDSTONE);
        inWorldReversible(recipeOutput, Blocks.CHISELED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE);
        inWorldReversible(recipeOutput,
                Blocks.WHITE_WOOL,
                Blocks.ORANGE_WOOL,
                Blocks.MAGENTA_WOOL,
                Blocks.LIGHT_BLUE_WOOL,
                Blocks.YELLOW_WOOL,
                Blocks.LIME_WOOL,
                Blocks.PINK_WOOL,
                Blocks.GRAY_WOOL,
                Blocks.LIGHT_GRAY_WOOL,
                Blocks.CYAN_WOOL,
                Blocks.PURPLE_WOOL,
                Blocks.BLUE_WOOL,
                Blocks.BROWN_WOOL,
                Blocks.GREEN_WOOL,
                Blocks.RED_WOOL,
                Blocks.BLACK_WOOL
        );
        inWorldReversible(recipeOutput,
                Blocks.WHITE_CARPET,
                Blocks.ORANGE_CARPET,
                Blocks.MAGENTA_CARPET,
                Blocks.LIGHT_BLUE_CARPET,
                Blocks.YELLOW_CARPET,
                Blocks.LIME_CARPET,
                Blocks.PINK_CARPET,
                Blocks.GRAY_CARPET,
                Blocks.LIGHT_GRAY_CARPET,
                Blocks.CYAN_CARPET,
                Blocks.PURPLE_CARPET,
                Blocks.BLUE_CARPET,
                Blocks.BROWN_CARPET,
                Blocks.GREEN_CARPET,
                Blocks.RED_CARPET,
                Blocks.BLACK_CARPET
        );
        inWorldReversible(recipeOutput,
                Blocks.WHITE_TERRACOTTA,
                Blocks.ORANGE_TERRACOTTA,
                Blocks.MAGENTA_TERRACOTTA,
                Blocks.LIGHT_BLUE_TERRACOTTA,
                Blocks.YELLOW_TERRACOTTA,
                Blocks.LIME_TERRACOTTA,
                Blocks.PINK_TERRACOTTA,
                Blocks.GRAY_TERRACOTTA,
                Blocks.LIGHT_GRAY_TERRACOTTA,
                Blocks.CYAN_TERRACOTTA,
                Blocks.PURPLE_TERRACOTTA,
                Blocks.BLUE_TERRACOTTA,
                Blocks.BROWN_TERRACOTTA,
                Blocks.GREEN_TERRACOTTA,
                Blocks.RED_TERRACOTTA,
                Blocks.BLACK_TERRACOTTA
        );
        inWorldReversible(recipeOutput,
                Blocks.WHITE_GLAZED_TERRACOTTA,
                Blocks.ORANGE_GLAZED_TERRACOTTA,
                Blocks.MAGENTA_GLAZED_TERRACOTTA,
                Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Blocks.YELLOW_GLAZED_TERRACOTTA,
                Blocks.LIME_GLAZED_TERRACOTTA,
                Blocks.PINK_GLAZED_TERRACOTTA,
                Blocks.GRAY_GLAZED_TERRACOTTA,
                Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Blocks.CYAN_GLAZED_TERRACOTTA,
                Blocks.PURPLE_GLAZED_TERRACOTTA,
                Blocks.BLUE_GLAZED_TERRACOTTA,
                Blocks.BROWN_GLAZED_TERRACOTTA,
                Blocks.GREEN_GLAZED_TERRACOTTA,
                Blocks.RED_GLAZED_TERRACOTTA,
                Blocks.BLACK_GLAZED_TERRACOTTA
        );
        inWorldReversible(recipeOutput,
                Blocks.WHITE_STAINED_GLASS,
                Blocks.ORANGE_STAINED_GLASS,
                Blocks.MAGENTA_STAINED_GLASS,
                Blocks.LIGHT_BLUE_STAINED_GLASS,
                Blocks.YELLOW_STAINED_GLASS,
                Blocks.LIME_STAINED_GLASS,
                Blocks.PINK_STAINED_GLASS,
                Blocks.GRAY_STAINED_GLASS,
                Blocks.LIGHT_GRAY_STAINED_GLASS,
                Blocks.CYAN_STAINED_GLASS,
                Blocks.PURPLE_STAINED_GLASS,
                Blocks.BLUE_STAINED_GLASS,
                Blocks.BROWN_STAINED_GLASS,
                Blocks.GREEN_STAINED_GLASS,
                Blocks.RED_STAINED_GLASS,
                Blocks.BLACK_STAINED_GLASS
        );
        inWorldReversible(recipeOutput,
                Blocks.WHITE_STAINED_GLASS_PANE,
                Blocks.ORANGE_STAINED_GLASS_PANE,
                Blocks.MAGENTA_STAINED_GLASS_PANE,
                Blocks.LIGHT_BLUE_STAINED_GLASS_PANE,
                Blocks.YELLOW_STAINED_GLASS_PANE,
                Blocks.LIME_STAINED_GLASS_PANE,
                Blocks.PINK_STAINED_GLASS_PANE,
                Blocks.GRAY_STAINED_GLASS_PANE,
                Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
                Blocks.CYAN_STAINED_GLASS_PANE,
                Blocks.PURPLE_STAINED_GLASS_PANE,
                Blocks.BLUE_STAINED_GLASS_PANE,
                Blocks.BROWN_STAINED_GLASS_PANE,
                Blocks.GREEN_STAINED_GLASS_PANE,
                Blocks.RED_STAINED_GLASS_PANE,
                Blocks.BLACK_STAINED_GLASS_PANE
        );
        inWorldReversible(recipeOutput,
                Blocks.WHITE_CANDLE,
                Blocks.ORANGE_CANDLE,
                Blocks.MAGENTA_CANDLE,
                Blocks.LIGHT_BLUE_CANDLE,
                Blocks.YELLOW_CANDLE,
                Blocks.LIME_CANDLE,
                Blocks.PINK_CANDLE,
                Blocks.GRAY_CANDLE,
                Blocks.LIGHT_GRAY_CANDLE,
                Blocks.CYAN_CANDLE,
                Blocks.PURPLE_CANDLE,
                Blocks.BLUE_CANDLE,
                Blocks.BROWN_CANDLE,
                Blocks.GREEN_CANDLE,
                Blocks.RED_CANDLE,
                Blocks.BLACK_CANDLE
        );
        inWorldReversible(recipeOutput,
                Blocks.WHITE_CONCRETE,
                Blocks.ORANGE_CONCRETE,
                Blocks.MAGENTA_CONCRETE,
                Blocks.LIGHT_BLUE_CONCRETE,
                Blocks.YELLOW_CONCRETE,
                Blocks.LIME_CONCRETE,
                Blocks.PINK_CONCRETE,
                Blocks.GRAY_CONCRETE,
                Blocks.LIGHT_GRAY_CONCRETE,
                Blocks.CYAN_CONCRETE,
                Blocks.PURPLE_CONCRETE,
                Blocks.BLUE_CONCRETE,
                Blocks.BROWN_CONCRETE,
                Blocks.GREEN_CONCRETE,
                Blocks.RED_CONCRETE,
                Blocks.BLACK_CONCRETE
        );
        inWorldReversible(recipeOutput,
                Blocks.WHITE_CONCRETE_POWDER,
                Blocks.ORANGE_CONCRETE_POWDER,
                Blocks.MAGENTA_CONCRETE_POWDER,
                Blocks.LIGHT_BLUE_CONCRETE_POWDER,
                Blocks.YELLOW_CONCRETE_POWDER,
                Blocks.LIME_CONCRETE_POWDER,
                Blocks.PINK_CONCRETE_POWDER,
                Blocks.GRAY_CONCRETE_POWDER,
                Blocks.LIGHT_GRAY_CONCRETE_POWDER,
                Blocks.CYAN_CONCRETE_POWDER,
                Blocks.PURPLE_CONCRETE_POWDER,
                Blocks.BLUE_CONCRETE_POWDER,
                Blocks.BROWN_CONCRETE_POWDER,
                Blocks.GREEN_CONCRETE_POWDER,
                Blocks.RED_CONCRETE_POWDER,
                Blocks.BLACK_CONCRETE_POWDER
        );
        inWorldReversible(recipeOutput, Blocks.SHORT_GRASS, Blocks.FERN, Blocks.DEAD_BUSH);
        inWorldReversible(recipeOutput,
                Blocks.POPPY,
                Blocks.DANDELION,
                Blocks.BLUE_ORCHID,
                Blocks.ALLIUM,
                Blocks.AZURE_BLUET,
                Blocks.RED_TULIP,
                Blocks.ORANGE_TULIP,
                Blocks.WHITE_TULIP,
                Blocks.PINK_TULIP,
                Blocks.OXEYE_DAISY,
                Blocks.CORNFLOWER,
                Blocks.LILY_OF_THE_VALLEY
        );
    }

    public static void singleResultReversible(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result, int ingredientCount) {
        singleResult(recipeOutput, ingredient, result, ingredientCount);
        singleIngredient(recipeOutput, result, ingredient, ingredientCount);
    }

    public static void singleIngredient(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result, int resultCount) {
        TransmutationCraftingRecipeBuilder.singleIngredient(Ingredient.of(ingredient), result, resultCount)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput,
                        MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting")
                );
    }

    public static void singleResult(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result, int ingredientCount) {
        TransmutationCraftingRecipeBuilder.singleResult(Ingredient.of(ingredient), result, ingredientCount)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput,
                        MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting")
                );
    }

    public static void inWorldReversible(RecipeOutput recipeOutput, Block... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            Block ingredient = blocks[i];
            Block result = blocks[(i + 1) % blocks.length];
            TransmutationInWorldRecipeBuilder.reversible(ingredient, result)
                    .unlockedBy(getHasName(ingredient), has(ingredient))
                    .save(recipeOutput,
                            MiniumStone.id(getConversionRecipeName(result, ingredient
                            ) + "_transmutation_in_world")
                    );
            single(recipeOutput, ingredient, result);
        }
    }

    public static void inWorldOneWay(RecipeOutput recipeOutput, Block ingredient, Block result) {
        TransmutationInWorldRecipeBuilder.oneWay(ingredient, result)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput,
                        MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_in_world")
                );
        single(recipeOutput, ingredient, result);
    }

    public static void single(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result) {
        TransmutationCraftingRecipeBuilder.single(Ingredient.of(ingredient), result)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput,
                        MiniumStone.id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting")
                );
    }

    public static void singleIngredientReversible(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result, int resultCount) {
        singleIngredient(recipeOutput, ingredient, result, resultCount);
        singleResult(recipeOutput, result, ingredient, resultCount);
    }

    public static void singleReversible(RecipeOutput recipeOutput, ItemLike ingredient, ItemLike result) {
        single(recipeOutput, ingredient, result);
        single(recipeOutput, result, ingredient);
    }
}

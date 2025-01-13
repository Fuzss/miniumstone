package fuzs.miniumstone.data.recipes;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.crafting.TransmutationSmeltingRecipe;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(this.items(), RecipeCategory.TOOLS, ModRegistry.MINIUM_STONE_ITEM.value())
                .define('#', ModRegistry.MINIUM_SHARD_ITEM.value())
                .define('X', Items.DIAMOND)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .unlockedBy(getHasName(ModRegistry.MINIUM_SHARD_ITEM.value()),
                        this.has(ModRegistry.MINIUM_SHARD_ITEM.value()))
                .unlockedBy(getHasName(Items.DIAMOND), this.has(Items.DIAMOND))
                .save(recipeOutput);
        SpecialRecipeBuilder.special(TransmutationSmeltingRecipe::new).save(recipeOutput, "transmutation_smelting");
        this.singleResultReversible(recipeOutput, this.items(), Items.IRON_INGOT, Items.ENDER_PEARL, 4);
        this.singleResultReversible(recipeOutput, this.items(), Items.IRON_INGOT, Items.GOLD_INGOT, 8);
        this.singleResultReversible(recipeOutput, this.items(), Items.GOLD_INGOT, Items.DIAMOND, 4);
        this.singleIngredient(recipeOutput, this.items(), Items.GLOWSTONE, Items.GLOWSTONE_DUST, 4);
        this.singleIngredient(recipeOutput, this.items(), Items.BRICK, Items.BRICKS, 4);
        this.singleResult(recipeOutput, this.items(), Items.BONE_MEAL, Items.BONE, 3);
        this.singleResult(recipeOutput, this.items(), Items.BLAZE_POWDER, Items.BLAZE_ROD, 2);
        this.singleResult(recipeOutput, this.items(), Items.GRAVEL, Items.FLINT, 4);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.MELON, Blocks.PUMPKIN);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.SAND, Blocks.DIRT, Blocks.STONE, Blocks.GRASS_BLOCK);
        this.inWorldOneWay(recipeOutput, this.items(), Blocks.COBBLESTONE, Blocks.STONE);
        this.inWorldOneWay(recipeOutput, this.items(), Blocks.NETHERRACK, Blocks.COBBLESTONE);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.NETHER_BRICKS, Blocks.RED_NETHER_BRICKS);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.CRIMSON_STEM, Blocks.WARPED_STEM);
        this.inWorldReversible(recipeOutput,
                this.items(),
                Blocks.OAK_LOG,
                Blocks.BIRCH_LOG,
                Blocks.SPRUCE_LOG,
                Blocks.JUNGLE_LOG,
                Blocks.ACACIA_LOG,
                Blocks.DARK_OAK_LOG,
                Blocks.MANGROVE_LOG,
                Blocks.CHERRY_LOG);
        this.inWorldReversible(recipeOutput,
                this.items(),
                Blocks.OAK_LEAVES,
                Blocks.BIRCH_LEAVES,
                Blocks.SPRUCE_LEAVES,
                Blocks.JUNGLE_LEAVES,
                Blocks.ACACIA_LEAVES,
                Blocks.DARK_OAK_LEAVES,
                Blocks.MANGROVE_LEAVES,
                Blocks.CHERRY_LEAVES);
        this.inWorldReversible(recipeOutput,
                this.items(),
                Blocks.OAK_PLANKS,
                Blocks.BIRCH_PLANKS,
                Blocks.SPRUCE_PLANKS,
                Blocks.JUNGLE_PLANKS,
                Blocks.ACACIA_PLANKS,
                Blocks.DARK_OAK_PLANKS,
                Blocks.MANGROVE_PLANKS,
                Blocks.CHERRY_PLANKS,
                Blocks.BAMBOO_PLANKS);
        this.inWorldOneWay(recipeOutput, this.items(), Blocks.GRAVEL, Blocks.SANDSTONE);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.CUT_SANDSTONE, Blocks.CUT_RED_SANDSTONE);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.CHISELED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_WOOL);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_CARPET);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_TERRACOTTA);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_GLAZED_TERRACOTTA);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_STAINED_GLASS);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_STAINED_GLASS_PANE);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_CANDLE);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_CONCRETE);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.BLACK_CONCRETE_POWDER);
        this.inWorldReversible(recipeOutput, this.items(), Blocks.SHORT_GRASS, Blocks.FERN, Blocks.DEAD_BUSH);
        this.inWorldReversible(recipeOutput,
                this.items(),
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
                Blocks.LILY_OF_THE_VALLEY);
    }

    public void singleResultReversible(RecipeOutput recipeOutput, HolderGetter<Item> items, ItemLike ingredient, ItemLike result, int ingredientCount) {
        this.singleResult(recipeOutput, items, ingredient, result, ingredientCount);
        this.singleIngredient(recipeOutput, items, result, ingredient, ingredientCount);
    }

    public void singleIngredient(RecipeOutput recipeOutput, HolderGetter<Item> items, ItemLike ingredient, ItemLike result, int resultCount) {
        TransmutationCraftingRecipeBuilder.singleIngredient(items, Ingredient.of(ingredient), result, resultCount)
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(recipeOutput, id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting"));
    }

    public void singleResult(RecipeOutput recipeOutput, HolderGetter<Item> items, ItemLike ingredient, ItemLike result, int ingredientCount) {
        TransmutationCraftingRecipeBuilder.singleResult(items, Ingredient.of(ingredient), result, ingredientCount)
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(recipeOutput, id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting"));
    }

    public void inWorldReversible(RecipeOutput recipeOutput, HolderGetter<Item> items, Block... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            Block ingredient = blocks[i];
            Block result = blocks[(i + 1) % blocks.length];
            TransmutationInWorldRecipeBuilder.reversible(ingredient, result)
                    .unlockedBy(getHasName(ingredient), this.has(ingredient))
                    .save(recipeOutput, id(getConversionRecipeName(result, ingredient) + "_transmutation_in_world"));
            this.single(recipeOutput, items, ingredient, result);
        }
    }

    public void inWorldOneWay(RecipeOutput recipeOutput, HolderGetter<Item> items, Block ingredient, Block result) {
        TransmutationInWorldRecipeBuilder.oneWay(ingredient, result)
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(recipeOutput, id(getConversionRecipeName(result, ingredient) + "_transmutation_in_world"));
        this.single(recipeOutput, items, ingredient, result);
    }

    public void single(RecipeOutput recipeOutput, HolderGetter<Item> items, ItemLike ingredient, ItemLike result) {
        TransmutationCraftingRecipeBuilder.single(items, Ingredient.of(ingredient), result)
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(recipeOutput, id(getConversionRecipeName(result, ingredient) + "_transmutation_crafting"));
    }

    public void singleIngredientReversible(RecipeOutput recipeOutput, HolderGetter<Item> items, ItemLike ingredient, ItemLike result, int resultCount) {
        this.singleIngredient(recipeOutput, items, ingredient, result, resultCount);
        this.singleResult(recipeOutput, items, result, ingredient, resultCount);
    }

    public void singleReversible(RecipeOutput recipeOutput, HolderGetter<Item> items, ItemLike ingredient, ItemLike result) {
        this.single(recipeOutput, items, ingredient, result);
        this.single(recipeOutput, items, result, ingredient);
    }

    static ResourceKey<Recipe<?>> id(String path) {
        return ResourceKey.create(Registries.RECIPE, MiniumStone.id(path));
    }
}

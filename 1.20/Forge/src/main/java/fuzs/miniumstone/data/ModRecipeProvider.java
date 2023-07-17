package fuzs.miniumstone.data;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
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
    }
}

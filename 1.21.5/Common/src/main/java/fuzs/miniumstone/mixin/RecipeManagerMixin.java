package fuzs.miniumstone.mixin;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.network.ClientboundTransmutationInWorldRecipesMessage;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.SelectableRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Shadow
    private RecipeMap recipes;

    @Inject(method = "finalizeRecipeLoading", at = @At("TAIL"))
    public void finalizeRecipeLoading(FeatureFlagSet enabledFeatures, CallbackInfo callback) {
        List<SelectableRecipe.SingleInputEntry<TransmutationInWorldRecipe>> list = new ArrayList<>();
        Collection<RecipeHolder<TransmutationInWorldRecipe>> recipeHolders = this.recipes.byType(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value());
        for (RecipeHolder<?> recipeHolder : recipeHolders) {
            if (recipeHolder.value() instanceof TransmutationInWorldRecipe transmutationInWorldRecipe) {
                list.add(new SelectableRecipe.SingleInputEntry<>(transmutationInWorldRecipe.input(),
                        new SelectableRecipe<>(transmutationInWorldRecipe.resultDisplay(),
                                Optional.of((RecipeHolder<TransmutationInWorldRecipe>) recipeHolder))));
            }
        }
        ClientboundTransmutationInWorldRecipesMessage.transmutationInWorldRecipes = new SelectableRecipe.SingleInputSet<>(
                list);
    }
}
